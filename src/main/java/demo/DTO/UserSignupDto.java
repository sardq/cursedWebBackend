package demo.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserSignupDto {
    @NotBlank
    @Size(min = 3, max = 20)
    private String fullname;
    @NotBlank
    @Size(min = 3, max = 20)
    private String email;
    @NotBlank
    @Size(min = 3, max = 20)
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
