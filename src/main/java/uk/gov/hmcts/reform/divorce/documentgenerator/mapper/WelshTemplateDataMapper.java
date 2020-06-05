package uk.gov.hmcts.reform.divorce.documentgenerator.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplateConfig;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.CcdCollectionMember;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.LocalDateToWelshStringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_ADULTERY_FOUND_OUT_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_BEHAVIOUR_MOST_RECENT_DATE_DN_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_COURT_HEARING_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.WELSH_CURRENT_DATE_KEY;
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
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.YES_VALUE;


@Component
public class WelshTemplateDataMapper {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TemplateConfig templateConfig;

    @Autowired
    private LocalDateToWelshStringConverter localDateToWelshStringConverter;

    public void updateWithWelshTranslatedData(Map<String, Object> placeholders) {

        Map<String, Object> data = (Map<String, Object>) ((Map) placeholders.get(CASE_DETAILS)).get(CASE_DATA);

        Optional.ofNullable(placeholders.get(ACCESS_CODE_KEY))
            .ifPresent(value -> data.put(ACCESS_CODE_KEY,value));
        Optional.ofNullable(placeholders.get(IS_DRAFT_KEY))
            .ifPresent(value -> data.put(IS_DRAFT_KEY, value));
        Optional.ofNullable(placeholders.get(FEATURE_TOGGLE_RESP_SOLCIITOR))
            .ifPresent(value -> data.put(FEATURE_TOGGLE_RESP_SOLCIITOR, value));
        Optional.ofNullable(placeholders.get(PETITION_ISSUE_FEE_KEY))
            .ifPresent(value -> data.put(PETITION_ISSUE_FEE_KEY, value));

        Map<String, Object> caseDetailsMap =
            Optional.ofNullable(placeholders.get(CASE_DETAILS)).map(Map.class::cast).orElse(Collections.emptyMap());
        Optional.ofNullable(caseDetailsMap.get(CASE_ID_KEY))
            .ifPresent(value -> data.put(CASE_ID_KEY, value));
        Optional.ofNullable(caseDetailsMap.get(LAST_MODIFIED_KEY))
            .ifPresent(value -> data.put(LAST_MODIFIED_KEY, value));

        Optional.ofNullable(data.get(LANGUAGE_PREFERENCE_WELSH_KEY))
            .map(String.class::cast)
            .filter(YES_VALUE::equalsIgnoreCase)
            .ifPresent(value -> translatedWelshData(data));
    }

