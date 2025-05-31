package demo.Model;

import java.util.Objects;

import demo.core.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Column(nullable = false, length = 20)
    private String fullname;
    @Column(nullable = false, length = 20)
    private String email;
    @Column(nullable = true, length = 11)
    private String phone;
    @Column(nullable = false)
    private String password;
    private UserRole role;

    public UserEntity() {
    }

    public UserEntity(String fullname, String email, String password, String phone) {

        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullname, email, password, phone);
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
                && Objects.equals(other.getPhone(), phone)
                && Objects.equals(other.getEmail(), email)
                && Objects.equals(other.getPassword(), password);
    }
}