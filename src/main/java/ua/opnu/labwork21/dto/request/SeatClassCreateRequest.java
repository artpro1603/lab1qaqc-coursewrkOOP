package ua.opnu.labwork21.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Дані для створення нового класу обслуговування")
public record SeatClassCreateRequest(

    @Schema(description = "Назва класу обслуговування", example = "Бізнес-клас", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 150, message = "Name must be between 2 and 150 characters")
    String name,

    @Schema(description = "Детальний опис класу обслуговування (переваги, послуги)", example = "Широкі крісла, безкоштовне харчування, пріоритетна посадка та збільшений простір для ніг.", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    String description
) {}
