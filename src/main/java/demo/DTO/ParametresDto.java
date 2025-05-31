package demo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ParametresDto {
    @JsonProperty(access = Access.READ_ONLY)
    private Long id;
    @NotNull
    @Min(1)
    private Long examinationTypeId;
    @NotBlank
    private String examinationTypeName;
    @NotBlank
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExaminationTypeId() {
        return examinationTypeId;
    }

    public void setExaminationTypeId(Long examinationTypeId) {
        this.examinationTypeId = examinationTypeId;
    }

    public String getExaminationTypeName() {
        return examinationTypeName;
    }

    public void setExaminationTypeName(String examinationTypeName) {
        this.examinationTypeName = examinationTypeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
