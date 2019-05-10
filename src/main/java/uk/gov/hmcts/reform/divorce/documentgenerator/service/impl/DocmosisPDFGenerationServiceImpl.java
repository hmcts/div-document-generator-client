package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.DocmosisBasePdfConfig;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.request.PdfDocumentRequest;
import uk.gov.hmcts.reform.divorce.documentgenerator.exception.PDFGenerationException;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.PDFGenerationService;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

@Component
@Slf4j
@Qualifier("docmosisPdfGenerator")
public class DocmosisPDFGenerationServiceImpl implements PDFGenerationService {

    private static final String CASE_DETAILS = "caseDetails";
    private static final String CASE_DATA = "case_data";
    private static final String COURT_NAME_KEY = "courtName";
    private static final String SERVICE_CENTRE_COURT_NAME = "Courts and Tribunals Service Centre";
    private static final String COURT_CONTACT_KEY = "CourtContactDetails";
    private static final String SERVICE_CENTRE_COURT_CONTACT_DETAILS = "c\\o East Midlands Regional Divorce " +
        "Centre\nPO Box 10447\nNottingham<br/>NG2 9QN\nEmail: divorcecase@justice.gov.uk\nPhone: 0300 303" +
        " 0642 (from 8.30am to 5pm)";

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

        log.info("Making request to pdf service to generate pdf document with template "
            + "and placeholders of size [{}]", templateName, placeholders.size());

        try {
            // Remove this log when tested
            log.info("Making Docmosis Request From {}", pdfServiceEndpoint);
            ResponseEntity<byte[]> response =
                restTemplate.postForEntity(pdfServiceEndpoint, request(templateName, placeholders), byte[].class);
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
        Map<String, Object> data = (Map<String, Object>) ((Map) placeholders.get(CASE_DETAILS)).get(CASE_DATA);
        data.put(COURT_NAME_KEY, SERVICE_CENTRE_COURT_NAME);
        data.put(COURT_CONTACT_KEY, SERVICE_CENTRE_COURT_CONTACT_DETAILS);
        data.put(docmosisBasePdfConfig.getDisplayTemplateKey(), docmosisBasePdfConfig.getDisplayTemplateVal());
        data.put(docmosisBasePdfConfig.getFamilyCourtImgKey(), docmosisBasePdfConfig.getFamilyCourtImgVal());
        data.put(docmosisBasePdfConfig.getHmctsImgKey(), docmosisBasePdfConfig.getHmctsImgVal());
        return data;
    }

}
