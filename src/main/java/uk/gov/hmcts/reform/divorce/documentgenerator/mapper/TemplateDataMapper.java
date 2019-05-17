package uk.gov.hmcts.reform.divorce.documentgenerator.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.DocmosisBasePdfConfig;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.CollectionMember;
import uk.gov.hmcts.reform.divorce.documentgenerator.exception.PDFGenerationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private static final String DN_APPROVAL_DATE_KEY = "DNApprovalDate";
    private static final String LETTER_DATE_FORMAT = "dd MMMM yyyy";
    private static final String RESPONDENT_KEY = "respondent";
    private static final String SERVICE_CENTRE_COURT_CONTACT_DETAILS = "c\\o East Midlands Regional Divorce"
        + " Centre\nPO Box 10447\nNottingham<\nNG2 9QN\nEmail: contactdivorce@justice.gov.uk\nPhone: 0300 303"
        + " 0642 (from 8.30am to 5pm)";
    private static final String WHO_PAYS_COSTS_BOTH = "respondentAndCorespondent";
    private static final String WHO_PAYS_COSTS_CORESPONDENT = "corespondent";
    private static final String WHO_PAYS_COSTS_KEY = "whoPaysCosts";
    private static final String WHO_PAYS_COSTS_RESPONDENT = "respondent";
    private static final String YES_VALUE = "YES";

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
        if (Objects.nonNull(ccdDateString)) {
            try {
                SimpleDateFormat ccdSdf = new SimpleDateFormat(CCD_DATE_FORMAT);
                Date ccdDate = ccdSdf.parse(ccdDateString);

                SimpleDateFormat letterSdf = new SimpleDateFormat(LETTER_DATE_FORMAT);
                ccdDateString = letterSdf.format(ccdDate);
            } catch (ParseException e) {
                throw new PDFGenerationException("Unable to parse court hearing date from CCD format", e);
            }
        }
        return ccdDateString;
    }
}
