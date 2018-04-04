package uk.gov.hmcts.reform.divorce.documentgenerator.domain.request;

import java.util.Map;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Value;

import com.fasterxml.jackson.annotation.JsonProperty;

@Value
@ApiModel(description = "Request body model for Document Generation Request")
public class GenerateDocumentRequest {
    @ApiModelProperty(value = "Name of the template", required = true)
    @JsonProperty(value = "template", required = true)
    @NotBlank
    private final String template;
    @JsonProperty(value = "values", required = true)
    @ApiModelProperty(value = "Placeholder key / value pairs", required = true)
    private final Map<String, Object> values;
}
