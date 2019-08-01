package uk.gov.hmcts.reform.divorce.documentgenerator.management.test.endpoint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.request.GenerateDocumentRequest;
import uk.gov.hmcts.reform.divorce.documentgenerator.functionaltest.ObjectMapperTestUtil;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.DocumentManagementService;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DocumentGenerationTestController.class)
public class DocumentGenerationTestControllerUTest {

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

        GenerateDocumentRequest generateDocumentRequest = new GenerateDocumentRequest(
            templateName,
            Collections.singletonMap(someOtherKeyName, someOtherKeyValue)
        );

        when(documentManagementService.generateDocument(templateName, placeHolders)).thenReturn(data);

        mockMvc.perform(
                post(contextPath)
                    .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().bytes(data))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
    }
}
