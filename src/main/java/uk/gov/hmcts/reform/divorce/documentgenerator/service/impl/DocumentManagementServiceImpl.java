package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.GeneratedDocumentInfo;
import uk.gov.hmcts.reform.divorce.documentgenerator.mapper.GeneratedDocumentInfoMapper;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.DocumentManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.EvidenceManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.PDFGenerationService;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.TemplateManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.HtmlFieldFormatter;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

@Service
@Slf4j
public class DocumentManagementServiceImpl implements DocumentManagementService {
    private static final String CURRENT_DATE_KEY = "current_date";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss.SSS";

    private final Clock clock = Clock.systemDefaultZone();

    @Autowired
    private TemplateManagementService templateManagementService;

    @Autowired
    private PDFGenerationService pdfGenerationService;

    @Autowired
    private EvidenceManagementService evidenceManagementService;

    @Override
    public GeneratedDocumentInfo generateAndStoreDocument(String templateName, Map<String, Object> placeholders, String authorizationToken) {
        log.debug("Generate and Store Document requested with templateName [{}], placeholders of size [{}]",
                templateName, placeholders.size());

        placeholders.put(CURRENT_DATE_KEY,
                new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date.from(clock.instant())));

        return storeDocument(generateDocument(templateName, placeholders), authorizationToken);
    }

    @Override
    public GeneratedDocumentInfo storeDocument(byte[] document, String authorizationToken) {
        log.debug("Store document requested with document of size [{}]", document.length);
        return GeneratedDocumentInfoMapper
                .mapToGeneratedDocumentInfo(evidenceManagementService.storeDocumentAndGetInfo(document, authorizationToken));
    }

    @Override
    public byte[] generateDocument(String templateName, Map<String, Object> placeholders) {
        log.debug("Generate document requested with templateName [{}], placeholders of size[{}]",
                templateName, placeholders.size());

        byte[] templateBytes = templateManagementService.getTemplateByName(templateName);

        Map<String, Object> formattedPlaceholders = HtmlFieldFormatter.format(placeholders);

        return pdfGenerationService.generateFromHtml(templateBytes, formattedPlaceholders);
    }
}
