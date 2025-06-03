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

@Entity
@Table(name = "protocolParametres")
public class ProtocolParametresEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "parametersEntity", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ParametresEntity parameters;
    @ManyToOne
    @JoinColumn(name = "examinationEntity", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ExaminationEntity examination;
    @Column(length = 20)
    private String body;

    public ProtocolParametresEntity() {
    }

    public ProtocolParametresEntity(ParametresEntity parameters, ExaminationEntity examination, String body) {
        this.parameters = parameters;
        this.body = body;
        this.examination = examination;
    }

    public ParametresEntity getParameters() {
        return parameters;
    }

    public void setParameters(ParametresEntity parametres) {
        this.parameters = parametres;
    }

    public ExaminationEntity getExamination() {
        return examination;
    }

    public void setExamination(ExaminationEntity examination) {
        this.examination = examination;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parameters, body);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final ProtocolParametresEntity other = (ProtocolParametresEntity) obj;
        return Objects.equals(other.getId(), id)
                && Objects.equals(other.getParameters(),
                        parameters)
                && Objects.equals(other.getBody(),
                        body);
    }
}
