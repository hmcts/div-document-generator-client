package uk.gov.hmcts.reform.divorce.documentgenerator.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.DocmosisBasePdfConfig;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.CcdCollectionMember;
import uk.gov.hmcts.reform.divorce.documentgenerator.exception.PDFGenerationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CARE_OF_PREFIX;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CASE_DATA;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CASE_DETAILS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CCD_DATE_FORMAT;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_CONTACT_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_JSON_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_TIME_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_NAME_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_WISH_TO_NAME;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_APPROVAL_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.LETTER_DATE_FORMAT;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.NEWLINE_DELIMITER;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SERVICE_CENTRE_COURT_CONTACT_DETAILS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SERVICE_CENTRE_COURT_NAME;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SOLICITOR_IS_NAMED_CO_RESPONDENT;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SPACE_DELIMITER;

@Component
public class TemplateDataMapper {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private DocmosisBasePdfConfig docmosisBasePdfConfig;

    @SuppressWarnings("unchecked")
    public Map<String, Object> map(Map<String, Object> placeholders) {
        // Get case data
        Map<String, Object> data = (Map<String, Object>) ((Map) placeholders.get(CASE_DETAILS)).get(CASE_DATA);

        // Setup Formatted DN Approval Date if exists
        if (Objects.nonNull(data.get(DN_APPROVAL_DATE_KEY))) {
            data.put(DN_APPROVAL_DATE_KEY, formatDateFromCCD((String) data.get(DN_APPROVAL_DATE_KEY)));
        }

        // If Solicitor WishToNameCoRespondent is set, also set the default wishToName field
        if (Objects.nonNull(data.get(SOLICITOR_IS_NAMED_CO_RESPONDENT))) {
            data.put(CO_RESPONDENT_WISH_TO_NAME,
                StringUtils.upperCase((String) data.get(SOLICITOR_IS_NAMED_CO_RESPONDENT)));
        }

        // Setup latest court hearing date and time if exists
        if (Objects.nonNull(data.get(COURT_HEARING_JSON_KEY))) {
            List<Object> listOfCourtHearings =
                mapper.convertValue(data.get(COURT_HEARING_JSON_KEY), ArrayList.class);

            // Last element of the list is the most recent court hearing
            CcdCollectionMember<Map<String, Object>> latestCourtHearing =
                mapper.convertValue(listOfCourtHearings.get(listOfCourtHearings.size() - 1), CcdCollectionMember.class);

            data.put(COURT_HEARING_DATE_KEY,
                formatDateFromCCD((String) latestCourtHearing.getValue().get(COURT_HEARING_DATE_KEY)));
            data.put(COURT_HEARING_TIME_KEY, latestCourtHearing.getValue().get(COURT_HEARING_TIME_KEY));
        }

        if (Objects.nonNull(data.get(COURT_NAME_KEY))) {
            if (Objects.nonNull(data.get(COURT_CONTACT_KEY))) {
                String careOfCourt = String.join(SPACE_DELIMITER, CARE_OF_PREFIX, String.valueOf(data.get(COURT_NAME_KEY)));
                data.put(COURT_CONTACT_KEY, String.join(NEWLINE_DELIMITER, careOfCourt, String.valueOf(data.get(COURT_CONTACT_KEY))));
            } else {
                data.put(COURT_CONTACT_KEY, SERVICE_CENTRE_COURT_CONTACT_DETAILS);
            }

            // We are told to hardcode the Court Name on the templates for now, but will leave as a variable for future change
            data.put(COURT_NAME_KEY, SERVICE_CENTRE_COURT_NAME);
        }

        // Get page assets
        data.putAll(getPageAssets());

        return data;
    }

    private Map<String, Object> getPageAssets() {
        Map<String, Object> pageAssets = new HashMap<>();
        pageAssets.put(docmosisBasePdfConfig.getDisplayTemplateKey(), docmosisBasePdfConfig.getDisplayTemplateVal());
        pageAssets.put(docmosisBasePdfConfig.getFamilyCourtImgKey(), docmosisBasePdfConfig.getFamilyCourtImgVal());
        pageAssets.put(docmosisBasePdfConfig.getHmctsImgKey(), docmosisBasePdfConfig.getHmctsImgVal());

        return pageAssets;
    }

    private String formatDateFromCCD(String ccdDateString) {
        if (Objects.nonNull(ccdDateString)) {
            try {
                DateTimeFormatter ccdFormatter = DateTimeFormatter.ofPattern(CCD_DATE_FORMAT);
                LocalDate ccdDate = LocalDate.parse(ccdDateString, ccdFormatter);

                DateTimeFormatter letterFormatter = DateTimeFormatter.ofPattern(LETTER_DATE_FORMAT);
                ccdDateString = ccdDate.format(letterFormatter);
            } catch (Exception e) {
                throw new PDFGenerationException("Unable to format CCD Date Type field", e);
            }
        }
        return ccdDateString;
    }
}
