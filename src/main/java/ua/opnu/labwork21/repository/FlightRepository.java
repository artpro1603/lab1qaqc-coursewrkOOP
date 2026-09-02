package ua.opnu.labwork21.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.opnu.labwork21.model.Flight;

import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByAircraftId(Long aircraftId);

    List<Flight> findByFlightNumberContainingIgnoreCaseOrDepartureCityContainingIgnoreCaseOrArrivalCityContainingIgnoreCase(
        String flightNumber, String departureCity, String arrivalCity);

    boolean existsByFlightNumber(String flightNumber);
    Optional<Flight> findByFlightNumber(String flightNumber);
}