    private void translatedWelshData(Map<String, Object> data) {

        Map<String, String> relationship =
                templateConfig.getTemplate().get(RELATION).get(WELSH);

        Optional.ofNullable(data.get(D8_DIVORCE_WHO_KEY)).ifPresent(
            divorceWho ->
                    data.put(WELSH_D8_DIVORCE_WHO_KEY, relationship.get(divorceWho)));

        setWelshEquivalentDates(data, D8_MARRIAGE_DATE_KEY, WELSH_D8_MARRIAGE_DATE_KEY);
        setWelshEquivalentDates(data, D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY, WELSH_D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY);
        setWelshEquivalentDates(data, D8_MENTAL_SEPARATION_DATE_KEY, WELSH_D8_MENTAL_SEPARATION_DATE_KEY);
        setWelshEquivalentDates(data, D8_PHYSICAL_SEPARATION_DATE_KEY, WELSH_D8_PHYSICAL_SEPARATION_DATE_KEY);
        setWelshEquivalentDates(data, D8_REASON_FOR_DIVORCE_SEPERATION_DATE_KEY, WELSH_D8_REASON_FOR_DIVORCE_SEPERATION_DATE_KEY);

        if (data.get(IS_DRAFT_KEY) == null) {
            Optional.ofNullable(data.get(PREVIOUS_ISSUE_DATE_KEY)).map(String.class::cast).ifPresent(
                date ->
                            data.put(WELSH_PREVIOUS_ISSUE_DATE_KEY,
                                    localDateToWelshStringConverter.convert(date)));

            String lastModifiedTime = (String)data.computeIfAbsent(LAST_MODIFIED_KEY,
                k -> LocalDateTime.now().toString());

            data.put(WELSH_LAST_MODIFIED_KEY,
                localDateToWelshStringConverter.convert((LocalDateTime.parse(lastModifiedTime).toLocalDate())));
        }

        if (Objects.nonNull(data.get(COURT_HEARING_JSON_KEY))) {
            List<Object> listOfCourtHearings =
                mapper.convertValue(data.get(COURT_HEARING_JSON_KEY), ArrayList.class);

            // Last element of the list is the most recent court hearing
            CcdCollectionMember<Map<String, Object>> latestCourtHearing =
                mapper.convertValue(listOfCourtHearings.get(listOfCourtHearings.size() - 1),
                    CcdCollectionMember.class);
            data.put(WELSH_COURT_HEARING_DATE_KEY, localDateToWelshStringConverter.convert(
                (String) latestCourtHearing.getValue().get(COURT_HEARING_DATE_KEY)));
        }
        data.put(WELSH_CURRENT_DATE_KEY,
            localDateToWelshStringConverter.convert((LocalDate.now())));

        String dateOfDocumentProduction = (String)data.computeIfAbsent(DATE_OF_DOCUMENT_PRODUCTION,
            k -> LocalDate.now().toString());

        data.put(WELSH_DATE_OF_DOCUMENT_PRODUCTION,
            localDateToWelshStringConverter.convert(dateOfDocumentProduction));

        setWelshEquivalentDates(data, DN_APPROVAL_DATE_KEY, WELSH_DN_APPROVAL_DATE_KEY);
        setWelshEquivalentDates(data, DECREE_NISI_GRANTED_DATE_KEY, WELSH_DECREE_NISI_GRANTED_DATE_KEY);
        setWelshEquivalentDates(data, DECREE_ABSOLUTE_GRANTED_DATE_KEY, WELSH_DECREE_ABSOLUTE_GRANTED_DATE_KEY);
        setWelshEquivalentDates(data, DECREE_ABSOLUTE_ELIGIBLE_FROM_DATE_KEY, WELSH_DECREE_ABSOLUTE_ELIGIBLE_FROM_DATE_KEY);
        setWelshEquivalentDates(data, BEHAVIOUR_MOST_RECENT_DATE_DN_KEY, WELSH_BEHAVIOUR_MOST_RECENT_DATE_DN_KEY);
        setWelshEquivalentDates(data, ADULTERY_FOUND_OUT_DATE_KEY, WELSH_ADULTERY_FOUND_OUT_DATE_KEY);
        setWelshEquivalentDates(data, DECREE_NISI_SUBMITTED_DATE_KEY, WELSH_DECREE_NISI_SUBMITTED_DATE_KEY);

        Optional<String> d8ReasonForDivorceBehaviourDetailsFlag =
            getLanguagePreferenceFlag(data, D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_TRANS_LANG);

        if (d8ReasonForDivorceBehaviourDetailsFlag.isPresent()) {
            if (d8ReasonForDivorceBehaviourDetailsFlag.get().equalsIgnoreCase(ENGLISH_VALUE)) {
                setWelshTranslatedData(data,D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_EN,
                    D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_CY,D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_TRANS,
                    D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS);
            } else {
                setWelshTranslatedData(data,D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_EN,
                    D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_CY,D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS,
                    D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_TRANS);
            }
        } else {
            setWelshTranslatedData(data,D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_EN,
                D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_CY,D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS,
                D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS);
        }

        Optional<String> d8ReasonForDivorceAdulteryDetailsFlag =
            getLanguagePreferenceFlag(data, D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_TRANS_LANG);

        if (d8ReasonForDivorceAdulteryDetailsFlag.isPresent()) {
            if (d8ReasonForDivorceAdulteryDetailsFlag.get().equalsIgnoreCase(ENGLISH_VALUE)) {
                setWelshTranslatedData(data,D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_EN,
                    D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_CY, D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_TRANS,
                    D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS);
            } else {
                setWelshTranslatedData(data,D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_EN,
                    D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_CY,D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS,
                    D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_TRANS);
            }
        } else {
            setWelshTranslatedData(data,D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_EN,
                D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_CY,D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS,
                D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS);
        }

        Optional<String> d8PetitionerNameChangedHowOtherDetailsFlag =
            getLanguagePreferenceFlag(data, D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_TRANS_LANG);

        if (d8PetitionerNameChangedHowOtherDetailsFlag.isPresent()) {
            if (d8PetitionerNameChangedHowOtherDetailsFlag.get().equalsIgnoreCase(ENGLISH_VALUE)) {
                setWelshTranslatedData(data,D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_EN,
                    D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_CY, D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_TRANS,
                    D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS);
            } else {
                setWelshTranslatedData(data,D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_EN,
                    D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_CY, D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS,
                    D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_TRANS);
            }
        } else {
            setWelshTranslatedData(data,D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_EN,
                D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_CY, D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS,
                D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS);
        }

        Optional<String> d8ReasonForDivorceAdulteryWhenDetailsFlag =
            getLanguagePreferenceFlag(data, D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_TRANS_LANG);

        if (d8ReasonForDivorceAdulteryWhenDetailsFlag.isPresent()) {
            if (d8ReasonForDivorceAdulteryWhenDetailsFlag.get().equalsIgnoreCase(ENGLISH_VALUE)) {
                setWelshTranslatedData(data, D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_EN,
                    D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_CY, D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_TRANS,
                    D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS);
            } else {
                setWelshTranslatedData(data, D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_EN,
                    D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_CY, D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS,
                    D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_TRANS);
            }
        } else {
            setWelshTranslatedData(data, D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_EN,
                D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_CY, D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS,
                D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS);
        }

        Optional<String> d8ReasonForDivorceAdulteryWhereDetailsFlag =
            getLanguagePreferenceFlag(data, D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_TRANS_LANG);

        if (d8ReasonForDivorceAdulteryWhereDetailsFlag.isPresent()) {
            if (d8ReasonForDivorceAdulteryWhereDetailsFlag.get().equalsIgnoreCase(ENGLISH_VALUE)) {
                setWelshTranslatedData(data, D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_EN,
                    D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_CY, D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_TRANS,
                    D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS);
            } else {
                setWelshTranslatedData(data, D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_EN,
                    D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_CY, D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS,
                    D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_TRANS);
            }
        } else {
            setWelshTranslatedData(data, D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_EN,
                D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_CY, D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS,
                D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS);
        }

        Optional<String> d8ReasonForDivorceAdultery2ndHandDetailsFlag =
            getLanguagePreferenceFlag(data, D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_TRANS_LANG);

        if (d8ReasonForDivorceAdultery2ndHandDetailsFlag.isPresent()) {
            if (d8ReasonForDivorceAdultery2ndHandDetailsFlag.get().equalsIgnoreCase(ENGLISH_VALUE)) {
                setWelshTranslatedData(data, D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_EN,
                    D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_CY, D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_TRANS,
                    D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS);
            } else {
                setWelshTranslatedData(data, D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_EN,
                    D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_CY, D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS,
                    D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_TRANS);
            }
        } else {
            setWelshTranslatedData(data, D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_EN,
                D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_CY, D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS,
                D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS);
        }

        Optional<String> d8ReasonForDivorceDesertionDetailsFlag =
            getLanguagePreferenceFlag(data, D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_TRANS_LANG);

        if (d8ReasonForDivorceDesertionDetailsFlag.isPresent()) {
            if (d8ReasonForDivorceDesertionDetailsFlag.get().equalsIgnoreCase(ENGLISH_VALUE)) {
                setWelshTranslatedData(data, D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_EN,
                    D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_CY, D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_TRANS,
                    D8_REASON_FOR_DIVORCE_DESERTION_DETAILS);
            } else {
                setWelshTranslatedData(data, D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_EN,
                    D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_CY, D8_REASON_FOR_DIVORCE_DESERTION_DETAILS,
                    D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_TRANS);
            }
        } else {
            setWelshTranslatedData(data, D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_EN,
                D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_CY, D8_REASON_FOR_DIVORCE_DESERTION_DETAILS,
                D8_REASON_FOR_DIVORCE_DESERTION_DETAILS);
        }

        Optional<String> d8LegalProceedingsDetailsFlag =
            getLanguagePreferenceFlag(data, D8_LEGAL_PROCEEDINGS_DETAILS_TRANS_LANG);

        if (d8LegalProceedingsDetailsFlag.isPresent()) {
            if (d8LegalProceedingsDetailsFlag.get().equalsIgnoreCase(ENGLISH_VALUE)) {
                setWelshTranslatedData(data, D8_LEGAL_PROCEEDINGS_DETAILS_EN,
                    D8_LEGAL_PROCEEDINGS_DETAILS_CY, D8_LEGAL_PROCEEDINGS_DETAILS_TRANS,
                    D8_LEGAL_PROCEEDINGS_DETAILS);
            } else {
                setWelshTranslatedData(data, D8_LEGAL_PROCEEDINGS_DETAILS_EN,
                    D8_LEGAL_PROCEEDINGS_DETAILS_CY, D8_LEGAL_PROCEEDINGS_DETAILS,
                    D8_LEGAL_PROCEEDINGS_DETAILS_TRANS);
            }
        } else {
            setWelshTranslatedData(data, D8_LEGAL_PROCEEDINGS_DETAILS_EN,
                D8_LEGAL_PROCEEDINGS_DETAILS_CY, D8_LEGAL_PROCEEDINGS_DETAILS,
                D8_LEGAL_PROCEEDINGS_DETAILS);
        }

        Optional<String> respJurisdictionDisagreeReasonFlag =
            getLanguagePreferenceFlag(data,RESP_JURISDICTION_DISAGREE_REASON_TRANS_LANG);

        if (respJurisdictionDisagreeReasonFlag.isPresent()) {
            if (respJurisdictionDisagreeReasonFlag.get().equalsIgnoreCase(ENGLISH_VALUE)) {
                setWelshTranslatedData(data, RESP_JURISDICTION_DISAGREE_REASON_EN,
                    RESP_JURISDICTION_DISAGREE_REASON_CY, RESP_JURISDICTION_DISAGREE_REASON_TRANS,
                    RESP_JURISDICTION_DISAGREE_REASON);
            } else {
                setWelshTranslatedData(data, RESP_JURISDICTION_DISAGREE_REASON_EN,
                    RESP_JURISDICTION_DISAGREE_REASON_CY, RESP_JURISDICTION_DISAGREE_REASON,
                    RESP_JURISDICTION_DISAGREE_REASON_TRANS);
            }
        } else {
            setWelshTranslatedData(data, RESP_JURISDICTION_DISAGREE_REASON_EN,
                RESP_JURISDICTION_DISAGREE_REASON_CY, RESP_JURISDICTION_DISAGREE_REASON,
                RESP_JURISDICTION_DISAGREE_REASON);
        }

        Optional<String> respLegalProceedingsDescriptionFlag =
            getLanguagePreferenceFlag(data,RESP_LEGAL_PROCEEDINGS_DESCRIPTION_TRANS_LANG);

        if (respLegalProceedingsDescriptionFlag.isPresent()) {
            if (respLegalProceedingsDescriptionFlag.get().equalsIgnoreCase(ENGLISH_VALUE)) {
                setWelshTranslatedData(data, RESP_LEGAL_PROCEEDINGS_DESCRIPTION_EN,
                    RESP_LEGAL_PROCEEDINGS_DESCRIPTION_CY, RESP_LEGAL_PROCEEDINGS_DESCRIPTION_TRANS,
                    RESP_LEGAL_PROCEEDINGS_DESCRIPTION);
            } else {
                setWelshTranslatedData(data, RESP_LEGAL_PROCEEDINGS_DESCRIPTION_EN,
                    RESP_LEGAL_PROCEEDINGS_DESCRIPTION_CY, RESP_LEGAL_PROCEEDINGS_DESCRIPTION,
                    RESP_LEGAL_PROCEEDINGS_DESCRIPTION_TRANS);
            }
        } else {
            setWelshTranslatedData(data, RESP_LEGAL_PROCEEDINGS_DESCRIPTION_EN,
                RESP_LEGAL_PROCEEDINGS_DESCRIPTION_CY, RESP_LEGAL_PROCEEDINGS_DESCRIPTION,
                RESP_LEGAL_PROCEEDINGS_DESCRIPTION);
        }

        Optional<String> respCostsReasonFlag =
            getLanguagePreferenceFlag(data,RESP_COSTS_REASON_TRANS_LANG);

        if (respCostsReasonFlag.isPresent()) {
            if (respCostsReasonFlag.get().equalsIgnoreCase(ENGLISH_VALUE)) {
                setWelshTranslatedData(data, RESP_COSTS_REASON_EN,
                    RESP_COSTS_REASON_CY, RESP_COSTS_REASON_TRANS,
                    RESP_COSTS_REASON);
            } else {
                setWelshTranslatedData(data, RESP_COSTS_REASON_EN,
                    RESP_COSTS_REASON_CY, RESP_COSTS_REASON,
                    RESP_COSTS_REASON_TRANS);
            }
        } else {
            setWelshTranslatedData(data, RESP_COSTS_REASON_EN,
                RESP_COSTS_REASON_CY, RESP_COSTS_REASON,
                RESP_COSTS_REASON);
        }

        Optional<String> coRespCostsReasonFlag =
            getLanguagePreferenceFlag(data,CO_RESP_COSTS_REASON_TRANS_LANG);

        if (coRespCostsReasonFlag.isPresent()) {
            if (coRespCostsReasonFlag.get().equalsIgnoreCase(ENGLISH_VALUE)) {
                setWelshTranslatedData(data, CO_RESP_COSTS_REASON_EN,
                    CO_RESP_COSTS_REASON_CY, CO_RESP_COSTS_REASON_TRANS,
                    CO_RESP_COSTS_REASON);
            } else {
                setWelshTranslatedData(data, CO_RESP_COSTS_REASON_EN,
                    CO_RESP_COSTS_REASON_CY, CO_RESP_COSTS_REASON,
                    CO_RESP_COSTS_REASON_TRANS);
            }
        } else {
            setWelshTranslatedData(data, CO_RESP_COSTS_REASON_EN,
                CO_RESP_COSTS_REASON_CY, CO_RESP_COSTS_REASON,
                CO_RESP_COSTS_REASON);
        }

        Optional<String> petitionChangedDetailsDNFlag =
            getLanguagePreferenceFlag(data,PETITION_CHANGED_DETAILS_DN_TRANS_LANG);

        if (petitionChangedDetailsDNFlag.isPresent()) {
            if (petitionChangedDetailsDNFlag.get().equalsIgnoreCase(ENGLISH_VALUE)) {
                setWelshTranslatedData(data, PETITION_CHANGED_DETAILS_DN_EN,
                    PETITION_CHANGED_DETAILS_DN_CY, PETITION_CHANGED_DETAILS_DN_TRANS,
                    PETITION_CHANGED_DETAILS_DN);
            } else {
                setWelshTranslatedData(data, PETITION_CHANGED_DETAILS_DN_EN,
                    PETITION_CHANGED_DETAILS_DN_CY, PETITION_CHANGED_DETAILS_DN,
                    PETITION_CHANGED_DETAILS_DN_TRANS);
            }
        } else {
            setWelshTranslatedData(data, PETITION_CHANGED_DETAILS_DN_EN,
                PETITION_CHANGED_DETAILS_DN_CY, PETITION_CHANGED_DETAILS_DN,
                PETITION_CHANGED_DETAILS_DN);
        }

        Optional<String> adulteryTimeLivedTogetherDetailsDNFlag =
            getLanguagePreferenceFlag(data, ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG);

        if (adulteryTimeLivedTogetherDetailsDNFlag.isPresent()) {
            if (adulteryTimeLivedTogetherDetailsDNFlag.get().equalsIgnoreCase(ENGLISH_VALUE)) {
                setWelshTranslatedData(data, ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_EN,
                    ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_CY, ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,
                    ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN);
            } else {
                setWelshTranslatedData(data, ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_EN,
                    ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_CY, ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN,
                    ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS);
            }
        } else {
            setWelshTranslatedData(data, ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_EN,
                ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_CY, ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN,
                ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN);
        }

        Optional<String> behaviourTimeLivedTogetherDetailsDNFlag =
            getLanguagePreferenceFlag(data, BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG);

        if (behaviourTimeLivedTogetherDetailsDNFlag.isPresent()) {
            if (behaviourTimeLivedTogetherDetailsDNFlag.get().equalsIgnoreCase(ENGLISH_VALUE)) {
                setWelshTranslatedData(data, BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_EN,
                    BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_CY, BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,
                    BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN);
            } else {
                setWelshTranslatedData(data, BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_EN,
                    BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_CY, BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN,
                    BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS);
            }
        } else {
            setWelshTranslatedData(data, BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_EN,
                BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_CY, BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN,
                BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN);
        }

        Optional<String> desertionTimeLivedTogetherDetailsDNFlag = getLanguagePreferenceFlag(data,
            DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG);

        if (desertionTimeLivedTogetherDetailsDNFlag.isPresent()) {
            if (desertionTimeLivedTogetherDetailsDNFlag.get().equalsIgnoreCase(ENGLISH_VALUE)) {
                setWelshTranslatedData(data, DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_EN,
                    DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_CY, DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS,
                    DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN);
            } else {
                setWelshTranslatedData(data, DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_EN,
                    DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_CY, DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN,
                    DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS);
            }
        } else {
            setWelshTranslatedData(data, DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_EN,
                DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_CY, DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN,
                DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN);
        }

        Optional<String> costsDifferentDetailsFlag = getLanguagePreferenceFlag(data,
            COSTS_DIFFERENT_DETAILS_TRANS_LANG);

        if (costsDifferentDetailsFlag.isPresent()) {
            if (costsDifferentDetailsFlag.get().equalsIgnoreCase(ENGLISH_VALUE)) {
                setWelshTranslatedData(data, COSTS_DIFFERENT_DETAILS_EN,
                    COSTS_DIFFERENT_DETAILS_CY, COSTS_DIFFERENT_DETAILS_TRANS,
                    COSTS_DIFFERENT_DETAILS);
            } else {
                setWelshTranslatedData(data, COSTS_DIFFERENT_DETAILS_EN,
                    COSTS_DIFFERENT_DETAILS_CY, COSTS_DIFFERENT_DETAILS,
                    COSTS_DIFFERENT_DETAILS_TRANS);
            }
        } else {
            setWelshTranslatedData(data, COSTS_DIFFERENT_DETAILS_EN,
                COSTS_DIFFERENT_DETAILS_CY, COSTS_DIFFERENT_DETAILS,
                COSTS_DIFFERENT_DETAILS);
        }
    }

    private void setWelshTranslatedData(Map<String, Object> data, String englishKey, String welshKey,
                                       String englishValue, String welshValue) {
        data.put(englishKey,data.get(englishValue));
        data.put(welshKey,data.get(welshValue));
    }

    private Optional<String> getLanguagePreferenceFlag(Map<String, Object> data, String languageFlag) {
        return Optional.ofNullable(data.get(languageFlag))
            .map(String.class::cast);
    }

    private void setWelshEquivalentDates(Map<String, Object> data, String original, String translated) {
        Optional.ofNullable(data.get(original)).map(String.class::cast).ifPresent(
            date -> data.put(translated, localDateToWelshStringConverter.convert(date)));
    }
}
