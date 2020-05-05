package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Qualifier("pdfGenerator")
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

    @Value("${service.pdf-service.uri}")
    private String pdfServiceEndpoint;

    @Autowired
    private TemplateManagementService templateManagementService;

    @Override
    public byte[] generate(String templateName, Map<String, Object> placeholders) {
        NullOrEmptyValidator.requireNonBlank(templateName);//TODO - ?
        Objects.requireNonNull(placeholders);//TODO - ?
        byte[] template = templateManagementService.getTemplateByName(templateName);
        log.info("Making request to pdf service to generate pdf document with template bytes of size [{}] "
              + "and placeholders of size [{}]", template.length, placeholders.size());

        try {
            return restTemplate.postForObject(
                    pdfServiceEndpoint,
                    buildRequest(serviceTokenGenerator.generate(), template, placeholders),
                    byte[].class);//TODO - at worse at fails and at best it generates a blank document - maybe I need to discuss with the team what we want to happen
            //TODO - I think I can make a decision that I want this to throw an exception that gets translated to a proper error to the user - let's just see if it makes sense to implement it as part of this
        } catch (Exception e) {
            throw new PDFGenerationException("Failed to request PDF from REST endpoint " + e.getMessage(), e);
        }
    }

    private HttpEntity<String> buildRequest(String serviceAuthToken, byte[] template,
                                            Map<String, Object> placeholders) {
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
