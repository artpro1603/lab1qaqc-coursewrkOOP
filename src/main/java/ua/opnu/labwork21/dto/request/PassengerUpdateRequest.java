package ua.opnu.labwork21.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Дані для оновлення профілю пасажира")
public record PassengerUpdateRequest(

    @Schema(description = "Ім'я пасажира", example = "Олександр", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    String firstName,

    @Schema(description = "Прізвище пасажира", example = "Шевченко", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    String lastName,

    @Schema(description = "Унікальна електронна адреса пасажира", example = "o.shevchenko@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must be at most 100 characters")
    String email,

    @Schema(description = "Унікальний номер паспорта пасажира", example = "FE123456", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Passport number is required")
    @Size(min = 2, max = 30, message = "Passport number must be between 2 and 30 characters")
    String passportNumber
) {}
