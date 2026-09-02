package ua.opnu.labwork21.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Стандартна відповідь сервера при виникненні помилки")
public record ApiErrorResponse(
    @Schema(description = "Час виникнення помилки", example = "2026-06-03T12:00:00")
    LocalDateTime timestamp,

    @Schema(description = "HTTP-код статусу", example = "400")
    int status,

    @Schema(description = "Текстовий опис HTTP-статусу", example = "Bad Request")
    String error,

    @Schema(description = "Детальне повідомлення про помилку", example = "Departure time must be in the future")
    String message,

    @Schema(description = "Шлях запиту, що спричинив помилку", example = "/flights")
    String path,

    @Schema(description = "Карта помилок валідації (поле → повідомлення). Присутня лише при 400 Validation failed.",
            example = "{\"email\": \"Email must be valid\", \"firstName\": \"First name is required\"}")
    Map<String, String> errors
) {}
