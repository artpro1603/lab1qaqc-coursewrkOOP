package ua.opnu.labwork21.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import ua.opnu.labwork21.model.BookingStatus;

import java.time.LocalDateTime;

@Schema(description = "Дані для оновлення бронювання")
public record BookingUpdateRequest(

    @Schema(description = "Нова дата та час бронювання", example = "2026-08-01T09:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Booking date is required")
    LocalDateTime bookingDate,

    @Schema(description = "Новий статус бронювання", example = "CONFIRMED", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Status is required")
    BookingStatus status
) {}
