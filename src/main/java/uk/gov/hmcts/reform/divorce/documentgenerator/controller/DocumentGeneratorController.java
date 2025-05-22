package uk.gov.hmcts.reform.divorce.documentgenerator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.request.GenerateDocumentRequest;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.GeneratedDocumentInfo;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.DocumentManagementService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/version/1")
@Tag(name = "Document Generation", description = "Operations related to generating documents")
@Slf4j
public class DocumentGeneratorController {

    @Autowired
    private DocumentManagementService documentManagementService;

    @Operation(
        summary = "Generate PDF document",
        description = "Generate PDF document based on the supplied template name and placeholder texts and saves it in the evidence management."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "PDF was generated successfully and stored in the evidence management. Returns the URL to the stored document."
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Returned when input parameters are invalid or template not found"
        ),
        @ApiResponse(
            responseCode = "503",
            description = "Returned when the PDF Service or Evidence Management Client API cannot be reached"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Returned when there is an unknown server error"
        )
    })
    @PostMapping("/generatePDF")
    public GeneratedDocumentInfo generateAndUploadPdf(
        @RequestHeader(value = "Authorization", required = false)
            String authorizationToken,
        @Parameter(
            description = "JSON object containing the template name and placeholder values",
            required = true
        )
        @RequestBody
        @Valid
            GenerateDocumentRequest templateData) {
        //This service is internal to Divorce system. No need to do service authentication here
        log.info("Document generation requested with templateName [{}], placeholders map of size[{}]",
                templateData.getTemplate(), templateData.getValues().size());
        return documentManagementService.generateAndStoreDocument(templateData.getTemplate(), templateData.getValues(),
            authorizationToken);
    }

    @Operation(
        summary = "Generate and upload draft PDF document",
        description = "Generates a draft PDF using the supplied template name and placeholders, and stores it in Evidence Management.",
        tags = {"Document Generation"}
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "PDF was generated successfully and stored in the evidence management. Returns the URL to the stored document."),
        @ApiResponse(responseCode = "400", description = "Returned when input parameters are invalid or template not found"),
        @ApiResponse(responseCode = "503", description = "Returned when the PDF Service or Evidence Management Client API cannot be reached"),
        @ApiResponse(responseCode = "500", description = "Returned when there is an unknown server error")
    })
    @PostMapping("/generateDraftPDF")
    public GeneratedDocumentInfo generateAndUploadDraftPdf(
        @RequestHeader(value = "Authorization", required = false)
            String authorizationToken,
        @Parameter(description = "JSON object containing the template name and the placeholder text map", required = true)
        @RequestBody
        @Valid
            GenerateDocumentRequest templateData) {
        //This service is internal to Divorce system. No need to do service authentication here
        log.info("Document generation requested with templateName [{}], placeholders map of size[{}]",
                templateData.getTemplate(), templateData.getValues().size());
        return documentManagementService.generateAndStoreDraftDocument(templateData.getTemplate(),
                templateData.getValues(), authorizationToken);
    }

}
