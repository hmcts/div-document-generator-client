package uk.gov.hmcts.reform.divorce.documentgenerator.util;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class HtmlFieldFormatterUTest {

    @Test
    public void testConstructorPrivate() throws Exception {
        Constructor<HtmlFieldFormatter> constructor = HtmlFieldFormatter.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void givenFieldDataIsNull_whenFormat_thenReturnNull() {
        assertNull(HtmlFieldFormatter.format(null));
    }

    @Test
    public void givenFieldDataIsNotAString_whenFormat_thenReturnSameData() {
        final Object data = new Object();
        assertEquals(data, HtmlFieldFormatter.format(data));
    }

    @Test
    public void givenFieldDataIsAStringAndHasNoSpecialChars_whenFormat_thenReturnSameData() {
        final String data = "hi";
        assertEquals(data, HtmlFieldFormatter.format(data));
    }

    @Test
    public void givenFieldDataContainsSpecialCharacters_whenFormat_thenReturnFormattedData() {
        final String data = "<>&'";
        final String expected = "&lt;&gt;&amp;&#39;";

        final String actual = HtmlFieldFormatter.format(data);

        assertEquals(expected, actual);
    }

    @Test
    public void givenFieldDataContainsNewLine_whenFormat_thenReturnFormattedData() {
        final String data = "<>\n\n&\n'";
        final String expected = "&lt;&gt;<br /><br />&amp;<br />&#39;";

        String actual = HtmlFieldFormatter.format(data);

        assertEquals(expected, actual);
    }


    @Test
    public void givenMapOfDataIsNull_whenFormat_thenReturnNull() {
        assertEquals(null, HtmlFieldFormatter.format((Map<String, String>) null));
    }

    @Test
    public void givenMapOfDataIsEmpty_whenFormat_thenReturnEmptyMap() {
        assertTrue(HtmlFieldFormatter.format(Collections.emptyMap()).isEmpty());
    }

    @Test
    public void givenMapWithData_whenFormat_thenReturnUpdatedMap() {
        final String key1 = "key1";
        final String key2 = "key2";
        final String key3 = "key3";
        final String key4 = "key4";
        final String key5 = "key5";

        final Object value1 = new Object();
        final Object value2 = "<\n";
        final Object value3 = "Some text";
        final Object value4 = ">'";

        final Object formattedValue2 = "&lt;<br />";
        final Object formattedValue4 = "&gt;&#39;";

        final Map<String, Object> innerMapWithDataToFormat = new HashMap<>(ImmutableMap.of(
                key1, value1,
                key2, value2,
                key3, value3,
                key4, value4
        ));

        final Map<String, Object> mapWithDataToFormat = new HashMap<>(ImmutableMap.of(
                key1, value1,
                key2, value2,
                key3, value3,
                key4, value4,
                key5, innerMapWithDataToFormat
        ));

        final Map<String, Object> expectedInnerMap = ImmutableMap.of(
                key1, value1,
                key2, formattedValue2,
                key3, value3,
                key4, formattedValue4
        );

        final Map<String, Object> expected = ImmutableMap.of(
                key1, value1,
                key2, formattedValue2,
                key3, value3,
                key4, formattedValue4,
                key5, expectedInnerMap
        );

        Map<String, Object> actual = HtmlFieldFormatter.format(mapWithDataToFormat);

        assertEquals(expected, actual);
    }
}