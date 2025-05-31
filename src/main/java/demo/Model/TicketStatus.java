package demo.Model;

public enum TicketStatus {
    InProcess,
    Answered;

    public String getStatus() {
        return this.name();
    }
}
