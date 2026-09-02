package ua.opnu.labwork21.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Schema(description = "Дані для створення нового авіарейсу")
public record FlightCreateRequest(

    @Schema(description = "Унікальний номер рейсу", example = "PS101", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Flight number is required")
    @Size(min = 2, max = 30, message = "Flight number must be between 2 and 30 characters")
    String flightNumber,

    @Schema(description = "Місто відправлення", example = "Київ", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Departure city is required")
    @Size(min = 2, max = 150, message = "Departure city must be between 2 and 150 characters")
    String departureCity,

    @Schema(description = "Місто прибуття", example = "Варшава", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Arrival city is required")
    @Size(min = 2, max = 150, message = "Arrival city must be between 2 and 150 characters")
    String arrivalCity,

    @Schema(description = "Дата та час відправлення (повинні бути в майбутньому)", example = "2026-09-01T10:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Departure time is required")
    @Future(message = "Departure time must be in the future")
    LocalDateTime departureTime,

    @Schema(description = "ID повітряного судна (необов'язково)", example = "1")
    Long aircraftId
) {}
