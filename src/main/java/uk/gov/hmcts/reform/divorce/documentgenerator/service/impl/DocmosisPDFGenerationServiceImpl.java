package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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
public class DocmosisPDFGenerationServiceImpl implements PDFGenerationService {

    @Autowired
    private RestTemplate restTemplate;

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
            // Remove this log when tested
            log.info("Making Docmosis Request From {}", pdfServiceEndpoint);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            HttpEntity<PdfDocumentRequest> httpEntity = new HttpEntity<>(request(templateName, placeholders), headers);

            ResponseEntity<byte[]> response =
                restTemplate.exchange(pdfServiceEndpoint, HttpMethod.POST, httpEntity, byte[].class);

            return response.getBody();
        } catch (Exception e) {
            throw new PDFGenerationException("Failed to request PDF from REST endpoint " + e.getMessage(), e);
        }
    }

    private PdfDocumentRequest request(String templateName, Map<String, Object> placeholders) {
        log.info("XXXXXX key = {}", pdfServiceAccessKey);
        return PdfDocumentRequest.builder()
            .accessKey("dyVv8pXwQ03RRyJZQIPX2RWP9LgJJGTU08kc9dA8ATJoA9EZXQEWe7L1Uwe")
            .templateName(templateName)
            .outputName("result.pdf")
            .devMode(docmosisDevMode)
            .data(templateDataMapper.map(placeholders)).build();
    }

}
