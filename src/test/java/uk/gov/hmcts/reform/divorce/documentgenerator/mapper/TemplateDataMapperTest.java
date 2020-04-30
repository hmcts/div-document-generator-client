package uk.gov.hmcts.reform.divorce.documentgenerator.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.DocmosisBasePdfConfig;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.LanguagePreference;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplateConfig;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.CcdCollectionMember;
import uk.gov.hmcts.reform.divorce.documentgenerator.exception.PDFGenerationException;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.LocalDateToWelshStringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.divorce.documentgenerator.config.LanguagePreference.WELSH;
import static uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplateConfig.RELATION;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.ACCESS_CODE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.ADULTERY_FOUND_OUT_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.BEHAVIOUR_MOST_RECENT_DATE_DN_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CASE_DATA;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CASE_DETAILS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CASE_ID_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CLIAM_COSTS_FROM;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CLIAM_COSTS_FROM_CORESP;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CLIAM_COSTS_FROM_RESP;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CLIAM_COSTS_FROM_RESP_CORESP;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_CONTACT_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_JSON_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_TIME_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_WISH_TO_NAME;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_DIVORCE_WHO_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_MARRIAGE_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_MENTAL_SEPARATION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_PHYSICAL_SEPARATION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_SEPERATION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_ABSOLUTE_ELIGIBLE_FROM_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_ABSOLUTE_GRANTED_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_GRANTED_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_SUBMITTED_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_APPROVAL_DATE_KEY;
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
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.ISSUE_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.IS_DRAFT_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.LANGUAGE_PREFERENCE_WELSH_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.LAST_MODIFIED_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.REFUSAL_CLARIFICATION_REASONS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.REFUSAL_REJECTION_REASONS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SERVICE_CENTRE_COURT_CONTACT_DETAILS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SERVICE_CENTRE_COURT_NAME;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SERVICE_COURT_NAME_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SOLICITOR_IS_NAMED_CO_RESPONDENT;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_D8_DIVORCE_WHO_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_D8_MARRIAGE_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_D8_MENTAL_SEPARATION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_D8_PHYSICAL_SEPARATION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_D8_REASON_FOR_DIVORCE_SEPERATION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_LAST_MODIFIED_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.YES_VALUE;

@RunWith(MockitoJUnitRunner.class)
public class TemplateDataMapperTest {

    // Test Values
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
    private TemplateConfig templateConfig;

    @Mock
    private LocalDateToWelshStringConverter localDateToWelshStringConverter;

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
        expectedData.put(COURT_CONTACT_KEY, SERVICE_CENTRE_COURT_CONTACT_DETAILS);
        expectedData.put(docmosisBasePdfConfig.getDisplayTemplateKey(), docmosisBasePdfConfig.getDisplayTemplateVal());
        expectedData.put(docmosisBasePdfConfig.getFamilyCourtImgKey(), docmosisBasePdfConfig.getFamilyCourtImgVal());
        expectedData.put(docmosisBasePdfConfig.getHmctsImgKey(), docmosisBasePdfConfig.getHmctsImgVal());
        Map<String, String> welshRelationship = ImmutableMap.of("male", "gŵr",
                "female", "gwraig",
                "husband", "gŵr", "wife", "gwraig");

        Map<LanguagePreference, Map<String, String>> relation = ImmutableMap.of(WELSH, welshRelationship);

        Map<String, Map<LanguagePreference, Map<String, String>>> template =
                ImmutableMap.of(RELATION, relation);

