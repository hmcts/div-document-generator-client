package uk.gov.hmcts.reform.divorce.documentgenerator.util;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.util.HtmlUtils;

import java.util.Map;

@SuppressWarnings("squid:S1118")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HtmlFieldFormatter {
    private static final Map<String, String> CONVERSION_MAP = ImmutableMap.of("\n", "<br />");

    @SuppressWarnings("unchecked")
    public static <T> T format(T fieldData) {
        if (fieldData instanceof String) {
            String data = (String) fieldData;

            data = HtmlUtils.htmlEscape(data);

            for (Map.Entry<String, String> entry : CONVERSION_MAP.entrySet()) {
                data = data.replaceAll(entry.getKey(), entry.getValue());
            }

            return (T) data;
        }

        return fieldData;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> format(Map<String, Object> fieldData) {
        if (fieldData == null || fieldData.isEmpty()) {
            return fieldData;
        }

        fieldData.entrySet()
                .forEach(entry -> {
                    if (entry.getValue() instanceof Map) {
                        entry.setValue(format((Map)entry.getValue()));
                    } else {
                        entry.setValue(format(entry.getValue()));
                    }
                }
            );

        return fieldData;
    }
}