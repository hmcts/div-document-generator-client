package uk.gov.hmcts.reform.divorce.documentgenerator.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
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
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.ACCESS_CODE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.ADULTERY_FOUND_OUT_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_CY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_EN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.BEHAVIOUR_MOST_RECENT_DATE_DN_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_CY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_EN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CASE_DATA;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CASE_DETAILS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CASE_ID_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COSTS_DIFFERENT_DETAILS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COSTS_DIFFERENT_DETAILS_CY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COSTS_DIFFERENT_DETAILS_EN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COSTS_DIFFERENT_DETAILS_TRANS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COSTS_DIFFERENT_DETAILS_TRANS_LANG;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_JSON_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_TIME_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESP_COSTS_REASON;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESP_COSTS_REASON_CY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESP_COSTS_REASON_EN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESP_COSTS_REASON_TRANS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESP_COSTS_REASON_TRANS_LANG;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_DIVORCE_WHO_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_LEGAL_PROCEEDINGS_DETAILS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_LEGAL_PROCEEDINGS_DETAILS_CY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_LEGAL_PROCEEDINGS_DETAILS_EN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_LEGAL_PROCEEDINGS_DETAILS_TRANS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_LEGAL_PROCEEDINGS_DETAILS_TRANS_LANG;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_MARRIAGE_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_MENTAL_SEPARATION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_CY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_EN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_TRANS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_TRANS_LANG;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_PHYSICAL_SEPARATION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_CY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_EN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_TRANS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_TRANS_LANG;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_CY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_EN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_TRANS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_TRANS_LANG;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_CY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_EN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_TRANS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_TRANS_LANG;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_CY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_EN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_TRANS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_TRANS_LANG;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_CY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_EN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_TRANS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_TRANS_LANG;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_DESERTION_DETAILS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_CY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_EN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_TRANS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_TRANS_LANG;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.D8_REASON_FOR_DIVORCE_SEPERATION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DATE_OF_DOCUMENT_PRODUCTION;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_ABSOLUTE_ELIGIBLE_FROM_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_ABSOLUTE_GRANTED_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_GRANTED_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_SUBMITTED_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DESERTION_ASKED_TO_RESUME_DN_DETAILS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DESERTION_ASKED_TO_RESUME_DN_DETAILS_CY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DESERTION_ASKED_TO_RESUME_DN_DETAILS_EN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DESERTION_ASKED_TO_RESUME_DN_DETAILS_TRANS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DESERTION_ASKED_TO_RESUME_DN_DETAILS_TRANS_LANG;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_CY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_EN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_APPROVAL_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.ENGLISH_VALUE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.FEATURE_TOGGLE_RESP_SOLCIITOR;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.IS_DRAFT_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.LANGUAGE_PREFERENCE_WELSH_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.LAST_MODIFIED_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PETITION_CHANGED_DETAILS_DN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PETITION_CHANGED_DETAILS_DN_CY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PETITION_CHANGED_DETAILS_DN_EN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PETITION_CHANGED_DETAILS_DN_TRANS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PETITION_CHANGED_DETAILS_DN_TRANS_LANG;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PETITION_ISSUE_FEE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PREVIOUS_ISSUE_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESP_COSTS_REASON;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESP_COSTS_REASON_CY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESP_COSTS_REASON_EN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESP_COSTS_REASON_TRANS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESP_COSTS_REASON_TRANS_LANG;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESP_JURISDICTION_DISAGREE_REASON;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESP_JURISDICTION_DISAGREE_REASON_CY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESP_JURISDICTION_DISAGREE_REASON_EN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESP_JURISDICTION_DISAGREE_REASON_TRANS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESP_JURISDICTION_DISAGREE_REASON_TRANS_LANG;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESP_LEGAL_PROCEEDINGS_DESCRIPTION;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESP_LEGAL_PROCEEDINGS_DESCRIPTION_CY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESP_LEGAL_PROCEEDINGS_DESCRIPTION_EN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESP_LEGAL_PROCEEDINGS_DESCRIPTION_TRANS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESP_LEGAL_PROCEEDINGS_DESCRIPTION_TRANS_LANG;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN_CY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN_EN;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_ADULTERY_FOUND_OUT_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_BEHAVIOUR_MOST_RECENT_DATE_DN_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_COURT_HEARING_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_D8_DIVORCE_WHO_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_D8_MARRIAGE_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_D8_MENTAL_SEPARATION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_D8_PHYSICAL_SEPARATION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_D8_REASON_FOR_DIVORCE_SEPERATION_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_DATE_OF_DOCUMENT_PRODUCTION;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_DECREE_ABSOLUTE_ELIGIBLE_FROM_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_DECREE_ABSOLUTE_GRANTED_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_DECREE_NISI_GRANTED_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_DECREE_NISI_SUBMITTED_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_DN_APPROVAL_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_LAST_MODIFIED_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_PREVIOUS_ISSUE_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_VALUE;
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
    private String accessCode = "3333";
    private String caseIdKey = "2222";

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

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);

        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
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
        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);

        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
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
        expectedData.put(PETITION_ISSUE_FEE_KEY, "550");
        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);

        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            PETITION_ISSUE_FEE_KEY, "550"
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
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
        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void decreeAbsoluteGrantedDate() {
        when(localDateToWelshStringConverter.convert("2001-12-02")).thenReturn("2 Rhagfyr 2012");

        Map<String, Object> caseData = new HashMap<>();
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        caseData.put(DECREE_ABSOLUTE_GRANTED_DATE_KEY, "2001-12-02");

        expectedData.put(WELSH_DECREE_ABSOLUTE_GRANTED_DATE_KEY, "2 Rhagfyr 2012");

        expectedData.putAll(caseData);

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void welshDAEligibleFromDate() {
        when(localDateToWelshStringConverter.convert("2001-12-02")).thenReturn("2 Rhagfyr 2012");

        Map<String, Object> caseData = new HashMap<>();
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        caseData.put(DECREE_ABSOLUTE_ELIGIBLE_FROM_DATE_KEY, "2001-12-02");

        expectedData.put(WELSH_DECREE_ABSOLUTE_ELIGIBLE_FROM_DATE_KEY, "2 Rhagfyr 2012");

        expectedData.putAll(caseData);

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void behaviourMostRecentIncidentDateDN() {
        when(localDateToWelshStringConverter.convert("2001-12-02")).thenReturn("2 Rhagfyr 2012");

        Map<String, Object> caseData = new HashMap<>();
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        caseData.put(BEHAVIOUR_MOST_RECENT_DATE_DN_KEY, "2001-12-02");

        expectedData.put(WELSH_BEHAVIOUR_MOST_RECENT_DATE_DN_KEY, "2 Rhagfyr 2012");

        expectedData.putAll(caseData);

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void adulteryDateFoundOut() {
        when(localDateToWelshStringConverter.convert("2001-12-12")).thenReturn("12 Rhagfyr 2012");

        Map<String, Object> caseData = new HashMap<>();
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        caseData.put(ADULTERY_FOUND_OUT_DATE_KEY, "2001-12-12");

        expectedData.put(WELSH_ADULTERY_FOUND_OUT_DATE_KEY, "12 Rhagfyr 2012");

        expectedData.putAll(caseData);

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void dnApplicationSubmittedDate() {
        when(localDateToWelshStringConverter.convert("2017-03-01")).thenReturn("1 Mawrth 2017");

        Map<String, Object> caseData = new HashMap<>();
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        caseData.put(DECREE_NISI_SUBMITTED_DATE_KEY, "2017-03-01");

        expectedData.put(WELSH_DECREE_NISI_SUBMITTED_DATE_KEY, "1 Mawrth 2017");

        expectedData.putAll(caseData);

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8ReasonForDivorceBehaviourDetails_EN() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS,"gwerth prawf");
        caseData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_TRANS,"test value");
        caseData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_TRANS_LANG, ENGLISH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS,"gwerth prawf");
        expectedData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_TRANS,"test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_EN, "test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8ReasonForDivorceBehaviourDetails_CY() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS,"test value");
        caseData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_TRANS,"gwerth prawf");
        caseData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_TRANS_LANG, WELSH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS,"test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_EN, "test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8ReasonForDivorceBehaviourDetails_NONE() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS,"test value");
        caseData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_TRANS,"gwerth prawf");
        caseData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_TRANS_LANG,"");
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS,"test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_EN, "test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8ReasonForDivorceAdulteryDetails_EN() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS,"gwerth prawf");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_TRANS,"test value");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_TRANS_LANG, ENGLISH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS,"gwerth prawf");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_TRANS,"test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_EN, "test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8ReasonForDivorceAdulteryDetails_CY() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS,"test value");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_TRANS,"gwerth prawf");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_TRANS_LANG, WELSH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS,"test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_EN, "test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8ReasonForDivorceAdulteryDetails_NONE() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS,"test value");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_TRANS,"gwerth prawf");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_TRANS_LANG,"");
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS,"test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_EN, "test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8PetitionerNameChangedHowOtherDetails_EN() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS,"gwerth prawf");
        caseData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_TRANS,"test value");
        caseData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_TRANS_LANG, ENGLISH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS,"gwerth prawf");
        expectedData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_TRANS,"test value");
        expectedData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_EN, "test value");
        expectedData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8PetitionerNameChangedHowOtherDetails_CY() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS,"test value");
        caseData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_TRANS,"gwerth prawf");
        caseData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_TRANS_LANG, WELSH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS,"test value");
        expectedData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_EN, "test value");
        expectedData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8PetitionerNameChangedHowOtherDetails_NONE() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS,"test value");
        caseData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_TRANS,"gwerth prawf");
        caseData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_TRANS_LANG,"");
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS,"test value");
        expectedData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_EN, "test value");
        expectedData.put(D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8ReasonForDivorceAdulteryWhenDetails_EN() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS,"gwerth prawf");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_TRANS,"test value");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_TRANS_LANG, ENGLISH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS,"gwerth prawf");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_TRANS,"test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_EN, "test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8ReasonForDivorceAdulteryWhenDetails_CY() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS,"test value");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_TRANS,"gwerth prawf");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_TRANS_LANG, WELSH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS,"test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_EN, "test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8ReasonForDivorceAdulteryWhenDetails_NONE() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS,"test value");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_TRANS,"gwerth prawf");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_TRANS_LANG,"");
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS,"test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_EN, "test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8ReasonForDivorceAdulteryWhereDetails_EN() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS,"gwerth prawf");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_TRANS,"test value");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_TRANS_LANG, ENGLISH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS,"gwerth prawf");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_TRANS,"test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_EN, "test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8ReasonForDivorceAdulteryWhereDetails_CY() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS,"test value");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_TRANS,"gwerth prawf");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_TRANS_LANG, WELSH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS,"test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_EN, "test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8ReasonForDivorceAdulteryWhereDetails_NONE() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS,"test value");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_TRANS,"gwerth prawf");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_TRANS_LANG,"");
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS,"test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_EN, "test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    //
    @Test
    public void d8ReasonForDivorceAdultery2ndHandDetails_EN() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS,"gwerth prawf");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_TRANS,"test value");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_TRANS_LANG, ENGLISH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS,"gwerth prawf");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_TRANS,"test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_EN, "test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8ReasonForDivorceAdultery2ndHandDetails_CY() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS,"test value");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_TRANS,"gwerth prawf");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_TRANS_LANG, WELSH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS,"test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_EN, "test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8ReasonForDivorceAdultery2ndHandDetails_NONE() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS,"test value");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_TRANS,"gwerth prawf");
        caseData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_TRANS_LANG,"");
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS,"test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_EN, "test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8ReasonForDivorceDesertionDetails_EN() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS,"gwerth prawf");
        caseData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_TRANS,"test value");
        caseData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_TRANS_LANG, ENGLISH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS,"gwerth prawf");
        expectedData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_TRANS,"test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_EN, "test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8ReasonForDivorceDesertionDetails_CY() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS,"test value");
        caseData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_TRANS,"gwerth prawf");
        caseData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_TRANS_LANG, WELSH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS,"test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_EN, "test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8ReasonForDivorceDesertionDetails_NONE() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS,"test value");
        caseData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_TRANS,"gwerth prawf");
        caseData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_TRANS_LANG,"");
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS,"test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_EN, "test value");
        expectedData.put(D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8LegalProceedingsDetails_EN() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_LEGAL_PROCEEDINGS_DETAILS,"gwerth prawf");
        caseData.put(D8_LEGAL_PROCEEDINGS_DETAILS_TRANS,"test value");
        caseData.put(D8_LEGAL_PROCEEDINGS_DETAILS_TRANS_LANG, ENGLISH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_LEGAL_PROCEEDINGS_DETAILS,"gwerth prawf");
        expectedData.put(D8_LEGAL_PROCEEDINGS_DETAILS_TRANS,"test value");
        expectedData.put(D8_LEGAL_PROCEEDINGS_DETAILS_EN, "test value");
        expectedData.put(D8_LEGAL_PROCEEDINGS_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8LegalProceedingsDetails_CY() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_LEGAL_PROCEEDINGS_DETAILS,"test value");
        caseData.put(D8_LEGAL_PROCEEDINGS_DETAILS_TRANS,"gwerth prawf");
        caseData.put(D8_LEGAL_PROCEEDINGS_DETAILS_TRANS_LANG, WELSH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_LEGAL_PROCEEDINGS_DETAILS,"test value");
        expectedData.put(D8_LEGAL_PROCEEDINGS_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(D8_LEGAL_PROCEEDINGS_DETAILS_EN, "test value");
        expectedData.put(D8_LEGAL_PROCEEDINGS_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void d8LegalProceedingsDetails_NONE() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(D8_LEGAL_PROCEEDINGS_DETAILS,"test value");
        caseData.put(D8_LEGAL_PROCEEDINGS_DETAILS_TRANS,"gwerth prawf");
        caseData.put(D8_LEGAL_PROCEEDINGS_DETAILS_TRANS_LANG,"");
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(D8_LEGAL_PROCEEDINGS_DETAILS,"test value");
        expectedData.put(D8_LEGAL_PROCEEDINGS_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(D8_LEGAL_PROCEEDINGS_DETAILS_EN, "test value");
        expectedData.put(D8_LEGAL_PROCEEDINGS_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void despJurisdictionDisagreeReason_EN() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(RESP_JURISDICTION_DISAGREE_REASON,"gwerth prawf");
        caseData.put(RESP_JURISDICTION_DISAGREE_REASON_TRANS,"test value");
        caseData.put(RESP_JURISDICTION_DISAGREE_REASON_TRANS_LANG, ENGLISH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(RESP_JURISDICTION_DISAGREE_REASON,"gwerth prawf");
        expectedData.put(RESP_JURISDICTION_DISAGREE_REASON_TRANS,"test value");
        expectedData.put(RESP_JURISDICTION_DISAGREE_REASON_EN, "test value");
        expectedData.put(RESP_JURISDICTION_DISAGREE_REASON_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void respJurisdictionDisagreeReason_CY() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(RESP_JURISDICTION_DISAGREE_REASON,"test value");
        caseData.put(RESP_JURISDICTION_DISAGREE_REASON_TRANS,"gwerth prawf");
        caseData.put(RESP_JURISDICTION_DISAGREE_REASON_TRANS_LANG, WELSH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(RESP_JURISDICTION_DISAGREE_REASON,"test value");
        expectedData.put(RESP_JURISDICTION_DISAGREE_REASON_TRANS,"gwerth prawf");
        expectedData.put(RESP_JURISDICTION_DISAGREE_REASON_EN, "test value");
        expectedData.put(RESP_JURISDICTION_DISAGREE_REASON_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void respJurisdictionDisagreeReason_NONE() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(RESP_JURISDICTION_DISAGREE_REASON,"test value");
        caseData.put(RESP_JURISDICTION_DISAGREE_REASON_TRANS,"gwerth prawf");
        caseData.put(RESP_JURISDICTION_DISAGREE_REASON_TRANS_LANG,"");
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(RESP_JURISDICTION_DISAGREE_REASON,"test value");
        expectedData.put(RESP_JURISDICTION_DISAGREE_REASON_TRANS,"gwerth prawf");
        expectedData.put(RESP_JURISDICTION_DISAGREE_REASON_EN, "test value");
        expectedData.put(RESP_JURISDICTION_DISAGREE_REASON_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void respLegalProceedingsDescription_EN() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION,"gwerth prawf");
        caseData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION_TRANS,"test value");
        caseData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION_TRANS_LANG, ENGLISH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION,"gwerth prawf");
        expectedData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION_TRANS,"test value");
        expectedData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION_EN, "test value");
        expectedData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void respLegalProceedingsDescription_CY() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION,"test value");
        caseData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION_TRANS,"gwerth prawf");
        caseData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION_TRANS_LANG, WELSH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION,"test value");
        expectedData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION_TRANS,"gwerth prawf");
        expectedData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION_EN, "test value");
        expectedData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void respLegalProceedingsDescription_NONE() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION,"test value");
        caseData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION_TRANS,"gwerth prawf");
        caseData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION_TRANS_LANG,"");
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION,"test value");
        expectedData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION_TRANS,"gwerth prawf");
        expectedData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION_EN, "test value");
        expectedData.put(RESP_LEGAL_PROCEEDINGS_DESCRIPTION_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void respCostsReason_EN() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(RESP_COSTS_REASON,"gwerth prawf");
        caseData.put(RESP_COSTS_REASON_TRANS,"test value");
        caseData.put(RESP_COSTS_REASON_TRANS_LANG, ENGLISH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(RESP_COSTS_REASON,"gwerth prawf");
        expectedData.put(RESP_COSTS_REASON_TRANS,"test value");
        expectedData.put(RESP_COSTS_REASON_EN, "test value");
        expectedData.put(RESP_COSTS_REASON_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void respCostsReason_CY() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(RESP_COSTS_REASON,"test value");
        caseData.put(RESP_COSTS_REASON_TRANS,"gwerth prawf");
        caseData.put(RESP_COSTS_REASON_TRANS_LANG, WELSH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(RESP_COSTS_REASON,"test value");
        expectedData.put(RESP_COSTS_REASON_TRANS,"gwerth prawf");
        expectedData.put(RESP_COSTS_REASON_EN, "test value");
        expectedData.put(RESP_COSTS_REASON_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void respCostsReason_NONE() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(RESP_COSTS_REASON,"test value");
        caseData.put(RESP_COSTS_REASON_TRANS,"gwerth prawf");
        caseData.put(RESP_COSTS_REASON_TRANS_LANG,"");
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(RESP_COSTS_REASON,"test value");
        expectedData.put(RESP_COSTS_REASON_TRANS,"gwerth prawf");
        expectedData.put(RESP_COSTS_REASON_EN, "test value");
        expectedData.put(RESP_COSTS_REASON_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void coRespCostsReason_EN() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(CO_RESP_COSTS_REASON,"gwerth prawf");
        caseData.put(CO_RESP_COSTS_REASON_TRANS,"test value");
        caseData.put(CO_RESP_COSTS_REASON_TRANS_LANG, ENGLISH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(CO_RESP_COSTS_REASON,"gwerth prawf");
        expectedData.put(CO_RESP_COSTS_REASON_TRANS,"test value");
        expectedData.put(CO_RESP_COSTS_REASON_EN, "test value");
        expectedData.put(CO_RESP_COSTS_REASON_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void coRespCostsReason_CY() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(CO_RESP_COSTS_REASON,"test value");
        caseData.put(CO_RESP_COSTS_REASON_TRANS,"gwerth prawf");
        caseData.put(CO_RESP_COSTS_REASON_TRANS_LANG, WELSH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(CO_RESP_COSTS_REASON,"test value");
        expectedData.put(CO_RESP_COSTS_REASON_TRANS,"gwerth prawf");
        expectedData.put(CO_RESP_COSTS_REASON_EN, "test value");
        expectedData.put(CO_RESP_COSTS_REASON_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void coRespCostsReason_NONE() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(CO_RESP_COSTS_REASON,"test value");
        caseData.put(CO_RESP_COSTS_REASON_TRANS,"gwerth prawf");
        caseData.put(CO_RESP_COSTS_REASON_TRANS_LANG,"");
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(CO_RESP_COSTS_REASON,"test value");
        expectedData.put(CO_RESP_COSTS_REASON_TRANS,"gwerth prawf");
        expectedData.put(CO_RESP_COSTS_REASON_EN, "test value");
        expectedData.put(CO_RESP_COSTS_REASON_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }


    @Test
    public void petitionChangedDetailsDN_EN() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(PETITION_CHANGED_DETAILS_DN,"gwerth prawf");
        caseData.put(PETITION_CHANGED_DETAILS_DN_TRANS,"test value");
        caseData.put(PETITION_CHANGED_DETAILS_DN_TRANS_LANG, ENGLISH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(PETITION_CHANGED_DETAILS_DN,"gwerth prawf");
        expectedData.put(PETITION_CHANGED_DETAILS_DN_TRANS,"test value");
        expectedData.put(PETITION_CHANGED_DETAILS_DN_EN, "test value");
        expectedData.put(PETITION_CHANGED_DETAILS_DN_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void petitionChangedDetailsDN_CY() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(PETITION_CHANGED_DETAILS_DN,"test value");
        caseData.put(PETITION_CHANGED_DETAILS_DN_TRANS,"gwerth prawf");
        caseData.put(PETITION_CHANGED_DETAILS_DN_TRANS_LANG, WELSH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(PETITION_CHANGED_DETAILS_DN,"test value");
        expectedData.put(PETITION_CHANGED_DETAILS_DN_TRANS,"gwerth prawf");
        expectedData.put(PETITION_CHANGED_DETAILS_DN_EN, "test value");
        expectedData.put(PETITION_CHANGED_DETAILS_DN_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void petitionChangedDetailsDN_NONE() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(PETITION_CHANGED_DETAILS_DN,"test value");
        caseData.put(PETITION_CHANGED_DETAILS_DN_TRANS,"gwerth prawf");
        caseData.put(PETITION_CHANGED_DETAILS_DN_TRANS_LANG,"");
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(PETITION_CHANGED_DETAILS_DN,"test value");
        expectedData.put(PETITION_CHANGED_DETAILS_DN_TRANS,"gwerth prawf");
        expectedData.put(PETITION_CHANGED_DETAILS_DN_EN, "test value");
        expectedData.put(PETITION_CHANGED_DETAILS_DN_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void adulteryTimeLivedTogetherDetailsDN_EN() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN,"gwerth prawf");
        caseData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"test value");
        caseData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG, ENGLISH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN,"gwerth prawf");
        expectedData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"test value");
        expectedData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_EN, "test value");
        expectedData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void adulteryTimeLivedTogetherDetailsDN_CY() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN,"test value");
        caseData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"gwerth prawf");
        caseData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG, WELSH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN,"test value");
        expectedData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"gwerth prawf");
        expectedData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_EN, "test value");
        expectedData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void adulteryTimeLivedTogetherDetailsDN_NONE() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN,"test value");
        caseData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"gwerth prawf");
        caseData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG,"");
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN,"test value");
        expectedData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"gwerth prawf");
        expectedData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_EN, "test value");
        expectedData.put(ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }


    @Test
    public void behaviourTimeLivedTogetherDetailsDN_EN() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN,"gwerth prawf");
        caseData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"test value");
        caseData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG, ENGLISH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN,"gwerth prawf");
        expectedData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"test value");
        expectedData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_EN, "test value");
        expectedData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void behaviourTimeLivedTogetherDetailsDN_CY() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN,"test value");
        caseData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"gwerth prawf");
        caseData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG, WELSH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN,"test value");
        expectedData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"gwerth prawf");
        expectedData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_EN, "test value");
        expectedData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void behaviourTimeLivedTogetherDetailsDN_NONE() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN,"test value");
        caseData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"gwerth prawf");
        caseData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG,"");
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN,"test value");
        expectedData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"gwerth prawf");
        expectedData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_EN, "test value");
        expectedData.put(BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void desertionTimeLivedTogetherDetailsDN_EN() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN,"gwerth prawf");
        caseData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"test value");
        caseData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG, ENGLISH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN,"gwerth prawf");
        expectedData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"test value");
        expectedData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_EN, "test value");
        expectedData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void desertionTimeLivedTogetherDetailsDN_CY() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN,"test value");
        caseData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"gwerth prawf");
        caseData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG, WELSH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN,"test value");
        expectedData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"gwerth prawf");
        expectedData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_EN, "test value");
        expectedData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void desertionTimeLivedTogetherDetailsDN_NONE() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN,"test value");
        caseData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"gwerth prawf");
        caseData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG,"");
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN,"test value");
        expectedData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"gwerth prawf");
        expectedData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_EN, "test value");
        expectedData.put(DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }


    @Test
    public void costsDifferentDetails_EN() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(COSTS_DIFFERENT_DETAILS,"gwerth prawf");
        caseData.put(COSTS_DIFFERENT_DETAILS_TRANS,"test value");
        caseData.put(COSTS_DIFFERENT_DETAILS_TRANS_LANG, ENGLISH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(COSTS_DIFFERENT_DETAILS,"gwerth prawf");
        expectedData.put(COSTS_DIFFERENT_DETAILS_TRANS,"test value");
        expectedData.put(COSTS_DIFFERENT_DETAILS_EN, "test value");
        expectedData.put(COSTS_DIFFERENT_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void costsDifferentDetails_CY() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(COSTS_DIFFERENT_DETAILS,"test value");
        caseData.put(COSTS_DIFFERENT_DETAILS_TRANS,"gwerth prawf");
        caseData.put(COSTS_DIFFERENT_DETAILS_TRANS_LANG, WELSH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(COSTS_DIFFERENT_DETAILS,"test value");
        expectedData.put(COSTS_DIFFERENT_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(COSTS_DIFFERENT_DETAILS_EN, "test value");
        expectedData.put(COSTS_DIFFERENT_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void costsDifferentDetails_NONE() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(COSTS_DIFFERENT_DETAILS,"test value");
        caseData.put(COSTS_DIFFERENT_DETAILS_TRANS,"gwerth prawf");
        caseData.put(COSTS_DIFFERENT_DETAILS_TRANS_LANG,"");
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(COSTS_DIFFERENT_DETAILS,"test value");
        expectedData.put(COSTS_DIFFERENT_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(COSTS_DIFFERENT_DETAILS_EN, "test value");
        expectedData.put(COSTS_DIFFERENT_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void desertionAskedToResumeDNDetails_EN() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS,"gwerth prawf");
        caseData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS_TRANS,"test value");
        caseData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS_TRANS_LANG, ENGLISH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS,"gwerth prawf");
        expectedData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS_TRANS,"test value");
        expectedData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS_EN, "test value");
        expectedData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void desertionAskedToResumeDNDetails_CY() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS,"test value");
        caseData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS_TRANS,"gwerth prawf");
        caseData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS_TRANS_LANG, WELSH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS,"test value");
        expectedData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS_EN, "test value");
        expectedData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void desertionAskedToResumeDNDetails_NONE() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS,"test value");
        caseData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS_TRANS,"gwerth prawf");
        caseData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS_TRANS_LANG,"");
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS,"test value");
        expectedData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS_TRANS,"gwerth prawf");
        expectedData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS_EN, "test value");
        expectedData.put(DESERTION_ASKED_TO_RESUME_DN_DETAILS_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void separationTimeLivedTogetherDetailsDN_EN() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN,"gwerth prawf");
        caseData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"test value");
        caseData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG, ENGLISH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN,"gwerth prawf");
        expectedData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"test value");
        expectedData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN_EN, "test value");
        expectedData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void separationTimeLivedTogetherDetailsDN_CY() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN,"test value");
        caseData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"gwerth prawf");
        caseData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG, WELSH_VALUE);
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN,"test value");
        expectedData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"gwerth prawf");
        expectedData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN_EN, "test value");
        expectedData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void separationTimeLivedTogetherDetailsDN_NONE() {
        Map<String, Object> caseData = new HashMap<>();
        caseData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN,"test value");
        caseData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"gwerth prawf");
        caseData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG,"");
        caseData.put(LANGUAGE_PREFERENCE_WELSH_KEY, YES_VALUE);
        expectedData.putAll(caseData);
        expectedData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN,"test value");
        expectedData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,"gwerth prawf");
        expectedData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN_EN, "test value");
        expectedData.put(SEPARATION_TIME_LIVED_TOGETHER_DETAILS_DN_CY, "gwerth prawf");

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);
        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );
        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }

    @Test
    public void testAOSInvitationParameters() {
        Map<String, Object> caseData = new HashMap<>();
        expectedData.put(ACCESS_CODE_KEY, accessCode);
        expectedData.put(CASE_ID_KEY, caseIdKey);
        expectedData.put(FEATURE_TOGGLE_RESP_SOLCIITOR, true);

        ImmutableMap<String, Object> caseDetails = ImmutableMap.of(CASE_DATA, caseData, CASE_ID_KEY, caseIdKey);

        Map<String, Object> requestData = ImmutableMap.of(
            CASE_DETAILS, caseDetails,
            ACCESS_CODE_KEY, accessCode,
            FEATURE_TOGGLE_RESP_SOLCIITOR,true
        );

        welshTemplateDataMapper.updateWithWelshTranslatedData(requestData);
        MapDifference<String, Object> diff = Maps.difference(expectedData, caseData);
        caseData.keySet().removeAll(diff.entriesOnlyOnRight().keySet());
        assertEquals(expectedData, caseData);
    }
}
