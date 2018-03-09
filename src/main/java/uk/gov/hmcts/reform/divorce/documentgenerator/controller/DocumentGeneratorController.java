package uk.gov.hmcts.reform.divorce.documentgenerator.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.request.GenerateDocumentRequest;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.GeneratedDocumentInfo;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.DocumentManagementService;

import javax.validation.Valid;

@RestController
@Api(value = "Document Generation", tags = {"Document Generation"})
@Slf4j
public class DocumentGeneratorController {

    @Autowired
    private DocumentManagementService documentManagementService;

    @ApiOperation(value = "Generate PDF document based on the supplied template name and placeholder texts and saves "
            + "it in the evidence management.", tags = {"Document Generation"})
    @ApiResponses({
            @ApiResponse(code = 200, message = "PDF was generated successfully and stored in the evidence management."
                    + " Returns the url to the stored document.", response = String.class),
            @ApiResponse(code = 400, message = "Returned when input parameters are invalid or template not found",
                    response = String.class),
            @ApiResponse(code = 503, message = "Returned when the PDF Service or Evidence Management Client Api "
                    + "cannot be reached", response = String.class),
            @ApiResponse(code = 500, message = "Returned when there is an unknown server error",
                    response = String.class)
        })
    @PostMapping("/version/1/generatePDF")
    public GeneratedDocumentInfo generatePDF(@RequestBody @Valid @ApiParam(value = "JSON object containing the "
            + "templateName and the placeholder text map", required = true) GenerateDocumentRequest templateData) {
        //This service is internal to Divorce system. No need to do service authentication here
        log.info("Document generation requested with templateName [{}], placeholders map of size[{}]",
                templateData.getTemplate(), templateData.getValues().size());
        return documentManagementService.generateAndStoreDocument(templateData.getTemplate(), templateData.getValues());
    }
}