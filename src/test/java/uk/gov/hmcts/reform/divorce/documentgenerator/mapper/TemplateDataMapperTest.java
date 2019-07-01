package uk.gov.hmcts.reform.divorce.documentgenerator.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.DocmosisBasePdfConfig;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.CcdCollectionMember;
import uk.gov.hmcts.reform.divorce.documentgenerator.exception.PDFGenerationException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CARE_OF_PREFIX;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CASE_DATA;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CASE_DETAILS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_CONTACT_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_JSON_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_TIME_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_NAME_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_WISH_TO_NAME;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_MARRIAGE_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_ABSOLUTE_ELIGIBLE_FROM_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_GRANTED_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_APPROVAL_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.NEWLINE_DELIMITER;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SERVICE_CENTRE_COURT_CONTACT_DETAILS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SERVICE_CENTRE_COURT_NAME;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SERVICE_COURT_NAME_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SOLICITOR_IS_NAMED_CO_RESPONDENT;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SPACE_DELIMITER;

@RunWith(MockitoJUnitRunner.class)
public class TemplateDataMapperTest {

    // Test Values
    private static final String TEST_COURT_NAME = "Court Name";
    private static final String TEST_COURT_ADDRESS = "Placeholder Court";

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
        expectedData.put(SERVICE_COURT_NAME_KEY, SERVICE_CENTRE_COURT_NAME);
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

    @Test(expected = PDFGenerationException.class)
    public void givenInvalidD8MarriageDate_whenTemplateDataMapperIsCalled_throwPdfGenerationException() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_MARRIAGE_DATE_KEY, "invalidDate");

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        templateDataMapper.map(requestData);
    }

    @Test
    public void givenValidDnGrantedDate_whenTemplateDataMapperIsCalled_returnFormattedDnGrantedDate() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(DECREE_NISI_GRANTED_DATE_KEY, "2019-05-30");

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        String expectedFormattedDnGrantedDate = "30 May 2019";
        expectedData.put(DECREE_NISI_GRANTED_DATE_KEY, expectedFormattedDnGrantedDate);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test(expected = PDFGenerationException.class)
    public void givenInvalidDnGrantedDate_whenTemplateDataMapperIsCalled_throwPdfGenerationException() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(DECREE_NISI_GRANTED_DATE_KEY, "invalidDate");

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        templateDataMapper.map(requestData);
    }

    @Test
    public void givenValidDaEligibleFromDate_whenTemplateDataMapperIsCalled_returnFormattedDaEligibleFromDate() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(DECREE_ABSOLUTE_ELIGIBLE_FROM_DATE_KEY, "2019-05-30");

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        String expectedFormattedDaEligibleFromDate = "30 May 2019";
        expectedData.put(DECREE_ABSOLUTE_ELIGIBLE_FROM_DATE_KEY, expectedFormattedDaEligibleFromDate);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test(expected = PDFGenerationException.class)
    public void givenInvalidDaEligibleFromDate_whenTemplateDataMapperIsCalled_throwPdfGenerationException() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(DECREE_ABSOLUTE_ELIGIBLE_FROM_DATE_KEY, "invalidDate");

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        templateDataMapper.map(requestData);
    }

    @Test
    public void givenSolicitorWishToNameCoRespondentField_whenTemplateDataMapperIsCalled_returnDefaultWishToName() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(SOLICITOR_IS_NAMED_CO_RESPONDENT, "Yes");

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        expectedData.put(SOLICITOR_IS_NAMED_CO_RESPONDENT, "Yes");
        expectedData.put(CO_RESPONDENT_WISH_TO_NAME, "YES");

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test
    public void givenCourtHearingDateTime_whenTemplateDataMapperIsCalled_returnFormattedData() {
        Map<String, Object> courtHearingDateTime = new HashMap<>();
        courtHearingDateTime.put(COURT_HEARING_DATE_KEY, "2019-10-10");
        courtHearingDateTime.put(COURT_HEARING_TIME_KEY, "10:30");

        CcdCollectionMember<Map<String, Object>> courtHearingDateTimeCcdCollectionMember = new CcdCollectionMember<>();
        courtHearingDateTimeCcdCollectionMember.setValue(courtHearingDateTime);

        Map<String, Object> caseData = new HashMap<>();
        caseData.put(COURT_HEARING_JSON_KEY, Collections.singletonList(courtHearingDateTimeCcdCollectionMember));

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        expectedData.putAll(caseData);
        expectedData.put(COURT_HEARING_DATE_KEY, "10 October 2019");
        expectedData.put(COURT_HEARING_TIME_KEY, "10:30");

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test
    public void givenExistingCourtName_whenTemplateDataMapperIsCalled_returnFormattedData() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(COURT_NAME_KEY, TEST_COURT_NAME);

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        expectedData.putAll(caseData);
        expectedData.put(COURT_CONTACT_KEY, SERVICE_CENTRE_COURT_CONTACT_DETAILS);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test
    public void givenExistingCourtContactKey_whenTemplateDataMapperIsCalled_returnFormattedData() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(COURT_NAME_KEY, TEST_COURT_NAME);
        caseData.put(COURT_CONTACT_KEY, TEST_COURT_ADDRESS);

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        expectedData.putAll(caseData);

        String expectedContactAddress = StringUtils.join(
            CARE_OF_PREFIX, SPACE_DELIMITER, TEST_COURT_NAME, NEWLINE_DELIMITER, TEST_COURT_ADDRESS);
        expectedData.put(COURT_CONTACT_KEY, expectedContactAddress);

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
