package demo.DTO;

public class ErrorDto {
    private String message;

    // Конструктор по умолчанию (необходим для Jackson)
    public ErrorDto() {
    }

    // Конструктор с сообщением
    public ErrorDto(String message) {
        this.message = message;
    }

    // Геттер и сеттер
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}