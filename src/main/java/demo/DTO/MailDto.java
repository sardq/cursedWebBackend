package demo.DTO;

import jakarta.validation.constraints.NotBlank;

public class MailDto {
    @NotBlank
    private String message;
    @NotBlank
    private String header;
    @NotBlank
    private String to;
    @NotBlank
    private String from;

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

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
