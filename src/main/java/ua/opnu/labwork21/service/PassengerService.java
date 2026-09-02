package ua.opnu.labwork21.service;

import org.springframework.stereotype.Service;
import ua.opnu.labwork21.dto.request.PassengerCreateRequest;
import ua.opnu.labwork21.dto.request.PassengerUpdateRequest;
import ua.opnu.labwork21.dto.response.PassengerResponse;
import ua.opnu.labwork21.exception.ConflictOperationException;
import ua.opnu.labwork21.exception.DuplicateResourceException;
import ua.opnu.labwork21.exception.ResourceNotFoundException;
import ua.opnu.labwork21.model.Booking;
import ua.opnu.labwork21.model.BookingStatus;
import ua.opnu.labwork21.model.Passenger;
import ua.opnu.labwork21.repository.BookingRepository;
import ua.opnu.labwork21.repository.PassengerRepository;

import java.util.List;

@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;

    public PassengerService(PassengerRepository passengerRepository,
                            BookingRepository bookingRepository) {
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
    }

    public PassengerResponse create(PassengerCreateRequest request) {
        // Business rule 1: email must be unique
        if (passengerRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Passenger with email '" + request.email() + "' already exists");
        }
        // Business rule: passport must be unique
        if (passengerRepository.existsByPassportNumber(request.passportNumber())) {
            throw new DuplicateResourceException("Passenger with passport '" + request.passportNumber() + "' already exists");
        }
        Passenger passenger = new Passenger();
        passenger.setFirstName(request.firstName());
        passenger.setLastName(request.lastName());
        passenger.setEmail(request.email());
        passenger.setPassportNumber(request.passportNumber());
        return toResponse(passengerRepository.save(passenger));
    }

    public List<PassengerResponse> getAll() {
        return passengerRepository.findAll().stream().map(this::toResponse).toList();
    }

    public PassengerResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public PassengerResponse update(Long id, PassengerUpdateRequest request) {
        Passenger passenger = findById(id);
        // Business rule 1: email uniqueness (excluding self)
        passengerRepository.findByEmail(request.email())
            .filter(p -> !p.getId().equals(id))
            .ifPresent(p -> { throw new DuplicateResourceException("Email already in use"); });

        passenger.setFirstName(request.firstName());
        passenger.setLastName(request.lastName());
        passenger.setEmail(request.email());
        passenger.setPassportNumber(request.passportNumber());
        return toResponse(passengerRepository.save(passenger));
    }

    public void delete(Long id) {
        Passenger passenger = findById(id);
        // Business rule 4: cannot delete passenger with active bookings
        boolean hasActive = bookingRepository.findByPassengerId(id).stream()
            .anyMatch(b -> b.getStatus() == BookingStatus.CONFIRMED || b.getStatus() == BookingStatus.CREATED);
        if (hasActive) {
            throw new ConflictOperationException("Cannot delete passenger with active bookings");
        }
        passengerRepository.delete(passenger);
    }

    public List<Booking> getBookings(Long passengerId) {
        findById(passengerId);
        return bookingRepository.findByPassengerId(passengerId);
    }

    public List<PassengerResponse> search(String query) {
        return passengerRepository
            .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                query, query, query)
            .stream().map(this::toResponse).toList();
    }

    private Passenger findById(Long id) {
        return passengerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Passenger not found: " + id));
    }

    private PassengerResponse toResponse(Passenger p) {
        return new PassengerResponse(p.getId(), p.getFirstName(), p.getLastName(), p.getEmail(), p.getPassportNumber());
    }
}
