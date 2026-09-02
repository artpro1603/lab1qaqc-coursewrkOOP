package ua.opnu.labwork21.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.opnu.labwork21.model.SeatClass;

public interface SeatClassRepository extends JpaRepository<SeatClass, Long> {
}