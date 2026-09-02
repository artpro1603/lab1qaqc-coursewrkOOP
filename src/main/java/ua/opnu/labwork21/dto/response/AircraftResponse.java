package ua.opnu.labwork21.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Дані повітряного судна у відповіді сервера")
public record AircraftResponse(
    @Schema(description = "Унікальний ідентифікатор судна", example = "1")
    Long id,

    @Schema(description = "Модель повітряного судна", example = "Boeing 737-800")
    String model,

    @Schema(description = "Пасажиромісткість", example = "189")
    Integer capacity,

    @Schema(description = "Виробник", example = "Boeing")
    String manufacturer
) {}
