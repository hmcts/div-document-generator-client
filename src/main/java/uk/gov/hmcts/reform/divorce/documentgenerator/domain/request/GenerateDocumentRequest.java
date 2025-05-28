package uk.gov.hmcts.reform.divorce.documentgenerator.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.util.Map;
import javax.validation.constraints.NotBlank;

@Value
@Schema(description = "Request body model for Document Generation Request")
public class GenerateDocumentRequest {
    @Schema(description = "Name of the template", required = true, example = "divorce-template")
    @JsonProperty(value = "template", required = true)
    @NotBlank
    String template;
    @JsonProperty(value = "values", required = true)
    @Schema(description = "Placeholder key / value pairs", required = true)
    Map<String, Object> values;
}
