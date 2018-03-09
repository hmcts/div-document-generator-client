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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.DocumentManagementService;

import java.util.Map;

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
            @ApiResponse(code = 200, message = "PDF was generated successfully. Returns the PDF document.",
                    response = byte[].class),
        })
    @RequestMapping(value = "/generateDocument", produces = "application/pdf", method = RequestMethod.POST)
    public ResponseEntity generateDocument(@RequestParam
                                               @ApiParam(value = "Form data containing the template name and key value "
                                                       + "pair of placeholder texts", required = true)
                                                       Map<String, Object> allRequestParams) {
        String templateName = (String) allRequestParams.remove(TEMPLATE_NAME);

        byte[] generatedPDF = documentManagementService.generateDocument(templateName, allRequestParams);

        return ResponseEntity
                .ok()
                .contentLength(generatedPDF.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(generatedPDF);
    }
}