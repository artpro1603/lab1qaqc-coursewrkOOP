package ua.opnu.labwork21.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ua.opnu.labwork21.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Дані бронювання у відповіді сервера")
public record BookingResponse(
    @Schema(description = "Унікальний ідентифікатор бронювання", example = "1")
    Long id,

    @Schema(description = "Дата та час бронювання", example = "2026-07-15T14:00:00")
    LocalDateTime bookingDate,

    @Schema(description = "Поточний статус бронювання", example = "CONFIRMED")
    BookingStatus status,

    @Schema(description = "ID пасажира, якому належить бронювання", example = "1")
    Long passengerId,

    @Schema(description = "Список ID рейсів, на які здійснено бронювання", example = "[1, 2]")
    List<Long> flightIds
) {}
