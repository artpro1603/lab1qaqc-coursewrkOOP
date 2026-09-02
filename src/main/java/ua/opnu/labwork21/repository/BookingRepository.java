package ua.opnu.labwork21.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.opnu.labwork21.model.Booking;
import ua.opnu.labwork21.model.BookingStatus;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByPassengerId(Long passengerId);
    long countByStatus(BookingStatus status);

    List<Booking> findByStatusContaining(String query);
}