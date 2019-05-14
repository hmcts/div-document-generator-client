package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.DocmosisBasePdfConfig;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.CollectionMember;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.request.PdfDocumentRequest;
import uk.gov.hmcts.reform.divorce.documentgenerator.exception.PDFGenerationException;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.PDFGenerationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

@Component
@Slf4j
@Qualifier("docmosisPdfGenerator")
public class DocmosisPDFGenerationServiceImpl implements PDFGenerationService {

    // CCD Values
    private static final String YES_VALUE = "YES";
    private static final String RESPONDENT_KEY = "respondent";
    private static final String CORESPONDENT_KEY = "correspondent";
    private static final String CASE_DETAILS = "caseDetails";
    private static final String CASE_DATA = "case_data";
    private static final String COURT_NAME_KEY = "courtName";
    private static final String SERVICE_CENTRE_COURT_NAME = "Courts and Tribunals Service Centre";
    private static final String COURT_CONTACT_KEY = "CourtContactDetails";
    private static final String SERVICE_CENTRE_COURT_CONTACT_DETAILS = "c\\o East Midlands Regional Divorce"
        + " Centre\nPO Box 10447\nNottingham<br/>NG2 9QN\nEmail: divorcecase@justice.gov.uk\nPhone: 0300 303"
        + " 0642 (from 8.30am to 5pm)";
    private static final String CLAIM_COSTS_JSON_KEY = "D8DivorceCostsClaim";
    private static final String CLAIM_COSTS_FROM_JSON_KEY = "D8DivorceClaimFrom";
    private static final String WHO_PAYS_COSTS_KEY = "whoPaysCosts";
    private static final String WHO_PAYS_COSTS_RESPONDENT = "respondent";
    private static final String WHO_PAYS_COSTS_CORESPONDENT = "corespondent";
    private static final String WHO_PAYS_COSTS_BOTH = "respondentAndCorespondent";
    private static final String COURT_HEARING_JSON_KEY = "DateAndTimeOfHearing";
    private static final String COURT_HEARING_DATE_KEY = "DateOfHearing";
    private static final String COURT_HEARING_TIME_KEY = "TimeOfHearing";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DocmosisBasePdfConfig docmosisBasePdfConfig;

    @Value("${docmosis.service.pdf-service.uri}")
    private String pdfServiceEndpoint;

    @Value("${docmosis.service.pdf-service.accessKey}")
        private String pdfServiceAccessKey;

    @Override
    public byte[] generate(String templateName, Map<String, Object> placeholders) {
        checkArgument(!isNullOrEmpty(templateName), "document generation template cannot be empty");
        checkNotNull(placeholders, "placeholders map cannot be null");

        log.info("Making request to pdf service to generate pdf document with template [{}]"
            + " and placeholders of size [{}]", templateName, placeholders.size());

        try {
            // Remove this log when tested
            log.info("Making Docmosis Request From {}", pdfServiceEndpoint);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            HttpEntity<PdfDocumentRequest> httpEntity = new HttpEntity<>(request(templateName, placeholders), headers);

            ResponseEntity<byte[]> response =
                restTemplate.exchange(pdfServiceEndpoint, HttpMethod.POST, httpEntity, byte[].class);
            return response.getBody();
        } catch (Exception e) {
            throw new PDFGenerationException("Failed to request PDF from REST endpoint " + e.getMessage(), e);
        }
    }

    private PdfDocumentRequest request(String templateName, Map<String, Object> placeholders) {
        return PdfDocumentRequest.builder()
            .accessKey(pdfServiceAccessKey)
            .templateName(templateName)
            .outputName("result.pdf")
            .data(caseData(placeholders)).build();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> caseData(Map<String, Object> placeholders) {
        ObjectMapper mapper = new ObjectMapper();

        // Get case data
        Map<String, Object> data = (Map<String, Object>) ((Map) placeholders.get(CASE_DETAILS)).get(CASE_DATA);

        // Setup court details
        data.put(COURT_NAME_KEY, SERVICE_CENTRE_COURT_NAME);
        data.put(COURT_CONTACT_KEY, SERVICE_CENTRE_COURT_CONTACT_DETAILS);

        // Setup who is paying costs if claims cost is chosen
        if (YES_VALUE.equals(data.get(CLAIM_COSTS_JSON_KEY))) {
            List<String> claimFrom = mapper.convertValue(data.get(CLAIM_COSTS_FROM_JSON_KEY), ArrayList.class);
            if (claimFrom.contains(RESPONDENT_KEY) && claimFrom.contains(CORESPONDENT_KEY)) {
                data.put(WHO_PAYS_COSTS_KEY, WHO_PAYS_COSTS_BOTH);
            } else if (claimFrom.contains(RESPONDENT_KEY)) {
                data.put(WHO_PAYS_COSTS_KEY, WHO_PAYS_COSTS_RESPONDENT);
            } else if (claimFrom.contains(CORESPONDENT_KEY)) {
                data.put(WHO_PAYS_COSTS_KEY, WHO_PAYS_COSTS_CORESPONDENT);
            }
        }

        // Setup latest court hearing date and time if exists
        if (Objects.nonNull(data.get(COURT_HEARING_JSON_KEY))) {
            List<Object> listOfCourtHearings =
                mapper.convertValue(data.get(COURT_HEARING_JSON_KEY), ArrayList.class);

            // Last element of the list is the most recent court hearing
            CollectionMember<Map<String, Object>> latestCourtHearing =
                mapper.convertValue(listOfCourtHearings.get(listOfCourtHearings.size() - 1), CollectionMember.class);

            data.put(COURT_HEARING_DATE_KEY, latestCourtHearing.getValue().get(COURT_HEARING_DATE_KEY));
            data.put(COURT_HEARING_TIME_KEY, latestCourtHearing.getValue().get(COURT_HEARING_TIME_KEY));
        }

        // Get page assets
        data.put(docmosisBasePdfConfig.getDisplayTemplateKey(), docmosisBasePdfConfig.getDisplayTemplateVal());
        data.put(docmosisBasePdfConfig.getFamilyCourtImgKey(), docmosisBasePdfConfig.getFamilyCourtImgVal());
        data.put(docmosisBasePdfConfig.getHmctsImgKey(), docmosisBasePdfConfig.getHmctsImgVal());
        return data;
    }

}
