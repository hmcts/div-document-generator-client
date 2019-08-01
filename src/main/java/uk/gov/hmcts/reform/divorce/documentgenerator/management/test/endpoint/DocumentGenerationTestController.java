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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.request.GenerateDocumentRequest;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.DocumentManagementService;

import javax.validation.Valid;

@RestController
@RequestMapping("/test")
@ConditionalOnProperty("pdf.test.enabled")
@Api(value = "Document Generation Test", tags = {"Document Generation Test"})
public class DocumentGenerationTestController {

    private static final String TEMPLATE_NAME = "templateName";

    @Autowired
    private DocumentManagementService documentManagementService;

    @ApiOperation(value = "Generate PDF document based on the supplied template name and placeholder texts and returns "
            + "the PDF.", tags = {"Document Generation Test"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "PDF was generated successfully. Returns the PDF document."),
        })
    @RequestMapping(value = "/generateDocument", produces = "application/pdf", method = RequestMethod.POST)
    public ResponseEntity generateDocument(
                        @ApiParam(value = "JSON object containing the templateName and the placeholder text map", required = true)
                        @RequestBody
                        @Valid
                        GenerateDocumentRequest templateData) {
        byte[] generatedPDF = documentManagementService.generateDocument(templateData.getTemplate(), templateData.getValues());

        return ResponseEntity
                .ok()
                .contentLength(generatedPDF.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(generatedPDF);
    }
}
