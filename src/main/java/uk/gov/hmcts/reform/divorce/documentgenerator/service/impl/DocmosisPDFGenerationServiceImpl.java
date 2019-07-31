package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.divorce.documentgenerator.client.DocmosisClient;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.request.PdfDocumentRequest;
import uk.gov.hmcts.reform.divorce.documentgenerator.exception.PDFGenerationException;
import uk.gov.hmcts.reform.divorce.documentgenerator.mapper.TemplateDataMapper;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.PDFGenerationService;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

@Service
@Slf4j
@Qualifier("docmosisPdfGenerator")
public class DocmosisPDFGenerationServiceImpl implements PDFGenerationService {

    @Autowired
    private DocmosisClient docmosisClient;

    @Autowired
    private TemplateDataMapper templateDataMapper;

    @Value("${docmosis.service.pdf-service.uri}")
    private String pdfServiceEndpoint;

    @Value("${docmosis.service.pdf-service.accessKey}")
    private String pdfServiceAccessKey;

    @Value("${docmosis.service.pdf-service.devMode}")
    private String docmosisDevMode;

    @Override
    public byte[] generate(String templateName, Map<String, Object> placeholders) {
        checkArgument(!isNullOrEmpty(templateName), "document generation template cannot be empty");
        checkNotNull(placeholders, "placeholders map cannot be null");

        log.info("Making request to pdf service to generate pdf document with template [{}]"
            + " and placeholders of size [{}]", templateName, placeholders.size());

        try {
            return docmosisClient.render(request(templateName, placeholders));
        } catch (Exception e) {
            throw new PDFGenerationException("Failed to request PDF from REST endpoint " + e.getMessage(), e);
        }
    }

    private PdfDocumentRequest request(String templateName, Map<String, Object> placeholders) {
        return PdfDocumentRequest.builder()
            .accessKey(pdfServiceAccessKey)
            .templateName(templateName)
            .outputName("result.pdf")
            .devMode(docmosisDevMode)
            .data(templateDataMapper.map(placeholders)).build();
    }
}
