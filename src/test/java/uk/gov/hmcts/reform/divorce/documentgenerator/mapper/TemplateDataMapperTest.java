package uk.gov.hmcts.reform.divorce.documentgenerator.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.DocmosisBasePdfConfig;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.CollectionMember;
import uk.gov.hmcts.reform.divorce.documentgenerator.exception.PDFGenerationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TemplateDataMapperTest {

    private static final String CASE_DATA = "case_data";
    private static final String CASE_DETAILS = "caseDetails";
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
    private static final String RESPONDENT_KEY = "respondent";
    private static final String SERVICE_CENTRE_COURT_CONTACT_DETAILS = "c\\o East Midlands Regional Divorce"
        + " Centre\nPO Box 10447\nNottingham<\nNG2 9QN\nEmail: contactdivorce@justice.gov.uk\nPhone: 0300 303"
        + " 0642 (from 8.30am to 5pm)";
    private static final String WHO_PAYS_COSTS_BOTH = "respondentAndCorespondent";
    private static final String WHO_PAYS_COSTS_CORESPONDENT = "corespondent";
    private static final String WHO_PAYS_COSTS_KEY = "whoPaysCosts";
    private static final String WHO_PAYS_COSTS_RESPONDENT = "respondent";
    private static final String YES_VALUE = "YES";

    // Docmosis Base Config Constants
    private static final String TEMPLATE_KEY = "templateKey";
    private static final String TEMPLATE_VAL = "templateVal";
    private static final String FAMILY_IMG_KEY = "familyImgKey";
    private static final String FAMILY_IMG_VAL = "familyImgVal";
    private static final String HMCTS_IMG_KEY = "hmctsImgKey";
    private static final String HMCTS_IMG_VAL = "hmctsImgVal";

    @Spy
    private ObjectMapper mapper;

    @Mock
    private DocmosisBasePdfConfig docmosisBasePdfConfig;

    @InjectMocks
    private TemplateDataMapper templateDataMapper;

    private Map<String, Object> expectedData;

    @Before
    public void setup() {
        // Mock docmosisBasePdfConfig
        mockDocmosisPdfBaseConfig();

        // Setup base data that will always be added to the payload
        expectedData = new HashMap<>();
        expectedData.put(COURT_CONTACT_KEY, SERVICE_CENTRE_COURT_CONTACT_DETAILS);
        expectedData.put(docmosisBasePdfConfig.getDisplayTemplateKey(), docmosisBasePdfConfig.getDisplayTemplateVal());
        expectedData.put(docmosisBasePdfConfig.getFamilyCourtImgKey(), docmosisBasePdfConfig.getFamilyCourtImgVal());
        expectedData.put(docmosisBasePdfConfig.getHmctsImgKey(), docmosisBasePdfConfig.getHmctsImgVal());
    }

    @Test
    public void givenEmptyRequest_whenTemplateDataMapperIsCalled_returnBaseData() {
        Map<String, Object> caseData = new HashMap<>();

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test
    public void givenValidD8MarriageDate_whenTemplateDataMapperIsCalled_returnFormattedD8MarriageDate() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_MARRIAGE_DATE_KEY, "2019-05-30");

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        String expectedFormattedD8MarriageDate = "30 May 2019";
        expectedData.put(D8_MARRIAGE_DATE_KEY, expectedFormattedD8MarriageDate);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test
    public void givenValidDnApprovalDate_whenTemplateDataMapperIsCalled_returnFormattedDnApprovalDate() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(DN_APPROVAL_DATE_KEY, "2019-05-30");

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        String expectedFormattedDNApprovalDate = "30 May 2019";
        expectedData.put(DN_APPROVAL_DATE_KEY, expectedFormattedDNApprovalDate);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test
    public void givenValidDnGrantedDate_whenTemplateDataMapperIsCalled_returnFormattedDnGrantedDateAndDaApplicableDate() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(DN_GRANTED_DATE_KEY, "2019-05-30");

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        String expectedFormattedDNGrantedDate = "30 May 2019";
        String expectedFormattedDaApplicableDate = "12 July 2019";
        expectedData.put(DN_GRANTED_DATE_KEY, expectedFormattedDNGrantedDate);
        expectedData.put(DA_APPLICABLE_DATE_KEY, expectedFormattedDaApplicableDate);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test(expected = PDFGenerationException.class)
    public void givenInvalidDnApprovalDate_whenTemplateDataMapperIsCalled_throwPdfGenerationException() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(DN_APPROVAL_DATE_KEY, "invalidDate");

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        templateDataMapper.map(requestData);
    }

    @Test
    public void givenClaimCostsForRespondent_whenTemplateDataMapperIsCalled_returnFormattedData() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(CLAIM_COSTS_JSON_KEY, YES_VALUE);
        caseData.put(CLAIM_COSTS_FROM_JSON_KEY, Collections.singletonList(RESPONDENT_KEY));

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        expectedData.putAll(caseData);
        expectedData.put(WHO_PAYS_COSTS_KEY, WHO_PAYS_COSTS_RESPONDENT);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test
    public void givenClaimCostsForCoRespondent_whenTemplateDataMapperIsCalled_returnFormattedData() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(CLAIM_COSTS_JSON_KEY, YES_VALUE);
        caseData.put(CLAIM_COSTS_FROM_JSON_KEY, Collections.singletonList(CORESPONDENT_KEY));

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        expectedData.putAll(caseData);
        expectedData.put(WHO_PAYS_COSTS_KEY, WHO_PAYS_COSTS_CORESPONDENT);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test
    public void givenClaimCostsForBoth_whenTemplateDataMapperIsCalled_returnFormattedData() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(CLAIM_COSTS_JSON_KEY, YES_VALUE);
        caseData.put(CLAIM_COSTS_FROM_JSON_KEY, Arrays.asList(RESPONDENT_KEY, CORESPONDENT_KEY));

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        expectedData.putAll(caseData);
        expectedData.put(WHO_PAYS_COSTS_KEY, WHO_PAYS_COSTS_BOTH);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test
    public void givenCourtHearingDateTime_whenTemplateDataMapperIsCalled_returnFormattedData() {
        Map<String, Object> courtHearingDateTime = new HashMap<>();
        courtHearingDateTime.put(COURT_HEARING_DATE_KEY, "2019-10-10");
        courtHearingDateTime.put(COURT_HEARING_TIME_KEY, "10:30");

        CollectionMember<Map<String, Object>> courtHearingDateTimeCollectionMember = new CollectionMember<>();
        courtHearingDateTimeCollectionMember.setValue(courtHearingDateTime);

        Map<String, Object> caseData = new HashMap<>();
        caseData.put(COURT_HEARING_JSON_KEY, Collections.singletonList(courtHearingDateTimeCollectionMember)
        );

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        expectedData.putAll(caseData);
        expectedData.put(COURT_HEARING_DATE_KEY, "10 October 2019");
        expectedData.put(COURT_HEARING_TIME_KEY, "10:30");

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    private void mockDocmosisPdfBaseConfig() {
        when(docmosisBasePdfConfig.getDisplayTemplateKey()).thenReturn(TEMPLATE_KEY);
        when(docmosisBasePdfConfig.getDisplayTemplateVal()).thenReturn(TEMPLATE_VAL);
        when(docmosisBasePdfConfig.getFamilyCourtImgKey()).thenReturn(FAMILY_IMG_KEY);
        when(docmosisBasePdfConfig.getFamilyCourtImgVal()).thenReturn(FAMILY_IMG_VAL);
        when(docmosisBasePdfConfig.getHmctsImgKey()).thenReturn(HMCTS_IMG_KEY);
        when(docmosisBasePdfConfig.getHmctsImgVal()).thenReturn(HMCTS_IMG_VAL);
    }
}
