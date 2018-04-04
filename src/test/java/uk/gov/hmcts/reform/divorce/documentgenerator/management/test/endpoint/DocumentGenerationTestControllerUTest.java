package uk.gov.hmcts.reform.divorce.documentgenerator.management.test.endpoint;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import uk.gov.hmcts.reform.divorce.documentgenerator.service.DocumentManagementService;

@RunWith(SpringRunner.class)
@WebMvcTest(DocumentGenerationTestController.class)
public class DocumentGenerationTestControllerUTest {
    private static final String TEMPLATE_NAME = "templateName";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentManagementService documentManagementService;

    @Test
    public void whenGenerateDocument_thenReturnProperResponse() throws Exception {
        final byte[] data = {1};
        final String contextPath = "/test/generateDocument";
        final String someOtherKeyName = "someOtherKeyName";
        final String someOtherKeyValue = "someOtherKeyValue";

        final Map<String, Object> placeHolders = Collections.singletonMap(someOtherKeyName, someOtherKeyValue);
        final String templateName = "templateName";

        final Map<String, Object> allRequestParams = new HashMap<>(placeHolders);
        allRequestParams.put(TEMPLATE_NAME, templateName);

        when(documentManagementService.generateDocument(templateName, placeHolders)).thenReturn(data);

        mockMvc.perform(
                post(contextPath)
                        .param(someOtherKeyName, someOtherKeyValue)
                        .param(TEMPLATE_NAME, templateName))
                .andExpect(status().isOk())
                .andExpect(content().bytes(data))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }
}
