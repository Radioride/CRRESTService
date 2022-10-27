package cinema;

public class RefundTicket extends BaseBook {
    private Seat returned_ticket;

    public RefundTicket(Seat returned_ticket) {
        this.returned_ticket = returned_ticket;
    }

    public Seat getReturned_ticket() {
        return returned_ticket;
    }

    public void setReturned_ticket(Seat returned_ticket) {
        this.returned_ticket = returned_ticket;
    }
}
