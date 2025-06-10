package uk.gov.hmcts.reform.divorce.documentgenerator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.request.GenerateDocumentRequest;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.DocumentManagementService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/version/1")
@Tag(name = "Document Generation and Download", description = "Operations related to PDF document generation and download")
@Slf4j
@ConditionalOnProperty("pdf.test.enabled")
public class DocumentGenerateDownloadController {

    @Autowired
    private DocumentManagementService documentManagementService;

    @Operation(
        summary = "Generate PDF document based on the supplied template name and placeholder texts and returns the PDF.",
        tags = {"Document Generation"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "PDF was generated successfully. Returns the PDF document.",
            content = @Content(mediaType = "application/octet-stream")
        )
    })
    @RequestMapping(value = "/generate-pdf-binary", produces = "application/octet-stream", method = RequestMethod.POST)
    public ResponseEntity<byte[]> generatePdfBinary(
        @Parameter(description = "JSON object containing the templateName and case details", required = true)
        @RequestBody
        @Valid
        GenerateDocumentRequest templateData) {
        byte[] pdf = documentManagementService.generateDocument(templateData.getTemplate(), templateData.getValues());

        return ResponseEntity
            .ok()
            .contentLength(pdf.length)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(pdf);
    }
}
