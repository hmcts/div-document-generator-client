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
        String caseId = getCaseId(placeholders);
        if (caseId == null) {
            log.warn("caseId is null for template \"" + templateName + "\"");
        }

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
        log.debug("Generate document requested with templateName [{}], placeholders of size[{}]", templateName, placeholders.size());

        PDFGenerationService generatorService = pdfGenerationFactory.getGeneratorService(templateName);
        return generatorService.generate(templateName, placeholders);
    }

    private String getCaseId(Map<String, Object> placeholders) {
        Map<String, Object> caseDetails = (Map<String, Object>) placeholders.getOrDefault("caseDetails", Collections.emptyMap());
        return (String) caseDetails.get("id");
    }

}
