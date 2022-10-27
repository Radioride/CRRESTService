package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.UUID.randomUUID;

public class BookService {
    private int total_rows;
    private int total_columns;
    private List<Seat> available_seats = new ArrayList<Seat>();
    private ConcurrentMap<String, Seat> available_seatsHasp = new ConcurrentHashMap<>();
    private ConcurrentMap<UUID, Ticket> tickets = new ConcurrentHashMap<>();
    public BookService() {
        this(9,9);
    }

    public BookService(int total_rows, int total_columns) {
        this.total_rows = total_rows;
        this.total_columns = total_columns;
        for(int row = 1; row <= this.total_rows; row++) {
            for(int col = 1; col <= this.total_columns; col++) {
                addSeat(row,col);
            }
        }
    }

    private Seat addSeat(int row, int column) {
        Seat tmpSeat = new Seat(row, column,row <= 4 ? 10 : 8);
        this.available_seats.add(tmpSeat);
        this.available_seatsHasp.put("" + row + column,tmpSeat);
        return  tmpSeat;
    }

    private int getCurrentIncome() {
        int tmpCount = 0;
        for (Ticket ticket : tickets.values()) {
            tmpCount += ticket.getTicket().getPrice();
        }
        return tmpCount;
    }

    public ResponseEntity<BaseBook> purchase(int row, int column) {
        String tmpID = "" + row + column;
        if (row > 0 && row <= total_rows && column > 0 && column <= total_columns) {
            if (this.available_seatsHasp.containsKey(tmpID)) {
                Seat tmpSeat = this.available_seatsHasp.get(tmpID);
                this.available_seats.remove(tmpSeat);
                this.available_seatsHasp.remove(tmpID);
                Ticket tmpTicket = new Ticket(randomUUID(),tmpSeat);
                this.tickets.put(tmpTicket.getToken(),tmpTicket);
                return new ResponseEntity<>(tmpTicket, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new BookError("The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new BookError("The number of a row or a column is out of bounds!"), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<BaseBook> refund(UUID token) {
        if (this.tickets.containsKey(token)) {
            Ticket tmpTicket = this.tickets.get(token);
            Seat tmpSeat = tmpTicket.getTicket();
            this.tickets.remove(token);
            this.available_seats.add(tmpSeat);
            this.available_seatsHasp.put("" + tmpSeat.getRow() + tmpSeat.getColumn(),tmpSeat);
            return new ResponseEntity<>(new RefundTicket(tmpSeat), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new BookError("Wrong token!"), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<BaseBook> stats(String password) {
        if ("super_secret".equals(password)) {
            return new ResponseEntity<>(new BookStats(getCurrentIncome(),this.available_seats.size(),this.tickets.size()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new BookError("The password is wrong!"), HttpStatus.UNAUTHORIZED);
        }
    }

    public int getTotal_rows() {
        return total_rows;
    }

    public void setTotal_rows(int total_rows) {
        this.total_rows = total_rows;
    }

    public int getTotal_columns() {
        return total_columns;
    }

    public void setTotal_columns(int total_columns) {
        this.total_columns = total_columns;
    }

    public List<Seat> getAvailable_seats() {
        return available_seats;
    }
}
