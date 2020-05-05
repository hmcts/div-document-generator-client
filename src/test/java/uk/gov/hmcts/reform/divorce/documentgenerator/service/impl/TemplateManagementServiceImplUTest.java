package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.NullOrEmptyValidator;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.ResourceLoader;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@PowerMockIgnore("com.microsoft.applicationinsights.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({NullOrEmptyValidator.class, ResourceLoader.class})
public class TemplateManagementServiceImplUTest {
    private static final String TEMPLATE_NAME = "some_html";
    private static final String RESOURCE_PATH = "data/templates/some_html.html";

    @InjectMocks
    private TemplateManagementServiceImpl classUnderTest;

    //TODO - this is my very first task: write good tests to test existing behaviour, then see what happens when an unexisting template name is passed as a parameter. The refactoring depends on this answer (at least)

    @Before
    public void before() {
        mockStatic(NullOrEmptyValidator.class, ResourceLoader.class);
    }//TODO - ???

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

    @Test
    public void checkWhatHappensWhenTemplateNameDoesNotExist(){
        byte[] template = classUnderTest.getTemplateByName("unexisting-template");
        assertThat(template, is(nullValue()));
    }

    private String getResourcePath() {//TODO - ???
        try {
            return Whitebox.invokeMethod(classUnderTest,
                            "getResourcePath",
                                            TemplateManagementServiceImplUTest.TEMPLATE_NAME);//TODO - ?
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
