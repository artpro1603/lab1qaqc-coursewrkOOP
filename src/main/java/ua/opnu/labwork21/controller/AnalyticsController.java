package ua.opnu.labwork21.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.opnu.labwork21.dto.response.FlightResponse;
import ua.opnu.labwork21.service.AircraftService;
import ua.opnu.labwork21.service.BookingService;
import ua.opnu.labwork21.service.FlightService;
import ua.opnu.labwork21.service.PassengerService;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final FlightService flightService;
    private final PassengerService passengerService;
    private final BookingService bookingService;
    private final AircraftService aircraftService;

    public AnalyticsController(FlightService flightService,
                                PassengerService passengerService,
                                BookingService bookingService,
                                AircraftService aircraftService) {
        this.flightService = flightService;
        this.passengerService = passengerService;
        this.bookingService = bookingService;
        this.aircraftService = aircraftService;
    }

    @GetMapping("/flights/count")
    public ResponseEntity<Long> flightsCount() {
        return ResponseEntity.ok((long) flightService.getAll().size());
    }

    @GetMapping("/passengers/count")
    public ResponseEntity<Long> passengersCount() {
        return ResponseEntity.ok((long) passengerService.getAll().size());
    }

    @GetMapping("/bookings/active")
    public ResponseEntity<Long> activeBookings() {
        return ResponseEntity.ok(bookingService.countActive());
    }

    @GetMapping("/flights/popular")
    public ResponseEntity<List<Map<String, Object>>> popularFlights() {
        List<Map<String, Object>> result = flightService.getAll().stream()
            .sorted(Comparator.comparingInt(
                (FlightResponse f) -> flightService.getPassengers(f.id()) == null
                    ? 0 : flightService.getPassengers(f.id()).size()
            ).reversed())
            .limit(5)
            .map(f -> {
                Map<String, Object> m = new HashMap<>();
                m.put("flightNumber", f.flightNumber());
                m.put("from", f.departureCity());
                m.put("to", f.arrivalCity());
                m.put("passengerCount",
                    flightService.getPassengers(f.id()) == null ? 0
                        : flightService.getPassengers(f.id()).size());
                return m;
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/aircraft/workload")
    public ResponseEntity<List<Map<String, Object>>> workload() {
        List<Map<String, Object>> result = aircraftService.getAll().stream()
            .map(a -> {
                Map<String, Object> m = new HashMap<>();
                m.put("model", a.model());
                m.put("manufacturer", a.manufacturer());
                m.put("flightCount", aircraftService.getFlightsByAircraft(a.id()).size());
                return m;
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
