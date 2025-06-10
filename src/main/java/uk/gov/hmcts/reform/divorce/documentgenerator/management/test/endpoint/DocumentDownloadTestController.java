package uk.gov.hmcts.reform.divorce.documentgenerator.management.test.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
    name = "Document Generation Test",
    description = "Document Download Test when the Evidence Management is stubbed"
)
public class DocumentDownloadTestController {
    @Autowired
    private DocumentDownloadService documentDownloadService;

    @Operation(
        summary = "Returns the generated document given name of the file",
        tags = {"Document Generation Test"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Returns the PDF document",
            content = @Content(mediaType = "application/pdf")
        )
    })

    @GetMapping(value = "/documents/{fileName}", produces = "application/pdf")
    public ResponseEntity<byte[]> downloadDocument(
        @Parameter(description = "Name of the file to download", required = true)
        @PathVariable String fileName) {
        byte[] document = documentDownloadService.getDocument(fileName);

        return ResponseEntity
            .ok()
            .contentLength(document.length)
            .contentType(MediaType.APPLICATION_PDF)
            .body(document);
    }
}
