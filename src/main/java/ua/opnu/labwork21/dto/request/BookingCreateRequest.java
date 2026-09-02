package ua.opnu.labwork21.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import ua.opnu.labwork21.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Дані для створення нового бронювання")
public record BookingCreateRequest(

    @Schema(description = "ID пасажира, який здійснює бронювання", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Passenger ID is required")
    Long passengerId,

    @Schema(description = "Список ID рейсів (мінімум один)", example = "[1, 2]", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Flight IDs are required")
    @Size(min = 1, message = "At least one flight is required")
    List<Long> flightIds,

    @Schema(description = "Дата та час бронювання", example = "2026-07-15T14:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Booking date is required")
    LocalDateTime bookingDate,

    @Schema(description = "Початковий статус бронювання (CREATED або CONFIRMED; CANCELLED — неприпустимий)", example = "CREATED", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Status is required")
    BookingStatus status
) {}
