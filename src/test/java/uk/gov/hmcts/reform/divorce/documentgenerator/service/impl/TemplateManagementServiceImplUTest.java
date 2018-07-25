package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.NullOrEmptyValidator;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.ResourceLoader;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NullOrEmptyValidator.class, ResourceLoader.class})
public class TemplateManagementServiceImplUTest {
    private static final String TEMPLATE_NAME = "some_html";
    private static final String RESOURCE_PATH = "data/templates/some_html.html";

    @InjectMocks
    private TemplateManagementServiceImpl classUnderTest;

    @Before
    public void before() {
        mockStatic(NullOrEmptyValidator.class, ResourceLoader.class);
    }

    @Test
    public void givenATemplateName_whenGetTemplateByName_thenReturnResource() {
        final byte[] data = {1};

        when(ResourceLoader.loadResource(RESOURCE_PATH)).thenReturn(data);

        assertEquals(data, classUnderTest.getTemplateByName(TEMPLATE_NAME));

        verifyStatic(ResourceLoader.class);
        ResourceLoader.loadResource(RESOURCE_PATH);
    }

    @Test
    public void givenATemplateName_whenGetResourcePath_thenReturnResourcePath() {

        assertEquals(RESOURCE_PATH, getResourcePath());

        verifyStatic(NullOrEmptyValidator.class);
        NullOrEmptyValidator.requireNonBlank(TEMPLATE_NAME);
    }

    private String getResourcePath() {
        try {
            return Whitebox.invokeMethod(classUnderTest, "getResourcePath", TemplateManagementServiceImplUTest.TEMPLATE_NAME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
