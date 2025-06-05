package demo.Model;

import java.util.Objects;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import demo.core.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "parametres")
public class ParametresEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "examinationTypeEntity", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ExaminationTypeEntity examinationType;
    @Column(nullable = false)
    @Size(min = 3, max = 20)
    private String name;

    public ParametresEntity() {
    }

    public ParametresEntity(ExaminationTypeEntity examinationType, String name) {
        this.examinationType = examinationType;
        this.name = name;
    }

    public ExaminationTypeEntity getExaminationType() {
        return examinationType;
    }

    public void setExaminationType(ExaminationTypeEntity examinationType) {
        this.examinationType = examinationType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, examinationType, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final ParametresEntity other = (ParametresEntity) obj;
        return Objects.equals(other.getId(), id)
                && Objects.equals(other.getExaminationType(), examinationType)
                && Objects.equals(other.getName(), name);
    }
}
