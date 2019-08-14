package uk.gov.hmcts.reform.divorce.documentgenerator.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(value = "Document Generation and Download", tags = {"Document Generation and Download"})
@Slf4j
@ConditionalOnProperty("pdf.test.enabled")
public class DocumentGenerateDownloadController {

    @Autowired
    private DocumentManagementService documentManagementService;

    @ApiOperation(value = "Generate PDF document based on the supplied template name and placeholder texts and returns "
        + "the PDF.", tags = {"Document Generation"})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "PDF was generated successfully. Returns the PDF document."),
    })
    @RequestMapping(value = "/generate-pdf-binary", produces = "application/octet-stream", method = RequestMethod.POST)
    public ResponseEntity generatePdfBinary(
        @ApiParam(value = "JSON object containing the templateName and case details", required = true)
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
