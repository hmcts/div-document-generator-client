package uk.gov.hmcts.reform.divorce.documentgenerator.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplateConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static uk.gov.hmcts.reform.divorce.documentgenerator.config.LanguagePreference.WELSH;
import static uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplateConfig.MONTHS;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocalDateToWelshStringConverter {
    private final TemplateConfig templateConfig;

    public String convert(LocalDate dateToConvert) {
        return Optional.ofNullable(dateToConvert).map(date -> {
            int day = dateToConvert.getDayOfMonth();
            int year = dateToConvert.getYear();
            int month = dateToConvert.getMonth().getValue();
            return String.join(" ", Integer.toString(day),
                    templateConfig.getTemplate().get(MONTHS).get(WELSH).get(String.valueOf(month)),
                            Integer.toString(year));
        }).orElse(null);
    }

    public String convert(String localDateFormat) {
        if (localDateFormat.isEmpty()) {
            return localDateFormat;
        }
        return convert(LocalDate.parse(localDateFormat));
    }

    public String convertDateTime(String localDateTimeFormat) {
        return convert(LocalDateTime.parse(localDateTimeFormat).toLocalDate());
    }
}
