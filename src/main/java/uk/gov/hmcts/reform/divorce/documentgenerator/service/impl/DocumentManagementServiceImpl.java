package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplateNameConfiguration;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.GeneratedDocumentInfo;
import uk.gov.hmcts.reform.divorce.documentgenerator.factory.PDFGenerationFactory;
import uk.gov.hmcts.reform.divorce.documentgenerator.mapper.GeneratedDocumentInfoMapper;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.DocumentManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.EvidenceManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.HtmlFieldFormatter;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.FEATURE_TOGGLE_RESP_SOLCIITOR;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PDF_GENERATOR_TYPE;

@Service
@Slf4j
public class DocumentManagementServiceImpl implements DocumentManagementService {
    private static final String CURRENT_DATE_KEY = "current_date";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss.SSS";

    private static final String DRAFT_PREFIX = "Draft";
    private static final String IS_DRAFT = "isDraft";

    private final Clock clock = Clock.systemDefaultZone();

    private TemplateNameConfiguration templateNameConfiguration;

    @Autowired
    private PDFGenerationFactory pdfGenerationFactory;

    @Autowired
    private EvidenceManagementService evidenceManagementService;
    @Value("${feature-toggle.toggle.feature_resp_solicitor_details}")
    private String featureToggleRespSolicitor;

    @Autowired
    public void setTemplateNameConfiguration(TemplateNameConfiguration templateNameConfiguration) {
        this.templateNameConfiguration = templateNameConfiguration;
    }

    @Override
    public GeneratedDocumentInfo generateAndStoreDocument(String templateName, Map<String, Object> placeholders,
        String authorizationToken) {
        String fileName = templateNameConfiguration.getTemplatesName().get(templateName);
        return getGeneratedDocumentInfo(templateName, placeholders, authorizationToken, fileName);
    }

    @Override
    public GeneratedDocumentInfo generateAndStoreDraftDocument(String templateName,
                Map<String, Object> placeholders, String authorizationToken) {
        String fileName = templateNameConfiguration.getTemplatesName().get(templateName);
        if (!fileName.startsWith(DRAFT_PREFIX)) {
            fileName = String.join("", DRAFT_PREFIX, fileName);
        }
        placeholders.put(IS_DRAFT, true);

        return getGeneratedDocumentInfo(templateName, placeholders, authorizationToken, fileName);
    }

    private GeneratedDocumentInfo getGeneratedDocumentInfo(String templateName, Map<String, Object> placeholders,
                                                           String authorizationToken, String fileName) {
        log.debug("Generate and Store Document requested with templateName [{}], placeholders of size [{}]",
            templateName, placeholders.size());
        String caseId = getCaseId(placeholders);

        log.info("Generating document for case Id {}", caseId);

        placeholders.put(
            CURRENT_DATE_KEY,
            new SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
                .format(Date.from(clock.instant())
                )
        );
        placeholders.put(FEATURE_TOGGLE_RESP_SOLCIITOR, Boolean.valueOf(featureToggleRespSolicitor));

        byte[] generatedDocument = generateDocument(templateName, placeholders);

        log.info("Document generated for case Id {}", caseId);

        return storeDocument(generatedDocument, authorizationToken, fileName);
    }

    @Override
    public GeneratedDocumentInfo storeDocument(byte[] document, String authorizationToken, String fileName) {
        log.debug("Store document requested with document of size [{}]", document.length);
        return GeneratedDocumentInfoMapper
            .mapToGeneratedDocumentInfo(evidenceManagementService.storeDocumentAndGetInfo(document,
                authorizationToken, fileName));
    }

    @Override
    public byte[] generateDocument(String templateName, Map<String, Object> placeholders) {
        log.debug("Generate document requested with templateName [{}], placeholders of size[{}]",
            templateName, placeholders.size());

        Map<String, Object> formattedPlaceholders = placeholders;

        // Reform PDF Generator requires formatting for certain characters
        if (PDF_GENERATOR_TYPE.equals(pdfGenerationFactory.getGeneratorType(templateName))) {
            formattedPlaceholders = HtmlFieldFormatter.format(placeholders);
        }

        return pdfGenerationFactory.getGeneratorService(templateName).generate(templateName, formattedPlaceholders);
    }

    private String getCaseId(Map<String, Object> placeholders) {
        Map<String, Object> caseDetails = (Map<String, Object>) placeholders.getOrDefault("caseDetails",
            Collections.emptyMap());
        return (String) caseDetails.get("id");
    }
}



