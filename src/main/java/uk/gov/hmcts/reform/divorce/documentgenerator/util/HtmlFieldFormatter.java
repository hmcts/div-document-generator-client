package uk.gov.hmcts.reform.divorce.documentgenerator.util;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@SuppressWarnings("squid:S1118")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HtmlFieldFormatter {
    private static final Map<Character, String> HTML_CHAR_ESCAPE_MAP =
            ImmutableMap.of(
                    '<', "&lt;",
                    '>', "&gt;",
                    '&', "&amp;",
                    '"', "&quot;",
                    '\n', "<br />"
            );

    @SuppressWarnings("unchecked")
    public static <T> T format(T fieldData) {
        if (fieldData instanceof String) {
            String data = (String) fieldData;

            //this is a temp hack to handle FE converting < to &lt; for security reason
            data = data.replaceAll("&lt;", "<");

            return (T)escape(data);
        }

        return fieldData;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> format(Map<String, Object> fieldData) {
        if (fieldData == null || fieldData.isEmpty()) {
            return fieldData;
        }

        fieldData.entrySet().forEach(
            entry -> {
                if (entry.getValue() instanceof Map) {
                    entry.setValue(format((Map) entry.getValue()));
                } else {
                    entry.setValue(format(entry.getValue()));
                }
            });

        return fieldData;
    }

    private static String escape(String string) {
        StringBuilder builder = new StringBuilder();

        for (char charToEscape : string.toCharArray()) {
            String escapeString = HTML_CHAR_ESCAPE_MAP.get(charToEscape);

            if (escapeString != null) {
                builder.append(escapeString);
            } else if (charToEscape < 128) {
                builder.append(charToEscape);
            } else {
                builder.append("&#").append((int)charToEscape).append(";");
            }
        }

        return builder.toString();
    }
}
