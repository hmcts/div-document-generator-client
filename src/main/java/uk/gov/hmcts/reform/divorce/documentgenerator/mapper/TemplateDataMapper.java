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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.ADULTERY_FOUND_OUT_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.BEHAVIOUR_MOST_RECENT_DATE_DN_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CASE_DATA;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CASE_DETAILS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CASE_DETAILS_STATEMENT_CLARIFICATION_VALUE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CCD_DATE_FORMAT;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CCD_DATE_TIME_FORMAT;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CLIAM_COSTS_FROM;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CLIAM_COSTS_FROM_CORESP;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CLIAM_COSTS_FROM_RESP;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CLIAM_COSTS_FROM_RESP_CORESP;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_CONTACT_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_JSON_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_TIME_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_WISH_TO_NAME;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_MARRIAGE_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_ABSOLUTE_ELIGIBLE_FROM_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_ABSOLUTE_GRANTED_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_GRANTED_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_SUBMITTED_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_APPROVAL_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.FREE_TEXT_ORDER_VALUE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.HAS_CASE_DETAILS_STATEMENT_CLARIFICATION_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.HAS_FREE_TEXT_ORDER_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.HAS_INSUFFICIENT_DETAILS_REJECTION_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.HAS_JURISDICTION_CLARIFICATION_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.HAS_MARRIAGE_CERT_CLARIFICATION_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.HAS_MARRIAGE_CERT_TRANSLATION_CLARIFICATION_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.HAS_NO_CRITERIA_REJECTION_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.HAS_NO_JURISDICTION_REJECTION_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.HAS_ONLY_FREE_TEXT_ORDER_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.HAS_PREVIOUS_PROCEEDINGS_CLARIFICATION_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.INSUFFICIENT_DETAILS_REJECTION_VALUE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.ISSUE_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.JURISDICTION_CLARIFICATION_VALUE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.LETTER_DATE_FORMAT;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.MARRIAGE_CERT_CLARIFICATION_VALUE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.MARRIAGE_CERT_TRANSLATION_CLARIFICATION_VALUE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.NO_CRITERIA_REJECTION_VALUE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.NO_JURISDICTION_REJECTION_VALUE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PREVIOUS_PROCEEDINGS_CLARIFICATION_VALUE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.REFUSAL_CLARIFICATION_REASONS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.REFUSAL_REJECTION_REASONS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SERVICE_CENTRE_COURT_CONTACT_DETAILS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SERVICE_CENTRE_COURT_NAME;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SERVICE_COURT_NAME_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SOLICITOR_IS_NAMED_CO_RESPONDENT;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.YES_VALUE;

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

        if (Objects.nonNull(data.get(DN_APPROVAL_DATE_KEY))) {
            data.put(DN_APPROVAL_DATE_KEY, formatDateFromCCD((String) data.get(DN_APPROVAL_DATE_KEY)));
        }

        if (Objects.nonNull(data.get(D8_MARRIAGE_DATE_KEY))) {
            data.put(D8_MARRIAGE_DATE_KEY, formatDateFromCCD((String) data.get(D8_MARRIAGE_DATE_KEY)));
        }

        if (Objects.nonNull(data.get(DECREE_NISI_GRANTED_DATE_KEY))) {
            data.put(DECREE_NISI_GRANTED_DATE_KEY, formatDateFromCCD((String) data.get(DECREE_NISI_GRANTED_DATE_KEY)));
        }

        if (Objects.nonNull(data.get(DECREE_ABSOLUTE_GRANTED_DATE_KEY))) {
            data.put(DECREE_ABSOLUTE_GRANTED_DATE_KEY,
                formatDateTimeFromCCD((String) data.get(DECREE_ABSOLUTE_GRANTED_DATE_KEY)));
        }

        if (Objects.nonNull(data.get(DECREE_ABSOLUTE_ELIGIBLE_FROM_DATE_KEY))) {
            data.put(DECREE_ABSOLUTE_ELIGIBLE_FROM_DATE_KEY,
                formatDateFromCCD((String) data.get(DECREE_ABSOLUTE_ELIGIBLE_FROM_DATE_KEY)));
        }

        if (Objects.nonNull(data.get(DECREE_NISI_SUBMITTED_DATE_KEY))) {
            data.put(DECREE_NISI_SUBMITTED_DATE_KEY,
                formatDateFromCCD((String) data.get(DECREE_NISI_SUBMITTED_DATE_KEY)));
        }

        if (Objects.nonNull(data.get(ISSUE_DATE_KEY))) {
            data.put(ISSUE_DATE_KEY, formatDateFromCCD((String) data.get(ISSUE_DATE_KEY)));
        }

        if (Objects.nonNull(data.get(ADULTERY_FOUND_OUT_DATE_KEY))) {
            data.put(ADULTERY_FOUND_OUT_DATE_KEY,
                formatDateFromCCD((String) data.get(ADULTERY_FOUND_OUT_DATE_KEY)));
        }

        if (Objects.nonNull(data.get(BEHAVIOUR_MOST_RECENT_DATE_DN_KEY))) {
            data.put(BEHAVIOUR_MOST_RECENT_DATE_DN_KEY,
                formatDateFromCCD((String) data.get(BEHAVIOUR_MOST_RECENT_DATE_DN_KEY)));
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

        if (Objects.isNull(data.get(COURT_CONTACT_KEY))) {
            data.put(COURT_CONTACT_KEY, SERVICE_CENTRE_COURT_CONTACT_DETAILS);
        }

        if (Objects.nonNull(data.get(CLIAM_COSTS_FROM))) {
            List<String> listOfClaimCostsFrom =
                mapper.convertValue(data.get(CLIAM_COSTS_FROM), ArrayList.class);
            if (listOfClaimCostsFrom.contains("respondent")) {
                if (listOfClaimCostsFrom.contains("correspondent")) {
                    data.put(CLIAM_COSTS_FROM_RESP_CORESP, YES_VALUE);
                } else {
                    data.put(CLIAM_COSTS_FROM_RESP, YES_VALUE);
                }
            } else if (listOfClaimCostsFrom.contains("correspondent")) {
                data.put(CLIAM_COSTS_FROM_CORESP, YES_VALUE);
            }
        }

        data.put(SERVICE_COURT_NAME_KEY, SERVICE_CENTRE_COURT_NAME);

        if (Objects.nonNull(data.get(REFUSAL_CLARIFICATION_REASONS))) {
            List<String> clarificationReasons =
                mapper.convertValue(data.get(REFUSAL_CLARIFICATION_REASONS), ArrayList.class);
            data.put(HAS_JURISDICTION_CLARIFICATION_KEY,
                clarificationReasons.contains(JURISDICTION_CLARIFICATION_VALUE));
            data.put(HAS_MARRIAGE_CERT_CLARIFICATION_KEY,
                clarificationReasons.contains(MARRIAGE_CERT_CLARIFICATION_VALUE));
            data.put(HAS_MARRIAGE_CERT_TRANSLATION_CLARIFICATION_KEY,
                clarificationReasons.contains(MARRIAGE_CERT_TRANSLATION_CLARIFICATION_VALUE));
            data.put(HAS_PREVIOUS_PROCEEDINGS_CLARIFICATION_KEY,
                clarificationReasons.contains(PREVIOUS_PROCEEDINGS_CLARIFICATION_VALUE));
            data.put(HAS_CASE_DETAILS_STATEMENT_CLARIFICATION_KEY,
                clarificationReasons.contains(CASE_DETAILS_STATEMENT_CLARIFICATION_VALUE));
            data.put(HAS_FREE_TEXT_ORDER_KEY, clarificationReasons.contains(FREE_TEXT_ORDER_VALUE));
            data.put(HAS_ONLY_FREE_TEXT_ORDER_KEY, clarificationReasons.equals(Arrays.asList(FREE_TEXT_ORDER_VALUE)));
        }

        if (Objects.nonNull(data.get(REFUSAL_REJECTION_REASONS))) {
            List<String> rejectionReasons =
                mapper.convertValue(data.get(REFUSAL_REJECTION_REASONS), ArrayList.class);
            data.put(HAS_NO_JURISDICTION_REJECTION_KEY, rejectionReasons.contains(NO_JURISDICTION_REJECTION_VALUE));
            data.put(HAS_NO_CRITERIA_REJECTION_KEY, rejectionReasons.contains(NO_CRITERIA_REJECTION_VALUE));
            data.put(HAS_INSUFFICIENT_DETAILS_REJECTION_KEY,
                rejectionReasons.contains(INSUFFICIENT_DETAILS_REJECTION_VALUE));
            data.put(HAS_FREE_TEXT_ORDER_KEY, rejectionReasons.contains(FREE_TEXT_ORDER_VALUE));
            data.put(HAS_ONLY_FREE_TEXT_ORDER_KEY, rejectionReasons.equals(Arrays.asList(FREE_TEXT_ORDER_VALUE)));
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
        try {
            ccdDateString = formatDateFromPattern(ccdDateString, CCD_DATE_FORMAT);
        } catch (Exception e) {
            throw new PDFGenerationException("Unable to format CCD Date Type field", e);
        }
        return ccdDateString;
    }

    private String formatDateTimeFromCCD(String ccdDateString) {
        try {
            ccdDateString = formatDateFromPattern(ccdDateString, CCD_DATE_TIME_FORMAT);
        } catch (Exception e) {
            throw new PDFGenerationException("Unable to format CCD DateTime Type field", e);
        }
        return ccdDateString;
    }

    private String formatDateFromPattern(String ccdDateString, String fromPattern) {
        if (Objects.nonNull(ccdDateString)) {
            DateTimeFormatter ccdFormatter = DateTimeFormatter.ofPattern(fromPattern);
            LocalDate ccdDate = LocalDate.parse(ccdDateString, ccdFormatter);

            DateTimeFormatter letterFormatter = DateTimeFormatter.ofPattern(LETTER_DATE_FORMAT);
            ccdDateString = ccdDate.format(letterFormatter);
        }
        return ccdDateString;
    }
}
