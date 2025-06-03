package demo.DTO;

import java.time.LocalDate;
import java.util.Map;

public class ExaminationStatistic {
    private Long totalExaminations;
    private Map<String, Long> examinationsByType;
    private Map<LocalDate, Long> examinationsByDate;

    public Long getTotalExaminations() {
        return totalExaminations;
    }

    public Map<String, Long> getExaminationsByType() {
        return examinationsByType;
    }

    public Map<LocalDate, Long> getExaminationsByDate() {
        return examinationsByDate;
    }

    public void setTotalExaminations(Long totalExaminations) {
        this.totalExaminations = totalExaminations;
    }

    public void setExaminationsByDate(Map<LocalDate, Long> examinationsByDate) {
        this.examinationsByDate = examinationsByDate;
    }

    public void setExaminationsByType(Map<String, Long> examinationsByType) {
        this.examinationsByType = examinationsByType;
    }
}