package cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
class SeatsInfoController {
    BookService bookService = new BookService();

    @GetMapping("/seats")
    public BookService getSeats() {
        return bookService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<BaseBook> purchaseSeat(@RequestBody Seat seat) {
        return bookService.purchase(seat.getRow(), seat.getColumn());
    }

    @PostMapping("/return")
    public ResponseEntity<BaseBook> refundTicket(@RequestBody Ticket ticket) {
        return bookService.refund(ticket.getToken());
    }

    @PostMapping("/stats")
    public ResponseEntity<BaseBook> getStats(@RequestParam Optional<String> password) {
        return bookService.stats(password.isEmpty() ? "" : password.get());
    }
}

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}