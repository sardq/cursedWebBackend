package demo.DTO;

import jakarta.validation.constraints.NotBlank;

public class NotificationDto {
    @NotBlank
    private String message;
    @NotBlank
    private String header;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
