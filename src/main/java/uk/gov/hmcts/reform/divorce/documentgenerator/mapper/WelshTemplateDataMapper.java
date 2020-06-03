package uk.gov.hmcts.reform.divorce.documentgenerator.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.DocmosisBasePdfConfig;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplateConfig;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.CcdCollectionMember;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.LocalDateToWelshStringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import static uk.gov.hmcts.reform.divorce.documentgenerator.config.LanguagePreference.WELSH;
import static uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplateConfig.RELATION;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COURT_HEARING_JSON_KEY;
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


@Component
public class WelshTemplateDataMapper {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private DocmosisBasePdfConfig docmosisBasePdfConfig;

    @Autowired
    private TemplateConfig templateConfig;

    @Autowired
    private LocalDateToWelshStringConverter localDateToWelshStringConverter;

    public void updateWithWelshTranslatedData(Map<String, Object> caseData) {

        Consumer<Map<String, Object>> welshConsumer = data -> {
            Map<String, String> relationship =
                    templateConfig.getTemplate().get(RELATION).get(WELSH);

            Optional.ofNullable(data.get(D8_DIVORCE_WHO_KEY)).ifPresent(
                divorceWho ->
                        data.put(WELSH_D8_DIVORCE_WHO_KEY, relationship.get(divorceWho)));

            Optional.ofNullable(data.get(D8_MARRIAGE_DATE_KEY)).map(String.class::cast).ifPresent(
                date ->
                    data.put(WELSH_D8_MARRIAGE_DATE_KEY,
                                localDateToWelshStringConverter.convert(date)));

            Optional.ofNullable(data.get(D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY)).map(String.class::cast).ifPresent(
                date ->
                     data.put(WELSH_D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY,
                                localDateToWelshStringConverter.convert(date)));

            Optional.ofNullable(data.get(D8_MENTAL_SEPARATION_DATE_KEY)).map(String.class::cast).ifPresent(
                date ->
                     data.put(WELSH_D8_MENTAL_SEPARATION_DATE_KEY,
                                localDateToWelshStringConverter.convert(date)));

            Optional.ofNullable(data.get(D8_PHYSICAL_SEPARATION_DATE_KEY)).map(String.class::cast).ifPresent(
                date ->
                     data.put(WELSH_D8_PHYSICAL_SEPARATION_DATE_KEY,
                                localDateToWelshStringConverter.convert(date)));

            Optional.ofNullable(data.get(D8_REASON_FOR_DIVORCE_SEPERATION_DATE_KEY)).map(String.class::cast).ifPresent(
                date ->
                     data.put(WELSH_D8_REASON_FOR_DIVORCE_SEPERATION_DATE_KEY,
                                localDateToWelshStringConverter.convert(date)));

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

            Optional.ofNullable(data.get(DN_APPROVAL_DATE_KEY)).map(String.class::cast).ifPresent(
                date -> data.put(WELSH_DN_APPROVAL_DATE_KEY, localDateToWelshStringConverter.convert(date)));

            Optional.ofNullable(data.get(DECREE_NISI_GRANTED_DATE_KEY)).map(String.class::cast).ifPresent(
                date -> data.put(WELSH_DECREE_NISI_GRANTED_DATE_KEY, localDateToWelshStringConverter.convert(date)));
        };

        Optional.ofNullable(caseData.get(LANGUAGE_PREFERENCE_WELSH_KEY))
                .map(String.class::cast)
                .filter(YES_VALUE::equalsIgnoreCase)
                .ifPresent(data -> welshConsumer.accept(caseData));
    }
}
