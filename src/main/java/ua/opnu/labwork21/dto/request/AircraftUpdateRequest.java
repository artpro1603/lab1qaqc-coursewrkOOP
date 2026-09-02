package ua.opnu.labwork21.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Дані для оновлення повітряного судна")
public record AircraftUpdateRequest(

    @Schema(description = "Модель повітряного судна", example = "Airbus A320", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Model is required")
    @Size(min = 2, max = 150, message = "Model must be between 2 and 150 characters")
    String model,

    @Schema(description = "Пасажиромісткість судна", example = "150", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be a positive number")
    Integer capacity,

    @Schema(description = "Виробник повітряного судна", example = "Airbus", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Manufacturer is required")
    @Size(min = 2, max = 150, message = "Manufacturer must be between 2 and 150 characters")
    String manufacturer
) {}
