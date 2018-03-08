package uk.gov.hmcts.reform.divorce.documentgenerator.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.gov.hmcts.reform.divorce.documentgenerator.exception.ErrorLoadingTemplateException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(NullOrEmptyValidator.class)
public class ResourceLoaderUTest {
    private static final String NON_EXISTENT_PATH = "somePath";
    private static final String EXISTING_PATH = "ResourceLoadTest.txt";
    private static final String DATA_IN_FILE = "Resource Load Test";

    @Before
    public void beforeTest() {
        mockStatic(NullOrEmptyValidator.class);
    }

    @Test
    public void testConstructorPrivate() throws Exception {
        Constructor<ResourceLoader> constructor = ResourceLoader.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test(expected = ErrorLoadingTemplateException.class)
    public void givenFileIsDoNotExists_whenLoadResource_thenThrowsErrorLoadingTemplateException() {
        ResourceLoader.loadResource(NON_EXISTENT_PATH);

        verifyStatic(NullOrEmptyValidator.class);
        NullOrEmptyValidator.requireNonBlank(NON_EXISTENT_PATH);
    }

    @Test
    public void givenFileExists_whenLoadResource_thenLoadFile() {
        byte[] data = ResourceLoader.loadResource(EXISTING_PATH);

        assertEquals(DATA_IN_FILE, new String(data, StandardCharsets.UTF_8));

        verifyStatic(NullOrEmptyValidator.class);
        NullOrEmptyValidator.requireNonBlank(EXISTING_PATH);
    }
}