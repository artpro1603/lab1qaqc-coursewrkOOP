package ua.opnu.labwork21.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Дані класу обслуговування у відповіді сервера")
public record SeatClassResponse(
    @Schema(description = "Унікальний ідентифікатор класу", example = "1")
    Long id,

    @Schema(description = "Назва класу обслуговування", example = "Бізнес-клас")
    String name,

    @Schema(description = "Опис класу обслуговування", example = "Широкі крісла, безкоштовне харчування, пріоритетна посадка.")
    String description
) {}
