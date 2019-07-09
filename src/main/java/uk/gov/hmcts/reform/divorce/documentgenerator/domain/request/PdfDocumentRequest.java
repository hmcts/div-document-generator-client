package uk.gov.hmcts.reform.divorce.documentgenerator.domain.request;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Data
@Builder
public class PdfDocumentRequest {

    private String accessKey;

private String templateName;

    private String outputName;

    private String devMode;

    private Map<String,Object> data;
}
