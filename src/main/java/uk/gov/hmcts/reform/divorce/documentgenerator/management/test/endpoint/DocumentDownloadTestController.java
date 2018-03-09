package uk.gov.hmcts.reform.divorce.documentgenerator.management.test.endpoint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.divorce.documentgenerator.management.test.stub.DocumentDownloadService;

@RestController
@RequestMapping("/test")
@ConditionalOnProperty(value = "evidence-management-api.service.stub.enabled", havingValue = "true")
@Api(value = "Document Download Test when the Evidence Management is stubbed", tags = {"Document Generation Test"})
public class DocumentDownloadTestController {
    @Autowired
    private DocumentDownloadService documentDownloadService;

    @ApiOperation(value = "Returns the generated document given name of the file", tags = {"Document Generation Test"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns the PDF document.", response = byte[].class),
        })
    @GetMapping(value = "/documents/{fileName}", produces = "application/pdf")
    public ResponseEntity downloadDocument(@PathVariable
                                               @ApiParam(value = "Name of the file to download", required = true)
                                                       String fileName) {
        byte[] document = documentDownloadService.getDocument(fileName);

        return ResponseEntity
                .ok()
                .contentLength(document.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(document);
    }
}