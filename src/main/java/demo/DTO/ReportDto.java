package demo.DTO;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public class ReportDto {
    @NotNull
    private List<ExaminationDto> examinations;

    public List<ExaminationDto> getExaminations() {
        return examinations;
    }

    public void setExaminations(List<ExaminationDto> examinations) {
        this.examinations = examinations;
    }
}
