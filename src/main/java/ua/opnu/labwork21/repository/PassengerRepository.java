package ua.opnu.labwork21.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.opnu.labwork21.model.Passenger;

import java.util.List;
import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    List<Passenger> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
        String firstName, String lastName, String email);

    boolean existsByEmail(String email);
    boolean existsByPassportNumber(String passportNumber);
    Optional<Passenger> findByEmail(String email);
}