        when(templateConfig.getTemplate()).thenReturn(template);
    }

    @Test
    public void testDraftMiniPetitionParameters() {
        when(localDateToWelshStringConverter.convert("2001-12-02")).thenReturn("2 Rhagfyr 2012");
        when(localDateToWelshStringConverter.convert("2015-11-01")).thenReturn("1 Tachwedd 2015");
        when(localDateToWelshStringConverter.convert("2017-03-01")).thenReturn("1 Mawrth 2017");
        when(localDateToWelshStringConverter.convert("2018-06-01")).thenReturn("1 Mehefin 2018");
        when(localDateToWelshStringConverter.convert("2018-04-01")).thenReturn("1 Ebrill 2018");
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        caseData.put(D8_MARRIAGE_DATE_KEY, "2001-12-02");
        caseData.put(D8_DIVORCE_WHO_KEY, "wife");
        caseData.put(D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY, "2015-11-01");
        caseData.put(D8_MENTAL_SEPARATION_DATE_KEY, "2017-03-01");
        caseData.put(D8_PHYSICAL_SEPARATION_DATE_KEY, "2018-06-01");
        caseData.put(D8_REASON_FOR_DIVORCE_SEPERATION_DATE_KEY, "2018-04-01");
        String accessCode = "3333";
        String caseIdKey = "2222";

        expectedData.put(ACCESS_CODE_KEY, accessCode);
        expectedData.put(CASE_ID_KEY, caseIdKey);
        expectedData.put(WELSH_D8_DIVORCE_WHO_KEY, "gwraig");
        expectedData.put(WELSH_D8_MARRIAGE_DATE_KEY, "2 Rhagfyr 2012");
        expectedData.put(WELSH_D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY, "1 Tachwedd 2015");
        expectedData.put(WELSH_D8_MENTAL_SEPARATION_DATE_KEY, "1 Mawrth 2017");
        expectedData.put(WELSH_D8_PHYSICAL_SEPARATION_DATE_KEY, "1 Mehefin 2018");
        expectedData.put(WELSH_D8_REASON_FOR_DIVORCE_SEPERATION_DATE_KEY, "1 Ebrill 2018");
        expectedData.put(IS_DRAFT_KEY, true);

        expectedData.putAll(caseData);
        expectedData.put(D8_MARRIAGE_DATE_KEY, "02 December 2001");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);

        Map<String, Object> requestData = ImmutableMap.of(
                CASE_DETAILS, caseDetails,
                ACCESS_CODE_KEY, accessCode,
                IS_DRAFT_KEY, true
        );

        Map<String, Object> actual = templateDataMapper.map(requestData);
        assertEquals(expectedData, actual);
    }

    @Test
    public void testMiniPetitionParameters() {
        when(localDateToWelshStringConverter.convert("2001-12-02")).thenReturn("2 Rhagfyr 2012");
        when(localDateToWelshStringConverter.convert("2015-11-01")).thenReturn("1 Tachwedd 2015");
        when(localDateToWelshStringConverter.convert(LocalDate.parse("2020-04-29"))).thenReturn("29 Ebrill 2020");

        Map<String, Object> caseData = new HashMap<>();
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        caseData.put(D8_MARRIAGE_DATE_KEY, "2001-12-02");
        caseData.put(D8_DIVORCE_WHO_KEY, "wife");
        caseData.put(D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY, "2015-11-01");
        String lastModified = "2020-04-29T22:35:21.717";
        String accessCode = "3333";
        String caseIdKey = "2222";
        expectedData.put(ACCESS_CODE_KEY, accessCode);
        expectedData.put(CASE_ID_KEY, caseIdKey);
        expectedData.put(WELSH_D8_DIVORCE_WHO_KEY, "gwraig");
        expectedData.put(WELSH_D8_MARRIAGE_DATE_KEY, "2 Rhagfyr 2012");
        expectedData.put(WELSH_D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY, "1 Tachwedd 2015");

        expectedData.putAll(caseData);
        expectedData.put(D8_MARRIAGE_DATE_KEY, "02 December 2001");
        expectedData.put(LAST_MODIFIED_KEY, lastModified);
        expectedData.put(WELSH_LAST_MODIFIED_KEY, "29 Ebrill 2020");



        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey,
                LAST_MODIFIED_KEY, lastModified);

        Map<String, Object> requestData = ImmutableMap.of(
                CASE_DETAILS, caseDetails,
                ACCESS_CODE_KEY, accessCode
        );

        Map<String, Object> actual = templateDataMapper.map(requestData);
        assertEquals(expectedData, actual);
    }

    @Test
    public void testMiniPetitionParameters_LastModifiedTme_NotPresent() {
        when(localDateToWelshStringConverter.convert("2001-12-02")).thenReturn("2 Rhagfyr 2012");
        when(localDateToWelshStringConverter.convert("2015-11-01")).thenReturn("1 Tachwedd 2015");

        Map<String, Object> caseData = new HashMap<>();
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        caseData.put(D8_MARRIAGE_DATE_KEY, "2001-12-02");
        caseData.put(D8_DIVORCE_WHO_KEY, "wife");
        caseData.put(D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY, "2015-11-01");
        String lastModified = "2020-04-29T22:35:21.717";
        String accessCode = "3333";
        String caseIdKey = "2222";
        expectedData.put(ACCESS_CODE_KEY, accessCode);
        expectedData.put(CASE_ID_KEY, caseIdKey);
        expectedData.put(WELSH_D8_DIVORCE_WHO_KEY, "gwraig");
        expectedData.put(WELSH_D8_MARRIAGE_DATE_KEY, "2 Rhagfyr 2012");
        expectedData.put(WELSH_D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY, "1 Tachwedd 2015");

        expectedData.putAll(caseData);
        expectedData.put(D8_MARRIAGE_DATE_KEY, "02 December 2001");
        expectedData.put(LAST_MODIFIED_KEY, lastModified);
        expectedData.put(WELSH_LAST_MODIFIED_KEY, "29 Ebrill 2020");



        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);

        Map<String, Object> requestData = ImmutableMap.of(
                CASE_DETAILS, caseDetails,
                ACCESS_CODE_KEY, accessCode
        );

        Map<String, Object> actual = templateDataMapper.map(requestData);
        assertEquals("LAST_MODIFIED_KEY set to todays date " , LocalDate.now(),
                LocalDateTime.parse((String)actual.get(LAST_MODIFIED_KEY)).toLocalDate());
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
    public void givenValidDaGrantedDate_whenTemplateDataMapperIsCalled_returnFormattedDnApprovalDate() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(DECREE_ABSOLUTE_GRANTED_DATE_KEY, "2019-05-30T10:30:30.125");

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        String expectedFormattedDaGrantedDate = "30 May 2019";
        expectedData.put(DECREE_ABSOLUTE_GRANTED_DATE_KEY, expectedFormattedDaGrantedDate);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test(expected = PDFGenerationException.class)
    public void givenInvalidDaGrantedDate_whenTemplateDataMapperIsCalled_throwPdfGenerationException() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(DECREE_ABSOLUTE_GRANTED_DATE_KEY, "invalidDate");

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        templateDataMapper.map(requestData);
    }

    @Test
    public void givenValidAdulteryFoundDate_whenTemplateDataMapperIsCalled_returnFormattedAdulteryFoundDate() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(ADULTERY_FOUND_OUT_DATE_KEY, "2019-05-30");

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        String expectedFormattedDaGrantedDate = "30 May 2019";
        expectedData.put(ADULTERY_FOUND_OUT_DATE_KEY, expectedFormattedDaGrantedDate);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test(expected = PDFGenerationException.class)
    public void givenInvalidAdulteryFoundDate_whenTemplateDataMapperIsCalled_throwPdfGenerationException() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(ADULTERY_FOUND_OUT_DATE_KEY, "invalidDate");

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        templateDataMapper.map(requestData);
    }

    @Test
    public void givenValidBehaviourMostRecentDate_whenTemplateDataMapperIsCalled_returnFormattedDate() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(BEHAVIOUR_MOST_RECENT_DATE_DN_KEY, "2019-05-30");

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        String expectedFormattedDaGrantedDate = "30 May 2019";
        expectedData.put(BEHAVIOUR_MOST_RECENT_DATE_DN_KEY, expectedFormattedDaGrantedDate);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test(expected = PDFGenerationException.class)
    public void givenInvalidBehaviourMostRecentDate_whenTemplateDataMapperIsCalled_throwPdfGenerationException() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(BEHAVIOUR_MOST_RECENT_DATE_DN_KEY, "invalidDate");

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
    public void givenExistingCourtContactKey_whenTemplateDataMapperIsCalled_returnFormattedData() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(COURT_CONTACT_KEY, TEST_COURT_ADDRESS);

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        expectedData.putAll(caseData);
        expectedData.put(COURT_CONTACT_KEY, TEST_COURT_ADDRESS);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test(expected = PDFGenerationException.class)
    public void givenInvalidDNSubmittedDate_whenTemplateDataMapperIsCalled_throwPdfGenerationException() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(DECREE_NISI_SUBMITTED_DATE_KEY, "invalidDate");

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        templateDataMapper.map(requestData);
    }

    @Test
    public void givenValidDNSubmittedDate_whenTemplateDataMapperIsCalled_returnFormattedDNSubmittedDate() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(DECREE_NISI_SUBMITTED_DATE_KEY, "2019-05-30");

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        String expectedFormattedDNSubmittedDate = "30 May 2019";
        expectedData.put(DECREE_NISI_SUBMITTED_DATE_KEY, expectedFormattedDNSubmittedDate);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test(expected = PDFGenerationException.class)
    public void givenInvalidIssueDate_whenTemplateDataMapperIsCalled_throwPdfGenerationException() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(ISSUE_DATE_KEY, "invalidDate");

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        templateDataMapper.map(requestData);
    }

    @Test
    public void givenValidIssueDate_whenTemplateDataMapperIsCalled_returnFormattedDNSubmittedDate() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(ISSUE_DATE_KEY, "2019-05-30");

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        String expectedFormattedDNSubmittedDate = "30 May 2019";
        expectedData.put(ISSUE_DATE_KEY, expectedFormattedDNSubmittedDate);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test
    public void givenExistingClaimCostsFromRespCoresp_whenTemplateDataMapperIsCalled_returnFormattedData() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(CLIAM_COSTS_FROM, new String[] {"respondent", "correspondent"});

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        expectedData.putAll(caseData);
        expectedData.put(CLIAM_COSTS_FROM_RESP_CORESP, YES_VALUE);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }


    @Test
    public void givenExistingClaimCostsFromResp_whenTemplateDataMapperIsCalled_returnFormattedData() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(CLIAM_COSTS_FROM, new String[] {"respondent"});

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        expectedData.putAll(caseData);
        expectedData.put(CLIAM_COSTS_FROM_RESP, YES_VALUE);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }


    @Test
    public void givenExistingClaimCostsFromCoResp_whenTemplateDataMapperIsCalled_returnFormattedData() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(CLIAM_COSTS_FROM, new String[] {"correspondent"});

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        expectedData.putAll(caseData);
        expectedData.put(CLIAM_COSTS_FROM_CORESP, YES_VALUE);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test
    public void givenDnRefusalClarificationReasons_whenNoFreeTextOrder_returnFormattedData() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(REFUSAL_CLARIFICATION_REASONS, new String[] {"noKnownElements"});

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        expectedData.putAll(caseData);
        expectedData.put(HAS_JURISDICTION_CLARIFICATION_KEY, false);
        expectedData.put(HAS_MARRIAGE_CERT_CLARIFICATION_KEY, false);
        expectedData.put(HAS_MARRIAGE_CERT_TRANSLATION_CLARIFICATION_KEY, false);
        expectedData.put(HAS_PREVIOUS_PROCEEDINGS_CLARIFICATION_KEY, false);
        expectedData.put(HAS_CASE_DETAILS_STATEMENT_CLARIFICATION_KEY, false);
        expectedData.put(HAS_FREE_TEXT_ORDER_KEY, false);
        expectedData.put(HAS_ONLY_FREE_TEXT_ORDER_KEY, false);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test
    public void givenDnRefusalClarificationReasons_whenOnlyFreeTextOrder_returnFormattedData() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(REFUSAL_CLARIFICATION_REASONS, new String[] {"other"});

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        expectedData.putAll(caseData);
        expectedData.put(HAS_JURISDICTION_CLARIFICATION_KEY, false);
        expectedData.put(HAS_MARRIAGE_CERT_CLARIFICATION_KEY, false);
        expectedData.put(HAS_MARRIAGE_CERT_TRANSLATION_CLARIFICATION_KEY, false);
        expectedData.put(HAS_PREVIOUS_PROCEEDINGS_CLARIFICATION_KEY, false);
        expectedData.put(HAS_CASE_DETAILS_STATEMENT_CLARIFICATION_KEY, false);
        expectedData.put(HAS_FREE_TEXT_ORDER_KEY, true);
        expectedData.put(HAS_ONLY_FREE_TEXT_ORDER_KEY, true);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test
    public void givenDnRefusalClarificationReasons_whenIncludesFreeTextOrder_returnFormattedData() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(REFUSAL_CLARIFICATION_REASONS, new String[] {
            "jurisdictionDetails",
            "marriageCertTranslation",
            "marriageCertificate",
            "previousProceedingDetails",
            "caseDetailsStatement",
            "other"
        });

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        expectedData.putAll(caseData);
        expectedData.put(HAS_JURISDICTION_CLARIFICATION_KEY, true);
        expectedData.put(HAS_MARRIAGE_CERT_CLARIFICATION_KEY, true);
        expectedData.put(HAS_MARRIAGE_CERT_TRANSLATION_CLARIFICATION_KEY, true);
        expectedData.put(HAS_PREVIOUS_PROCEEDINGS_CLARIFICATION_KEY, true);
        expectedData.put(HAS_CASE_DETAILS_STATEMENT_CLARIFICATION_KEY, true);
        expectedData.put(HAS_FREE_TEXT_ORDER_KEY, true);
        expectedData.put(HAS_ONLY_FREE_TEXT_ORDER_KEY, false);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test
    public void givenDnRefusalRejectionReasons_whenNoKnownElements_thenReturnFormattedData() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(REFUSAL_REJECTION_REASONS, new String[] {"noKnownElements"});

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        expectedData.putAll(caseData);
        expectedData.put(HAS_NO_JURISDICTION_REJECTION_KEY, false);
        expectedData.put(HAS_NO_CRITERIA_REJECTION_KEY, false);
        expectedData.put(HAS_INSUFFICIENT_DETAILS_REJECTION_KEY, false);
        expectedData.put(HAS_FREE_TEXT_ORDER_KEY, false);
        expectedData.put(HAS_ONLY_FREE_TEXT_ORDER_KEY, false);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test
    public void givenDnRefusalRejectionReasons_whenHasAllKnownElements_thenReturnFormattedData() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(REFUSAL_REJECTION_REASONS, new String[] {
            "noJurisdiction",
            "noCriteria",
            "insufficentDetails",
            "other"
        });

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        expectedData.putAll(caseData);
        expectedData.put(HAS_NO_JURISDICTION_REJECTION_KEY, true);
        expectedData.put(HAS_NO_CRITERIA_REJECTION_KEY, true);
        expectedData.put(HAS_INSUFFICIENT_DETAILS_REJECTION_KEY, true);
        expectedData.put(HAS_FREE_TEXT_ORDER_KEY, true);
        expectedData.put(HAS_ONLY_FREE_TEXT_ORDER_KEY, false);

        Map<String, Object> actual = templateDataMapper.map(requestData);

        assertEquals(expectedData, actual);
    }

    @Test
    public void givenDnRefusalRejectionReasons_whenHasOnlyOther_thenReturnFormattedData() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(REFUSAL_REJECTION_REASONS, new String[] {
            "other"
        });

        Map<String, Object> requestData = Collections.singletonMap(
            CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData)
        );

        expectedData.putAll(caseData);
        expectedData.put(HAS_NO_JURISDICTION_REJECTION_KEY, false);
        expectedData.put(HAS_NO_CRITERIA_REJECTION_KEY, false);
        expectedData.put(HAS_INSUFFICIENT_DETAILS_REJECTION_KEY, false);
        expectedData.put(HAS_FREE_TEXT_ORDER_KEY, true);
        expectedData.put(HAS_ONLY_FREE_TEXT_ORDER_KEY, true);

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
