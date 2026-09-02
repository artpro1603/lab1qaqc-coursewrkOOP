package ua.opnu.labwork21.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Дані для оновлення класу обслуговування")
public record SeatClassUpdateRequest(

    @Schema(description = "Назва класу обслуговування", example = "Економ-клас", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 150, message = "Name must be between 2 and 150 characters")
    String name,

    @Schema(description = "Детальний опис класу обслуговування", example = "Стандартні крісла з достатнім простором, безкоштовний ручний багаж до 7 кг.", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    String description
) {}
