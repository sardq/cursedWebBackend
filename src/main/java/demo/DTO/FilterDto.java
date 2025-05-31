package demo.DTO;

public class FilterDto {
    private String examinationTypeName;
    private String examinationDateStart;
    private String examinationDateEnd;

    public String getExaminationTypeName() {
        return examinationTypeName;
    }

    public void setExaminationTypeName(String examinationTypeName) {
        this.examinationTypeName = examinationTypeName;
    }

    public String getExaminationDateStart() {
        return examinationDateStart;
    }

    public void setExaminationDateStart(String examinationDateStart) {
        this.examinationDateStart = examinationDateStart;
    }

    public String getExaminationDateEnd() {
        return examinationDateEnd;
    }

    public void setExaminationDateEnd(String examinationDateEnd) {
        this.examinationDateEnd = examinationDateEnd;
    }
}
