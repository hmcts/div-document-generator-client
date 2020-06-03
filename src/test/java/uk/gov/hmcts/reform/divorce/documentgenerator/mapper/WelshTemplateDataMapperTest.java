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
import uk.gov.hmcts.reform.divorce.documentgenerator.config.LanguagePreference;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplateConfig;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.CcdCollectionMember;
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
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_JSON_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_TIME_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_DIVORCE_WHO_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_MARRIAGE_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_MENTAL_SEPARATION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_PHYSICAL_SEPARATION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_SEPERATION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DATE_OF_DOCUMENT_PRODUCTION;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_GRANTED_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_APPROVAL_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.IS_DRAFT_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.LANGUAGE_PREFERENCE_WELSH_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.LAST_MODIFIED_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PREVIOUS_ISSUE_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_COURT_HEARING_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_CURRENT_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_D8_DIVORCE_WHO_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_D8_MARRIAGE_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_D8_MENTAL_SEPARATION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_D8_PHYSICAL_SEPARATION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_D8_REASON_FOR_DIVORCE_SEPERATION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_DATE_OF_DOCUMENT_PRODUCTION;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_DECREE_NISI_GRANTED_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_DN_APPROVAL_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_LAST_MODIFIED_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_PREVIOUS_ISSUE_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.YES_VALUE;

@RunWith(MockitoJUnitRunner.class)
public class WelshTemplateDataMapperTest {

    @Spy
    private ObjectMapper mapper;

    @Mock
    private TemplateConfig templateConfig;

    @Mock
    private LocalDateToWelshStringConverter localDateToWelshStringConverter;

    @InjectMocks
    private WelshTemplateDataMapper welshTemplateDataMapper;

    private Map<String, Object> expectedData;
    private final String lastModified = "2020-04-29T22:35:21.717";


    @Before
    public void setup() {
        // Setup base data that will always be added to the payload
        expectedData = new HashMap<>();
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
        caseData.put(DATE_OF_DOCUMENT_PRODUCTION, "2001-12-02");
        caseData.put(LAST_MODIFIED_KEY, lastModified);
        caseData.put(IS_DRAFT_KEY, true);
        expectedData.put(WELSH_D8_DIVORCE_WHO_KEY, "gwraig");
        expectedData.put(WELSH_D8_MARRIAGE_DATE_KEY, "2 Rhagfyr 2012");
        expectedData.put(WELSH_D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY, "1 Tachwedd 2015");
        expectedData.put(WELSH_D8_MENTAL_SEPARATION_DATE_KEY, "1 Mawrth 2017");
        expectedData.put(WELSH_D8_PHYSICAL_SEPARATION_DATE_KEY, "1 Mehefin 2018");
        expectedData.put(WELSH_D8_REASON_FOR_DIVORCE_SEPERATION_DATE_KEY, "1 Ebrill 2018");
        expectedData.put(WELSH_DATE_OF_DOCUMENT_PRODUCTION, "2 Rhagfyr 2012");
        expectedData.put(IS_DRAFT_KEY, true);

        expectedData.putAll(caseData);
        expectedData.put(D8_MARRIAGE_DATE_KEY, "2001-12-02");

        welshTemplateDataMapper.updateWithWelshTranslatedData(caseData);
        caseData.remove(WELSH_CURRENT_DATE_KEY);
        assertEquals(expectedData, caseData);
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
        caseData.put(PREVIOUS_ISSUE_DATE_KEY, "2001-12-02");
        caseData.put(DATE_OF_DOCUMENT_PRODUCTION, "2001-12-02");
        caseData.put(LAST_MODIFIED_KEY, lastModified);

        expectedData.put(WELSH_D8_DIVORCE_WHO_KEY, "gwraig");
        expectedData.put(WELSH_D8_MARRIAGE_DATE_KEY, "2 Rhagfyr 2012");
        expectedData.put(WELSH_D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY, "1 Tachwedd 2015");
        expectedData.put(WELSH_DATE_OF_DOCUMENT_PRODUCTION, "2 Rhagfyr 2012");
        expectedData.putAll(caseData);
        expectedData.put(D8_MARRIAGE_DATE_KEY, "2001-12-02");
        expectedData.put(LAST_MODIFIED_KEY, lastModified);
        expectedData.put(WELSH_LAST_MODIFIED_KEY, "29 Ebrill 2020");
        expectedData.put(WELSH_PREVIOUS_ISSUE_DATE_KEY, "2 Rhagfyr 2012");

        welshTemplateDataMapper.updateWithWelshTranslatedData(caseData);
        caseData.remove(WELSH_CURRENT_DATE_KEY);
        assertEquals(expectedData, caseData);
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
        caseData.put(DN_APPROVAL_DATE_KEY, "2015-11-01");
        caseData.put(DECREE_NISI_GRANTED_DATE_KEY, "2015-11-01");

        expectedData.put(WELSH_D8_DIVORCE_WHO_KEY, "gwraig");
        expectedData.put(WELSH_D8_MARRIAGE_DATE_KEY, "2 Rhagfyr 2012");
        expectedData.put(WELSH_D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY, "1 Tachwedd 2015");
        expectedData.put(WELSH_DN_APPROVAL_DATE_KEY, "1 Tachwedd 2015");
        expectedData.put(WELSH_DECREE_NISI_GRANTED_DATE_KEY, "1 Tachwedd 2015");
        expectedData.putAll(caseData);
        expectedData.put(D8_MARRIAGE_DATE_KEY, "02 December 2001");
        expectedData.put(LAST_MODIFIED_KEY, lastModified);
        expectedData.put(WELSH_LAST_MODIFIED_KEY, "29 Ebrill 2020");

        welshTemplateDataMapper.updateWithWelshTranslatedData(caseData);
        caseData.remove(WELSH_CURRENT_DATE_KEY);
        assertEquals("LAST_MODIFIED_KEY set to todays date " , LocalDate.now(),
            LocalDateTime.parse((String) caseData.get(LAST_MODIFIED_KEY)).toLocalDate());
    }

