package ua.opnu.labwork21.service;

import org.springframework.stereotype.Service;
import ua.opnu.labwork21.dto.request.AircraftCreateRequest;
import ua.opnu.labwork21.dto.request.AircraftUpdateRequest;
import ua.opnu.labwork21.dto.response.AircraftResponse;
import ua.opnu.labwork21.dto.response.FlightResponse;
import ua.opnu.labwork21.exception.ConflictOperationException;
import ua.opnu.labwork21.exception.ResourceNotFoundException;
import ua.opnu.labwork21.model.Aircraft;
import ua.opnu.labwork21.model.Flight;
import ua.opnu.labwork21.repository.AircraftRepository;
import ua.opnu.labwork21.repository.FlightRepository;

import java.util.List;

@Service
public class AircraftService {

    private final AircraftRepository aircraftRepository;
    private final FlightRepository flightRepository;

    public AircraftService(AircraftRepository aircraftRepository,
                           FlightRepository flightRepository) {
        this.aircraftRepository = aircraftRepository;
        this.flightRepository = flightRepository;
    }

    public AircraftResponse create(AircraftCreateRequest request) {
        Aircraft aircraft = new Aircraft();
        aircraft.setModel(request.model());
        aircraft.setCapacity(request.capacity());
        aircraft.setManufacturer(request.manufacturer());
        return toResponse(aircraftRepository.save(aircraft));
    }

    public List<AircraftResponse> getAll() {
        return aircraftRepository.findAll().stream().map(this::toResponse).toList();
    }

    public AircraftResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public AircraftResponse update(Long id, AircraftUpdateRequest request) {
        Aircraft aircraft = findById(id);
        aircraft.setModel(request.model());
        aircraft.setCapacity(request.capacity());
        aircraft.setManufacturer(request.manufacturer());
        return toResponse(aircraftRepository.save(aircraft));
    }

    public void delete(Long id) {
        Aircraft aircraft = findById(id);
        // Business rule 4: cannot delete aircraft with scheduled flights
        if (!flightRepository.findByAircraftId(id).isEmpty()) {
            throw new ConflictOperationException("Cannot delete aircraft with assigned flights");
        }
        aircraftRepository.delete(aircraft);
    }

    public List<FlightResponse> getFlightsByAircraft(Long aircraftId) {
        findById(aircraftId);
        return flightRepository.findByAircraftId(aircraftId).stream()
                .map(this::toFlightResponse)
                .toList();
    }

    private FlightResponse toFlightResponse(Flight flight) {
        return new FlightResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getDepartureCity(),
                flight.getArrivalCity(),
                flight.getDepartureTime(),
                flight.getAircraft() != null ? flight.getAircraft().getId() : null
        );
    }

    private Aircraft findById(Long id) {
        return aircraftRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found: " + id));
    }

    private AircraftResponse toResponse(Aircraft a) {
        return new AircraftResponse(a.getId(), a.getModel(), a.getCapacity(), a.getManufacturer());
    }
}
