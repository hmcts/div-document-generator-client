package uk.gov.hmcts.reform.divorce.documentgenerator.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.request.PdfDocumentRequest;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "docmosis-service-client", url = "${docmosis.service.pdf-service.uri}")
public interface DocmosisClient {

    @RequestMapping(
        method = RequestMethod.POST,
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    byte[] render(@RequestBody PdfDocumentRequest request);
}
