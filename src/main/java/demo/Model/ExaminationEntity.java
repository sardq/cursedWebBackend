package demo.Model;

import java.time.LocalDate;
import java.util.Objects;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonFormat;

import demo.core.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "examinations")
public class ExaminationEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "userEntity", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "examinationTypeEntity", nullable = false)
    private ExaminationTypeEntity examinationType;
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate time;
    @Size(min = 10, max = 100)
    private String conclusion;
    @Size(min = 10, max = 100)
    private String description;

    public ExaminationEntity() {
    }

    public ExaminationEntity(UserEntity user, ExaminationTypeEntity examinationType,
            LocalDate time, String conclusion,
            String description) {
        this.user = user;
        this.examinationType = examinationType;
        this.time = time;
        this.conclusion = conclusion;
        this.description = description;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ExaminationTypeEntity getExaminationType() {
        return examinationType;
    }

    public void setExaminationType(ExaminationTypeEntity examinationType) {
        this.examinationType = examinationType;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, examinationType, time, description, conclusion);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final ExaminationEntity other = (ExaminationEntity) obj;
        return Objects.equals(other.getId(), id)
                && Objects.equals(other.getUser(), user)
                && Objects.equals(other.getExaminationType(), examinationType)
                && Objects.equals(other.getDescription(), description)
                && Objects.equals(other.getConclusion(), conclusion)
                && Objects.equals(other.getTime(), time);
    }
}
