package demo.Model;

import java.util.Objects;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.http.MediaType;

import demo.core.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "medias")
public class MediaEntity extends BaseEntity {
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "examinationEntity", nullable = false)
    private ExaminationEntity examination;
    @Column
    private MediaType resource;

    public MediaEntity() {
    }

    public MediaEntity(ExaminationEntity examination, MediaType resource) {
        this.examination = examination;
        this.resource = resource;

    }

    public ExaminationEntity getExamination() {
        return examination;
    }

    public void setExamination(ExaminationEntity examination) {
        this.examination = examination;
    }

    public MediaType getResource() {
        return resource;
    }

    public void setResource(MediaType resource) {
        this.resource = resource;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, examination, resource);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final MediaEntity other = (MediaEntity) obj;
        return Objects.equals(other.getId(), id)
                && Objects.equals(other.getResource(), resource)
                && Objects.equals(other.getExamination(), examination);
    }
}