    @Test
    public void givenCourtHearingDateTime_whenTemplateDataMapperIsCalled_returnFormattedData() {
        when(localDateToWelshStringConverter.convert("2019-10-10")).thenReturn("10 Hydref 2019");
        when(localDateToWelshStringConverter.convert(LocalDate.parse("2020-04-29"))).thenReturn("29 Ebrill 2020");

        Map<String, Object> courtHearingDateTime = new HashMap<>();
        courtHearingDateTime.put(COURT_HEARING_DATE_KEY, "2019-10-10");
        courtHearingDateTime.put(COURT_HEARING_TIME_KEY, "10:30");

        CcdCollectionMember<Map<String, Object>> courtHearingDateTimeCcdCollectionMember = new CcdCollectionMember<>();
        courtHearingDateTimeCcdCollectionMember.setValue(courtHearingDateTime);

        Map<String, Object> caseData = new HashMap<>();
        caseData.put(COURT_HEARING_JSON_KEY, Collections.singletonList(courtHearingDateTimeCcdCollectionMember));
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        caseData.put(DATE_OF_DOCUMENT_PRODUCTION, "2019-10-10");
        caseData.put(COURT_HEARING_TIME_KEY, "10:30");
        caseData.put(COURT_HEARING_DATE_KEY, "2019-10-10");
        caseData.put(LAST_MODIFIED_KEY, lastModified);

        expectedData.putAll(caseData);
        expectedData.put(COURT_HEARING_DATE_KEY, "2019-10-10");
        expectedData.put(COURT_HEARING_TIME_KEY, "10:30");
        expectedData.put(WELSH_COURT_HEARING_DATE_KEY, "10 Hydref 2019");
        expectedData.put(LAST_MODIFIED_KEY, lastModified);
        expectedData.put(WELSH_LAST_MODIFIED_KEY, "29 Ebrill 2020");
        expectedData.put(WELSH_DATE_OF_DOCUMENT_PRODUCTION, "10 Hydref 2019");

        welshTemplateDataMapper.updateWithWelshTranslatedData(caseData);
        caseData.remove(WELSH_CURRENT_DATE_KEY);
        assertEquals(expectedData, caseData);
    }
}
