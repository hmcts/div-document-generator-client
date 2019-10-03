package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.GeneratedDocumentInfo;
import uk.gov.hmcts.reform.divorce.documentgenerator.factory.PDFGenerationFactory;
import uk.gov.hmcts.reform.divorce.documentgenerator.mapper.GeneratedDocumentInfoMapper;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.DocumentManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.EvidenceManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.HtmlFieldFormatter;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_INVITATION_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_INVITATION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_2_YEAR_SEPARATION_FORM_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_2_YEAR_SEPARATION_FORM_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_5_YEAR_SEPARATION_FORM_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_5_YEAR_SEPARATION_FORM_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CASE_LIST_FOR_PRONOUNCEMENT_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CASE_LIST_FOR_PRONOUNCEMENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CERTIFICATE_OF_ENTITLEMENT_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CERTIFICATE_OF_ENTITLEMENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COSTS_ORDER_DOCUMENT_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COSTS_ORDER_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_ANSWERS_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_INVITATION_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_INVITATION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_ABSOLUTE_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_ABSOLUTE_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_ANSWERS_TEMPLATE_NAME;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_TEMPLATE_NAME;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_ANSWERS_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_REFUSAL_ORDER_CLARIFICATION_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_REFUSAL_ORDER_REJECTION_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DRAFT_MINI_PETITION_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DRAFT_MINI_PETITION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.MINI_PETITION_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.MINI_PETITION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PDF_GENERATOR_TYPE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESPONDENT_ANSWERS_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SOLICITOR_PERSONAL_SERVICE_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SOLICITOR_PERSONAL_SERVICE_TEMPLATE_ID;

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

    @Value("${feature-toggle.toggle.feature_resp_solicitor_details}")
    private String featureToggleRespSolicitor;

    @Override
    public GeneratedDocumentInfo generateAndStoreDocument(String templateName, Map<String, Object> placeholders,
                                                          String authorizationToken) {
        log.debug("Generate and Store Document requested with templateName [{}], placeholders of size [{}]",
            templateName, placeholders.size());

        placeholders.put(
            CURRENT_DATE_KEY,
            new SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
                .format(Date.from(clock.instant())
                )
        );
        placeholders.put(FEATURE_TOGGLE_RESP_SOLCIITOR, Boolean.valueOf(featureToggleRespSolicitor));

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

        Map<String, Object> formattedPlaceholders = placeholders;

        // Reform PDF Generator requires formatting for certain characters
        if (PDF_GENERATOR_TYPE.equals(pdfGenerationFactory.getGeneratorType(templateName))) {
            formattedPlaceholders = HtmlFieldFormatter.format(placeholders);
        }

        return pdfGenerationFactory.getGeneratorService(templateName).generate(templateName, formattedPlaceholders);
    }

    private String getFileNameFromTemplateName(String templateName) {
        switch (templateName) {
            case AOS_INVITATION_TEMPLATE_ID:
                return AOS_INVITATION_NAME_FOR_PDF_FILE;
            case MINI_PETITION_TEMPLATE_ID:
                return MINI_PETITION_NAME_FOR_PDF_FILE;
            case DRAFT_MINI_PETITION_TEMPLATE_ID:
                return DRAFT_MINI_PETITION_NAME_FOR_PDF_FILE;
            case CO_RESPONDENT_INVITATION_TEMPLATE_ID:
                return CO_RESPONDENT_INVITATION_NAME_FOR_PDF_FILE;
            case RESPONDENT_ANSWERS_TEMPLATE_ID:
                return RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE;
            case CO_RESPONDENT_ANSWERS_TEMPLATE_ID:
                return CO_RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE;
            case CERTIFICATE_OF_ENTITLEMENT_TEMPLATE_ID:
                return CERTIFICATE_OF_ENTITLEMENT_NAME_FOR_PDF_FILE;
            case COSTS_ORDER_DOCUMENT_ID:
                return COSTS_ORDER_NAME_FOR_PDF_FILE;
            case DECREE_NISI_TEMPLATE_ID:
                return DECREE_NISI_TEMPLATE_NAME;
            case CASE_LIST_FOR_PRONOUNCEMENT_TEMPLATE_ID:
                return CASE_LIST_FOR_PRONOUNCEMENT_NAME_FOR_PDF_FILE;
            case DECREE_ABSOLUTE_TEMPLATE_ID:
                return DECREE_ABSOLUTE_NAME_FOR_PDF_FILE;
            case DN_ANSWERS_TEMPLATE_ID:
                return DECREE_NISI_ANSWERS_TEMPLATE_NAME;
            case AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_TEMPLATE_ID:
                return AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_NAME_FOR_PDF_FILE;
            case AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_TEMPLATE_ID:
                return AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_NAME_FOR_PDF_FILE;
            case AOS_OFFLINE_2_YEAR_SEPARATION_FORM_TEMPLATE_ID:
                return AOS_OFFLINE_2_YEAR_SEPARATION_FORM_NAME_FOR_PDF_FILE;
            case AOS_OFFLINE_5_YEAR_SEPARATION_FORM_TEMPLATE_ID:
                return AOS_OFFLINE_5_YEAR_SEPARATION_FORM_NAME_FOR_PDF_FILE;
            case AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_TEMPLATE_ID:
                return AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_NAME_FOR_PDF_FILE;
            case AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_TEMPLATE_ID:
                return AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_NAME_FOR_PDF_FILE;
            case AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_TEMPLATE_ID:
                return AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_NAME_FOR_PDF_FILE;
            case SOLICITOR_PERSONAL_SERVICE_TEMPLATE_ID:
                return SOLICITOR_PERSONAL_SERVICE_NAME_FOR_PDF_FILE;
            case DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID:
                return DN_REFUSAL_ORDER_CLARIFICATION_NAME_FOR_PDF_FILE;
            case DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID:
                return DN_REFUSAL_ORDER_REJECTION_NAME_FOR_PDF_FILE;
            default:
                throw new IllegalArgumentException("Unknown template: " + templateName);
        }
    }

}
