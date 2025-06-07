package demo.Model;

import java.util.Objects;

import demo.core.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Column(nullable = false)
    @Size(min = 4, max = 50)
    private String fullname;
    @Column(nullable = false)
    @Size(min = 5, max = 30)
    private String email;
    @Column(nullable = false)
    @Size(min = 5)
    private String password;
    @Column(nullable = false)
    private UserRole role;

    public UserEntity() {
    }

    public UserEntity(String fullname, String email, String password) {

        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.role = UserRole.USER;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullname, email, password);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final UserEntity other = (UserEntity) obj;
        return Objects.equals(other.getId(), id)
                && Objects.equals(other.getFullname(), fullname)
                && Objects.equals(other.getEmail(), email)
                && Objects.equals(other.getPassword(), password);
    }
}