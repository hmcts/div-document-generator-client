package uk.gov.hmcts.reform.divorce.documentgenerator.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.request.GenerateDocumentRequest;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.DocumentManagementService;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocumentGenerateDownloadControllerTest {

    @Mock
    private DocumentManagementService documentManagementService;

    @InjectMocks
    private DocumentGenerateDownloadController classUnderTest;

    @Test
    public void whenGenerateDocument_thenReturnProperResponse() {
        final byte[] data = {1};
        final String someOtherKeyName = "someOtherKeyName";
        final String someOtherKeyValue = "someOtherKeyValue";

        final Map<String, Object> placeHolders = Collections.singletonMap(someOtherKeyName, someOtherKeyValue);
        final String templateName = "templateName";

        GenerateDocumentRequest generateDocumentRequest = new GenerateDocumentRequest(
            templateName,
            placeHolders
        );

        when(documentManagementService.generateDocument(templateName, placeHolders)).thenReturn(data);

        classUnderTest.generatePdfBinary(generateDocumentRequest);

        verify(documentManagementService, times(1))
            .generateDocument(templateName, placeHolders);
    }
}
