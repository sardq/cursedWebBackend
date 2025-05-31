package demo.DTO;

import java.util.Map;

public class ExaminationStatistic {
    private Long totalExaminations;
    private Map<String, Long> examinationsByType;

    public Long getTotalExaminations() {
        return totalExaminations;
    }

    public Map<String, Long> getExaminationsByType() {
        return examinationsByType;
    }

    public void setTotalExaminations(Long totalExaminations) {
        this.totalExaminations = totalExaminations;
    }

    public void setExaminationsByType(Map<String, Long> examinationsByType) {
        this.examinationsByType = examinationsByType;
    }

}