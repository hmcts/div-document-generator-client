package uk.gov.hmcts.reform.divorce.documentgenerator.management.test.endpoint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.divorce.documentgenerator.management.test.stub.DocumentDownloadService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DocumentDownloadTestController.class)
@TestPropertySource(properties = {
        "evidence-management-api.service.stub.enabled=true"})
public class DocumentDownloadTestControllerUTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentDownloadService documentDownloadService;

    @Test
    public void whenDownloadDocument_thenReturnProperResponse() throws Exception {
        final String filename = "fileName";
        final byte[] data = {1};
        final String contextPath = "/test/documents/" + filename;

        when(documentDownloadService.getDocument(filename)).thenReturn(data);

        mockMvc.perform(get(contextPath))
                .andExpect(status().isOk())
                .andExpect(content().bytes(data))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }
}