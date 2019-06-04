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

@Component
public class TemplateDataMapper {

    // CCD Fields and Values
    private static final String CASE_DATA = "case_data";
    private static final String CASE_DETAILS = "caseDetails";
    private static final String CCD_DATE_FORMAT = "yyyy-MM-dd";
    private static final String CO_RESPONDENT_WISH_TO_NAME = "D8ReasonForDivorceAdulteryWishToName";
    private static final String COURT_CONTACT_KEY = "CourtContactDetails";
    private static final String COURT_HEARING_DATE_KEY = "DateOfHearing";
    private static final String COURT_HEARING_JSON_KEY = "DateAndTimeOfHearing";
    private static final String COURT_HEARING_TIME_KEY = "TimeOfHearing";
    private static final String DN_APPROVAL_DATE_KEY = "DNApprovalDate";
    private static final String LETTER_DATE_FORMAT = "dd MMMM yyyy";
    private static final String SERVICE_CENTRE_COURT_CONTACT_DETAILS = "c\\o East Midlands Regional Divorce"
        + " Centre\nPO Box 10447\nNottingham\nNG2 9QN\nEmail: contactdivorce@justice.gov.uk\nPhone: 0300 303"
        + " 0642 (from 8.30am to 5pm)";
    private static final String SOLICITOR_IS_NAMED_CO_RESPONDENT = "D8ReasonForDivorceAdulteryIsNamed";
    private static final String TYPE_COSTS_DECISION_JSON_KEY = "typeCostsDecision";
    private static final String WHO_PAYS_COSTS_JSON_KEY = "whoPaysCosts";
    private static final String WHO_PAYS_COSTS_DEFAULT_VALUE = "respondent";

    // CCD Case Sensitive Fields
    private static final String TYPE_COSTS_DECISION_TEMPLATE_KEY = "TypeCostsDecision";
    private static final String WHO_PAYS_COSTS_TEMPLATE_KEY = "WhoPaysCosts";

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

        // Setup hardcoded service centre court contact details
        data.put(COURT_CONTACT_KEY, SERVICE_CENTRE_COURT_CONTACT_DETAILS);

        // CoE template is using upperCase variable names compared to whats currently in the CCD configuration
        if (Objects.nonNull(data.get(WHO_PAYS_COSTS_JSON_KEY))) {
            data.put(WHO_PAYS_COSTS_TEMPLATE_KEY, data.get(WHO_PAYS_COSTS_JSON_KEY));
        } else {
            data.put(WHO_PAYS_COSTS_TEMPLATE_KEY, WHO_PAYS_COSTS_DEFAULT_VALUE);
        }

        if (Objects.nonNull(data.get(TYPE_COSTS_DECISION_JSON_KEY))) {
            data.put(TYPE_COSTS_DECISION_TEMPLATE_KEY, data.get(TYPE_COSTS_DECISION_JSON_KEY));
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
