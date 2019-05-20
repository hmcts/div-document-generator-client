package uk.gov.hmcts.reform.divorce.documentgenerator.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.DocmosisBasePdfConfig;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.CollectionMember;
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
    private static final String CLAIM_COSTS_FROM_JSON_KEY = "D8DivorceClaimFrom";
    private static final String CLAIM_COSTS_JSON_KEY = "D8DivorceCostsClaim";
    private static final String CORESPONDENT_KEY = "correspondent";
    private static final String COURT_CONTACT_KEY = "CourtContactDetails";
    private static final String COURT_HEARING_DATE_KEY = "DateOfHearing";
    private static final String COURT_HEARING_JSON_KEY = "DateAndTimeOfHearing";
    private static final String COURT_HEARING_TIME_KEY = "TimeOfHearing";
    private static final String D8_MARRIAGE_DATE_KEY = "D8MarriageDate";
    private static final String DA_APPLICABLE_DATE_KEY = "DecreeAbsoluteApplicableDate";
    private static final String DN_APPROVAL_DATE_KEY = "DNApprovalDate";
    private static final String DN_GRANTED_DATE_KEY = "DecreeNisiGrantedDate";
    private static final String LETTER_DATE_FORMAT = "dd MMMM yyyy";
    private static final String RESPONDENT_KEY = "respondent";
    private static final long ONE_DAY = 1l;
    private static final String SERVICE_CENTRE_COURT_CONTACT_DETAILS = "c\\o East Midlands Regional Divorce"
        + " Centre\nPO Box 10447\nNottingham<\nNG2 9QN\nEmail: contactdivorce@justice.gov.uk\nPhone: 0300 303"
        + " 0642 (from 8.30am to 5pm)";
    private static final long SIX_WEEKS = 6l;
    private static final String WHO_PAYS_COSTS_BOTH = "respondentAndCorespondent";
    private static final String WHO_PAYS_COSTS_CORESPONDENT = "corespondent";
    private static final String WHO_PAYS_COSTS_KEY = "whoPaysCosts";
    private static final String WHO_PAYS_COSTS_RESPONDENT = "respondent";
    private static final String YES_VALUE = "YES";

    private ObjectMapper mapper;
    private DocmosisBasePdfConfig docmosisBasePdfConfig;
    private DateTimeFormatter ccdFormatter;
    private DateTimeFormatter letterFormatter;

    @Autowired
    public TemplateDataMapper(ObjectMapper mapper,
                              DocmosisBasePdfConfig docmosisBasePdfConfig) {
        this.mapper = mapper;
        this.docmosisBasePdfConfig = docmosisBasePdfConfig;
        ccdFormatter = DateTimeFormatter.ofPattern(CCD_DATE_FORMAT);
        letterFormatter = DateTimeFormatter.ofPattern(LETTER_DATE_FORMAT);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> map(Map<String, Object> placeholders) {
        // Get case data
        Map<String, Object> data = (Map<String, Object>) ((Map) placeholders.get(CASE_DETAILS)).get(CASE_DATA);

        // Setup Formatted D8 Marriage Date if exists
        if (Objects.nonNull(data.get(D8_MARRIAGE_DATE_KEY))) {
            data.put(D8_MARRIAGE_DATE_KEY, formatDateFromCCD((String) data.get(D8_MARRIAGE_DATE_KEY)));
        }

        // Setup Formatted DN Approval Date if exists
        if (Objects.nonNull(data.get(DN_APPROVAL_DATE_KEY))) {
            data.put(DN_APPROVAL_DATE_KEY, formatDateFromCCD((String) data.get(DN_APPROVAL_DATE_KEY)));
        }

        // Setup Formatted DN Granted Date if exists
        if (Objects.nonNull(data.get(DN_GRANTED_DATE_KEY))) {
            String dnGrantedDateString = (String) data.get(DN_GRANTED_DATE_KEY);
            data.put(DN_GRANTED_DATE_KEY, formatDateFromCCD(dnGrantedDateString));
            data.put(DA_APPLICABLE_DATE_KEY, getDaApplicationDate(dnGrantedDateString));
        }

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

            data.put(COURT_HEARING_DATE_KEY,
                formatDateFromCCD((String) latestCourtHearing.getValue().get(COURT_HEARING_DATE_KEY)));
            data.put(COURT_HEARING_TIME_KEY, latestCourtHearing.getValue().get(COURT_HEARING_TIME_KEY));
        }

        // Setup hardcoded service centre court contact details
        data.put(COURT_CONTACT_KEY, SERVICE_CENTRE_COURT_CONTACT_DETAILS);

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
            LocalDate ccdDate = LocalDate.parse(ccdDateString, ccdFormatter);
            return ccdDate.format(letterFormatter);
        } catch (Exception e) {
            throw new PDFGenerationException("Unable to format CCD Date Type field", e);
        }
    }

    private String getDaApplicationDate(String dnGrantedDateString) {
        try {
            LocalDate dnGrantedDate = LocalDate.parse(dnGrantedDateString, ccdFormatter);

            // Decree Absolute application date is granted date plus six weeks and one day
            dnGrantedDate = dnGrantedDate.atStartOfDay().plusWeeks(SIX_WEEKS).plusDays(ONE_DAY).toLocalDate();

            return dnGrantedDate.format(letterFormatter);
        } catch (Exception e) {
            throw new PDFGenerationException("Unable to parse Decree Absolute application date", e);
        }
    }
}
