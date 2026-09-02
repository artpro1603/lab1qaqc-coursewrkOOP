package ua.opnu.labwork21.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.opnu.labwork21.dto.response.BookingResponse;
import ua.opnu.labwork21.dto.response.FlightResponse;
import ua.opnu.labwork21.dto.response.PassengerResponse;
import ua.opnu.labwork21.service.BookingService;
import ua.opnu.labwork21.service.FlightService;
import ua.opnu.labwork21.service.PassengerService;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final FlightService flightService;
    private final PassengerService passengerService;
    private final BookingService bookingService;

    public SearchController(FlightService flightService,
                            PassengerService passengerService,
                            BookingService bookingService) {
        this.flightService = flightService;
        this.passengerService = passengerService;
        this.bookingService = bookingService;
    }

    @GetMapping("/flights")
    public ResponseEntity<List<FlightResponse>> searchFlights(@RequestParam String query) {
        return ResponseEntity.ok(flightService.search(query));
    }

    @GetMapping("/passengers")
    public ResponseEntity<List<PassengerResponse>> searchPassengers(@RequestParam String query) {
        return ResponseEntity.ok(passengerService.search(query));
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingResponse>> searchBookings(@RequestParam String query) {
        return ResponseEntity.ok(bookingService.getAll().stream()
            .filter(b -> b.status().name().toLowerCase().contains(query.toLowerCase()))
            .toList());
    }
}
