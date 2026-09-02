package ua.opnu.labwork21.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Дані авіарейсу у відповіді сервера")
public record FlightResponse(
    @Schema(description = "Унікальний ідентифікатор рейсу", example = "1")
    Long id,

    @Schema(description = "Номер рейсу", example = "PS101")
    String flightNumber,

    @Schema(description = "Місто відправлення", example = "Київ")
    String departureCity,

    @Schema(description = "Місто прибуття", example = "Варшава")
    String arrivalCity,

    @Schema(description = "Дата та час відправлення", example = "2026-09-01T10:30:00")
    LocalDateTime departureTime,

    @Schema(description = "ID прив'язаного повітряного судна (null якщо не призначено)", example = "2")
    Long aircraftId
) {}
