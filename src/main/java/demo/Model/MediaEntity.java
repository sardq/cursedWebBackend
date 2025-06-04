package demo.Model;

import java.util.Objects;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import demo.core.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "media")
public class MediaEntity extends BaseEntity {
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "examinationEntity", nullable = false)
    private ExaminationEntity examination;
    private String filename;
    private String mimeType;
    private String bucket;
    private String objectName;

    public Long getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getBucket() {
        return bucket;
    }

    public String getObjectName() {
        return objectName;
    }

    public ExaminationEntity getExamination() {
        return examination;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public void setExamination(ExaminationEntity examination) {
        this.examination = examination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MediaEntity that = (MediaEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(filename, that.filename) &&
                Objects.equals(bucket,
                        that.bucket)
                &&
                Objects.equals(mimeType,
                        that.mimeType)
                &&
                Objects.equals(objectName, that.objectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, filename, objectName, mimeType, bucket);
    }

}