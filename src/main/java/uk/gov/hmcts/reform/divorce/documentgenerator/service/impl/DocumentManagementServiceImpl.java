package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplatesConfiguration;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.GeneratedDocumentInfo;
import uk.gov.hmcts.reform.divorce.documentgenerator.factory.PDFGenerationFactory;
import uk.gov.hmcts.reform.divorce.documentgenerator.mapper.GeneratedDocumentInfoMapper;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.DocumentManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.EvidenceManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.PDFGenerationService;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.HtmlFieldFormatter;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

@Service
@Slf4j
public class DocumentManagementServiceImpl implements DocumentManagementService {

    private static final String CURRENT_DATE_KEY = "current_date";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss.SSS";

    private static final String FEATURE_TOGGLE_RESP_SOLCIITOR = "featureToggleRespSolicitor";

    private final Clock clock = Clock.systemDefaultZone();

    @Autowired
    private PDFGenerationFactory pdfGenerationFactory;

    @Autowired
    private EvidenceManagementService evidenceManagementService;

    @Autowired
    private TemplatesConfiguration templatesConfiguration;

    @Value("${feature-toggle.toggle.feature_resp_solicitor_details}")
    private String featureToggleRespSolicitor;

    @Override
    public GeneratedDocumentInfo generateAndStoreDocument(String templateName, Map<String, Object> placeholders,
                                                          String authorizationToken) {
        log.debug("Generate and Store Document requested with templateName [{}], placeholders of size [{}]",
            templateName, placeholders.size());
        String caseId = getCaseId(placeholders);//TODO - we should probably enforce or at least log an error if the caseId is not passed.

        log.info("Generating document for case Id {}", caseId);

        placeholders.put(
            CURRENT_DATE_KEY,
            new SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
                .format(Date.from(clock.instant())
                )
        );
        placeholders.put(FEATURE_TOGGLE_RESP_SOLCIITOR, Boolean.valueOf(featureToggleRespSolicitor));

        String fileName = templatesConfiguration.getFileNameByTemplateName(templateName);

        byte[] generatedDocument = generateDocument(templateName, placeholders);

        log.info("Document generated for case Id {}", caseId);

        return storeDocument(generatedDocument, authorizationToken, fileName);
    }

    @Override
    public GeneratedDocumentInfo storeDocument(byte[] document, String authorizationToken, String fileName) {
        log.debug("Store document requested with document of size [{}]", document.length);
        return GeneratedDocumentInfoMapper.mapToGeneratedDocumentInfo(
            evidenceManagementService.storeDocumentAndGetInfo(document, authorizationToken, fileName)
        );
    }

    @Override
    public byte[] generateDocument(String templateName, Map<String, Object> placeholders) {
        log.debug("Generate document requested with templateName [{}], placeholders of size[{}]",
            templateName, placeholders.size());

        Map<String, Object> formattedPlaceholders = placeholders;

        // Reform PDF Generator requires formatting for certain characters
        PDFGenerationService generatorService = pdfGenerationFactory.getGeneratorService(templateName);
        if (generatorService instanceof PDFGenerationServiceImpl) {
            formattedPlaceholders = HtmlFieldFormatter.format(placeholders);
        }

        return generatorService.generate(templateName, formattedPlaceholders);
    }

    private String getCaseId(Map<String, Object> placeholders) {
        Map<String, Object> caseDetails = (Map<String, Object>) placeholders.getOrDefault("caseDetails",
            Collections.emptyMap());
        return (String) caseDetails.get("id");
    }

    //TODO - phase 1 - add a default name for my new template
    //TODO - last phase - after everything else is done, add an optional file name parameter
    //TODO - design decision - should COS know about the template name? or should it only know about a logical name? On the one hand, COS would know less about implementation details, on the other hand, we'd have to touch DGS for every new document
    //  a good gain would be if we could not touch DGS for new templates - every new story would be easier to implement
    //  we might want to take an optional parameter with the file name - what do we do if it's not passed? maybe we give the user the choice to put it the map. if it's not in the map and not in the parameter, then we fail the request
    //TODO - make sure every AC is understood and catered for

    //TODO - could I remove power mock from dependencies (very last thing)

}
