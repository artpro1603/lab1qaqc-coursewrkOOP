package ua.opnu.labwork21.service;

import org.springframework.stereotype.Service;
import ua.opnu.labwork21.dto.request.BookingCreateRequest;
import ua.opnu.labwork21.dto.request.BookingUpdateRequest;
import ua.opnu.labwork21.dto.response.BookingResponse;
import ua.opnu.labwork21.exception.BadRequestException;
import ua.opnu.labwork21.exception.ConflictOperationException;
import ua.opnu.labwork21.exception.ResourceNotFoundException;
import ua.opnu.labwork21.model.*;
import ua.opnu.labwork21.repository.BookingRepository;
import ua.opnu.labwork21.repository.FlightRepository;
import ua.opnu.labwork21.repository.PassengerRepository;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final FlightRepository flightRepository;

    public BookingService(BookingRepository bookingRepository,
                          PassengerRepository passengerRepository,
                          FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.flightRepository = flightRepository;
    }

    public BookingResponse create(BookingCreateRequest request) {
        // Business rule 2: passenger must exist
        Passenger passenger = passengerRepository.findById(request.passengerId())
            .orElseThrow(() -> new ResourceNotFoundException("Passenger not found: " + request.passengerId()));

        // Business rule 2: all flights must exist
        List<Flight> flights = request.flightIds().stream()
            .map(fid -> flightRepository.findById(fid)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found: " + fid)))
            .toList();

        // Business rule 5: booking date must not be null or in the past
        if (request.bookingDate() == null) {
            throw new BadRequestException("Booking date is required");
        }

        // Business rule 3: no duplicate active booking for same passenger+flight
        for (Flight flight : flights) {
            boolean duplicate = bookingRepository.findByPassengerId(passenger.getId()).stream()
                .filter(b -> b.getStatus() == BookingStatus.CREATED || b.getStatus() == BookingStatus.CONFIRMED)
                .anyMatch(b -> b.getFlights().stream().anyMatch(f -> f.getId().equals(flight.getId())));
            if (duplicate) {
                throw new ConflictOperationException(
                    "Passenger already has an active booking for flight " + flight.getFlightNumber());
            }
        }

        // Business rule 5: status must be valid initial value
        if (request.status() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Cannot create a booking with CANCELLED status");
        }

        Booking booking = new Booking();
        booking.setPassenger(passenger);
        booking.setFlights(flights);
        booking.setBookingDate(request.bookingDate());
        booking.setStatus(request.status());

        return toResponse(bookingRepository.save(booking));
    }

    public List<BookingResponse> getAll() {
        return bookingRepository.findAll().stream().map(this::toResponse).toList();
    }

    public BookingResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public BookingResponse update(Long id, BookingUpdateRequest request) {
        Booking booking = findById(id);

        // Business rule 5: cannot reactivate cancelled booking
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ConflictOperationException("Cannot update a cancelled booking");
        }

        booking.setBookingDate(request.bookingDate());
        booking.setStatus(request.status());
        return toResponse(bookingRepository.save(booking));
    }

    public void delete(Long id) {
        Booking booking = findById(id);
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    public List<BookingResponse> getByPassengerId(Long passengerId) {
        if (!passengerRepository.existsById(passengerId)) {
            throw new ResourceNotFoundException("Passenger not found: " + passengerId);
        }
        return bookingRepository.findByPassengerId(passengerId).stream().map(this::toResponse).toList();
    }

    public long countActive() {
        return bookingRepository.countByStatus(BookingStatus.CONFIRMED);
    }

    private Booking findById(Long id) {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + id));
    }

    private BookingResponse toResponse(Booking b) {
        List<Long> flightIds = b.getFlights() == null ? List.of() :
            b.getFlights().stream().map(Flight::getId).toList();
        return new BookingResponse(
            b.getId(),
            b.getBookingDate(),
            b.getStatus(),
            b.getPassenger().getId(),
            flightIds
        );
    }
}
