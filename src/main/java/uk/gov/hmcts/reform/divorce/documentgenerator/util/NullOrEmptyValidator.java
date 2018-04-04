package uk.gov.hmcts.reform.divorce.documentgenerator.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings("squid:S1118")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NullOrEmptyValidator {

    public static void requireNonEmpty(byte[] array) {
        if (ArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException("Array is empty");
        }
    }

    public static void requireNonBlank(String text) {
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException("Text is blank");
        }
    }
}
