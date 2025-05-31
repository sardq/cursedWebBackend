package demo.DTO;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MediaDto {
    private Long id;
    @NotNull
    @Min(1)
    private Long examinationId;
    @NotBlank
    private String examinationName;
    @NotBlank
    private MediaType resource;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExaminationId() {
        return examinationId;
    }

    public void SetExaminationId(Long examinationId) {
        this.examinationId = examinationId;
    }

    public String getExaminationName() {
        return examinationName;
    }

    public void setExaminationName(String examinationName) {
        this.examinationName = examinationName;
    }

    public MediaType getResource() {
        return resource;
    }

    public void setResource(MediaType resource) {
        this.resource = resource;
    }
}
