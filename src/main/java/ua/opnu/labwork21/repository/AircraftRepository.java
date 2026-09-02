package ua.opnu.labwork21.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.opnu.labwork21.model.Aircraft;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
}