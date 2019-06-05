package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.GeneratedDocumentInfo;
import uk.gov.hmcts.reform.divorce.documentgenerator.factory.PDFGenerationFactory;
import uk.gov.hmcts.reform.divorce.documentgenerator.mapper.GeneratedDocumentInfoMapper;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.DocumentManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.EvidenceManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.TemplateManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.HtmlFieldFormatter;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_INVITATION_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_INVITATION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CERTIFICATE_OF_ENTITLEMENT_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CERTIFICATE_OF_ENTITLEMENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_ANSWERS_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_INVITATION_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_INVITATION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.MINI_PETITION_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.MINI_PETITION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESPONDENT_ANSWERS_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COS;


@Service
@Slf4j
public class DocumentManagementServiceImpl implements DocumentManagementService {
    private static final String CURRENT_DATE_KEY = "current_date";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss.SSS";

    private final Clock clock = Clock.systemDefaultZone();

    @Autowired
    private TemplateManagementService templateManagementService;

    @Autowired
    private PDFGenerationFactory pdfGenerationFactory;

    @Autowired
    private EvidenceManagementService evidenceManagementService;

    @Override
    public GeneratedDocumentInfo generateAndStoreDocument(String templateName, Map<String, Object> placeholders,
                                                          String authorizationToken) {
        log.debug("Generate and Store Document requested with templateName [{}], placeholders of size [{}]",
                templateName, placeholders.size());

        placeholders.put(CURRENT_DATE_KEY,
                new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date.from(clock.instant())));

        String fileName = getFileNameFromTemplateName(templateName);

        byte[] generatedDocument = generateDocument(templateName, placeholders);
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

        Map<String, Object> formattedPlaceholders = HtmlFieldFormatter.format(placeholders);

        return pdfGenerationFactory.getGeneratorService(templateName).generate(templateName, formattedPlaceholders);
    }

    private String getFileNameFromTemplateName(String templateName) {
        switch (templateName) {
            case AOS_INVITATION_TEMPLATE_ID :
                return AOS_INVITATION_NAME_FOR_PDF_FILE;
            case MINI_PETITION_TEMPLATE_ID :
                return MINI_PETITION_NAME_FOR_PDF_FILE;
            case CO_RESPONDENT_INVITATION_TEMPLATE_ID :
                return CO_RESPONDENT_INVITATION_NAME_FOR_PDF_FILE;
            case RESPONDENT_ANSWERS_TEMPLATE_ID :
                return RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE;
            case CO_RESPONDENT_ANSWERS_TEMPLATE_ID :
                return CO_RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE;
            case CERTIFICATE_OF_ENTITLEMENT_TEMPLATE_ID :
                return CERTIFICATE_OF_ENTITLEMENT_NAME_FOR_PDF_FILE;
            case COSTS_ORDER_DOCUMENT_ID:
                return COSTS_ORDER_NAME_FOR_PDF_FILE;
            default : throw new IllegalArgumentException("Unknown template: " + templateName);
        }
    }
}
