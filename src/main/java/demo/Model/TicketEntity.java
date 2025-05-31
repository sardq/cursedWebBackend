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
@Table(name = "tickets")
public class TicketEntity extends BaseEntity {
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserId", nullable = false)
    private UserEntity user;
    @Column(nullable = false, length = 100)
    private String message;
    @Column(nullable = false, length = 100)
    private String answer;
    private TicketStatus status;

    public TicketEntity() {
        super();
    }

    public TicketEntity(UserEntity user, String message, String answer) {
        this.user = user;
        this.message = message;
        this.answer = answer;
        this.status = TicketStatus.InProcess;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAnswer() {
        return answer;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, message, answer);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final TicketEntity other = (TicketEntity) obj;
        return Objects.equals(other.getId(), id)
                && Objects.equals(other.getUser(), user)
                && Objects.equals(other.getMessage(), message)
                && Objects.equals(other.getAnswer(), answer);
    }

}
