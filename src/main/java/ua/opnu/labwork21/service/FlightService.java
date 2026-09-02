package ua.opnu.labwork21.service;

import org.springframework.stereotype.Service;
import ua.opnu.labwork21.dto.request.FlightCreateRequest;
import ua.opnu.labwork21.dto.request.FlightUpdateRequest;
import ua.opnu.labwork21.dto.response.FlightResponse;
import ua.opnu.labwork21.exception.BadRequestException;
import ua.opnu.labwork21.exception.ConflictOperationException;
import ua.opnu.labwork21.exception.DuplicateResourceException;
import ua.opnu.labwork21.exception.ResourceNotFoundException;
import ua.opnu.labwork21.model.Aircraft;
import ua.opnu.labwork21.model.Flight;
import ua.opnu.labwork21.model.Passenger;
import ua.opnu.labwork21.model.SeatClass;
import ua.opnu.labwork21.repository.AircraftRepository;
import ua.opnu.labwork21.repository.FlightRepository;
import ua.opnu.labwork21.repository.SeatClassRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final SeatClassRepository seatClassRepository;
    private final AircraftRepository aircraftRepository;

    public FlightService(FlightRepository flightRepository,
                         SeatClassRepository seatClassRepository,
                         AircraftRepository aircraftRepository) {
        this.flightRepository = flightRepository;
        this.seatClassRepository = seatClassRepository;
        this.aircraftRepository = aircraftRepository;
    }

    public FlightResponse create(FlightCreateRequest request) {
        // Business rule: flight number must be unique
        if (flightRepository.existsByFlightNumber(request.flightNumber())) {
            throw new DuplicateResourceException("Flight with number '" + request.flightNumber() + "' already exists");
        }
        // Business rule 5: departure time must be in the future
        if (!request.departureTime().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Departure time must be in the future");
        }

        Flight flight = new Flight();
        flight.setFlightNumber(request.flightNumber());
        flight.setDepartureCity(request.departureCity());
        flight.setArrivalCity(request.arrivalCity());
        flight.setDepartureTime(request.departureTime());

        // Business rule 2: aircraft must exist if provided
        if (request.aircraftId() != null) {
            Aircraft aircraft = aircraftRepository.findById(request.aircraftId())
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found: " + request.aircraftId()));
            flight.setAircraft(aircraft);
        }

        return toResponse(flightRepository.save(flight));
    }

    public List<FlightResponse> getAll() {
        return flightRepository.findAll().stream().map(this::toResponse).toList();
    }

    public FlightResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public FlightResponse update(Long id, FlightUpdateRequest request) {
        Flight flight = findById(id);

        // Business rule 5
        if (!request.departureTime().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Departure time must be in the future");
        }

        // Business rule: flight number uniqueness (excluding self)
        flightRepository.findByFlightNumber(request.flightNumber())
            .filter(f -> !f.getId().equals(id))
            .ifPresent(f -> { throw new DuplicateResourceException("Flight number already in use"); });

        flight.setFlightNumber(request.flightNumber());
        flight.setDepartureCity(request.departureCity());
        flight.setArrivalCity(request.arrivalCity());
        flight.setDepartureTime(request.departureTime());

        if (request.aircraftId() != null) {
            Aircraft aircraft = aircraftRepository.findById(request.aircraftId())
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found: " + request.aircraftId()));
            flight.setAircraft(aircraft);
        }

        return toResponse(flightRepository.save(flight));
    }

    public void delete(Long id) {
        Flight flight = findById(id);
        // Business rule 4: cannot delete flight with active bookings
        boolean hasActiveBookings = flight.getBookings() != null && flight.getBookings().stream()
            .anyMatch(b -> b.getStatus() != null &&
                (b.getStatus().name().equals("CONFIRMED") || b.getStatus().name().equals("CREATED")));
        if (hasActiveBookings) {
            throw new ConflictOperationException("Cannot delete flight with active bookings");
        }
        flightRepository.delete(flight);
    }

    public List<FlightResponse> getByAircraftId(Long aircraftId) {
        return flightRepository.findByAircraftId(aircraftId).stream().map(this::toResponse).toList();
    }

    public List<Passenger> getPassengers(Long flightId) {
        return findById(flightId).getPassengers();
    }

    public Flight addSeatClass(Long flightId, Long seatClassId) {
        Flight flight = findById(flightId);
        SeatClass seatClass = seatClassRepository.findById(seatClassId)
            .orElseThrow(() -> new ResourceNotFoundException("SeatClass not found: " + seatClassId));
        // Business rule 3: no duplicate seat classes on same flight
        boolean alreadyAdded = flight.getSeatClasses() != null &&
            flight.getSeatClasses().stream().anyMatch(sc -> sc.getId().equals(seatClassId));
        if (alreadyAdded) {
            throw new ConflictOperationException("SeatClass already added to this flight");
        }
        flight.getSeatClasses().add(seatClass);
        return flightRepository.save(flight);
    }

    public Flight removeSeatClass(Long flightId, Long seatClassId) {
        Flight flight = findById(flightId);
        flight.getSeatClasses().removeIf(sc -> sc.getId().equals(seatClassId));
        return flightRepository.save(flight);
    }

    public List<FlightResponse> search(String query) {
        return flightRepository
            .findByFlightNumberContainingIgnoreCaseOrDepartureCityContainingIgnoreCaseOrArrivalCityContainingIgnoreCase(
                query, query, query)
            .stream().map(this::toResponse).toList();
    }

    private Flight findById(Long id) {
        return flightRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Flight not found: " + id));
    }

    private FlightResponse toResponse(Flight f) {
        return new FlightResponse(
            f.getId(),
            f.getFlightNumber(),
            f.getDepartureCity(),
            f.getArrivalCity(),
            f.getDepartureTime(),
            f.getAircraft() != null ? f.getAircraft().getId() : null
        );
    }
}
