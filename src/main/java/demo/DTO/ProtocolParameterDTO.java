package demo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ProtocolParameterDTO {
    @JsonProperty(access = Access.READ_ONLY)
    private Long id;
    @Min(1)
    private Long parametersId;
    @NotBlank
    private String parametersName;
    @NotBlank
    private String body;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParametersName() {
        return parametersName;
    }

    public void setParametersName(String parametersName) {
        this.parametersName = parametersName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getParametersId() {
        return parametersId;
    }

    public void setParametersId(Long parametersId) {
        this.parametersId = parametersId;
    }
}