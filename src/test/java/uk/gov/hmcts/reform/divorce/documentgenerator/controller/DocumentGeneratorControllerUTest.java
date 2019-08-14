package uk.gov.hmcts.reform.divorce.documentgenerator.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.request.GenerateDocumentRequest;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.GeneratedDocumentInfo;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.DocumentManagementService;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocumentGeneratorControllerUTest {

    @Mock
    private DocumentManagementService documentManagementService;

    @InjectMocks
    private DocumentGeneratorController classUnderTest;

    @Test
    public void whenGeneratePDF_thenReturnGeneratedPDFDocumentInfo() {
        final String templateName = "templateName";
        final Map<String, Object> placeholder = Collections.emptyMap();

        final GeneratedDocumentInfo expected = new GeneratedDocumentInfo();

        when(documentManagementService.generateAndStoreDocument(templateName, placeholder, "testToken"))
            .thenReturn(expected);

        GeneratedDocumentInfo actual = classUnderTest
                .generateAndUploadPdf("testToken", new GenerateDocumentRequest(templateName, placeholder));

        assertEquals(expected, actual);

        verify(documentManagementService, times(1))
                .generateAndStoreDocument(templateName, placeholder, "testToken");
    }

}
