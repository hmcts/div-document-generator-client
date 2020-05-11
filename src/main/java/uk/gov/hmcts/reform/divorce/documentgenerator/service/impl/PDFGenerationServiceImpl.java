package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.request.GenerateDocumentRequest;
import uk.gov.hmcts.reform.divorce.documentgenerator.exception.PDFGenerationException;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.PDFGenerationService;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.TemplateManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.NullOrEmptyValidator;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class PDFGenerationServiceImpl implements PDFGenerationService {
    private static final MediaType API_VERSION = MediaType
            .valueOf("application/vnd.uk.gov.hmcts.pdf-service.v2+json;charset=UTF-8");

    private static final String SERVICE_AUTHORIZATION_HEADER = "ServiceAuthorization";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthTokenGenerator serviceTokenGenerator;

    @Autowired
    private TemplateManagementService templateManagementService;

    private String pdfServiceEndpoint;

    public PDFGenerationServiceImpl(@Value("${service.pdf-service.uri}") String pdfServiceEndpoint) {
        this.pdfServiceEndpoint = pdfServiceEndpoint;
    }

    @Override
    public byte[] generate(String templateName, Map<String, Object> placeholders) {
        NullOrEmptyValidator.requireNonBlank(templateName);//TODO - next PR
        Objects.requireNonNull(placeholders);//TODO - next PR
        byte[] template = templateManagementService.getTemplateByName(templateName);
        log.info("Making request to pdf service to generate pdf document with template bytes of size [{}] "
              + "and placeholders of size [{}]", template.length, placeholders.size());

        try {
            String authToken = serviceTokenGenerator.generate();
            HttpEntity<String> request = buildRequest(authToken, template, placeholders);
            return restTemplate.postForObject(pdfServiceEndpoint, request, byte[].class);//restTemplate.postForObject("pdfServiceEndpoint","", byte[].class)
        } catch (Exception e) {
            throw new PDFGenerationException("Failed to request PDF from REST endpoint " + e.getMessage(), e);
        }
    }

    private HttpEntity<String> buildRequest(String serviceAuthToken, byte[] template, Map<String, Object> placeholders) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(API_VERSION);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_PDF));
        headers.add(SERVICE_AUTHORIZATION_HEADER, serviceAuthToken);

        GenerateDocumentRequest request = new GenerateDocumentRequest(new String(template), placeholders);

        try {
            return new HttpEntity<>(objectMapper.writeValueAsString(request), headers);
        } catch (JsonProcessingException e) {
            throw new PDFGenerationException("Failed to convert PDF request into JSON", e);
        }
    }
}
