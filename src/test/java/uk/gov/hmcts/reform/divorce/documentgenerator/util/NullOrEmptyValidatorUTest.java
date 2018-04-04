package uk.gov.hmcts.reform.divorce.documentgenerator.util;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

public class NullOrEmptyValidatorUTest {

    private static final String BLANK_STRING = " ";
    private static final String EMPTY_STRING = "";
    private static final String SOME_STRING = "Some String";

    @Test
    public void testConstructorPrivate() throws Exception {
        Constructor<NullOrEmptyValidator> constructor = NullOrEmptyValidator.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenArrayIsNull_whenRequireNonEmpty_thenThrowsIllegalArgumentException() {
        NullOrEmptyValidator.requireNonEmpty(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenArrayIsEmpty_whenRequireNonEmpty_thenThrowsIllegalArgumentException() {
        NullOrEmptyValidator.requireNonEmpty(ArrayUtils.EMPTY_BYTE_ARRAY);
    }

    @Test
    public void givenArrayIsNotEmptyOrNull_whenRequireNonEmpty_thenDoesNotThrowException() {
        try {
            NullOrEmptyValidator.requireNonEmpty(new byte[]{1});
        } catch (Exception e) {
            fail();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenTextIsNull_whenRequireNonBlank_thenThrowsIllegalArgumentException() {
        NullOrEmptyValidator.requireNonBlank(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenTextIsEmpty_whenRequireNonBlank_thenThrowsIllegalArgumentException() {
        NullOrEmptyValidator.requireNonBlank(EMPTY_STRING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenTextIsBlank_whenRequireNonBlank_thenThrowsIllegalArgumentException() {
        NullOrEmptyValidator.requireNonBlank(BLANK_STRING);
    }

    @Test
    public void givenTextIsNotBlank_whenRequireNonBlank_thenDoesNotThrowException() {
        try {
            NullOrEmptyValidator.requireNonBlank(SOME_STRING);
        } catch (Exception e) {
            fail();
        }
    }

}
