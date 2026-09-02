package ua.opnu.labwork21.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Дані пасажира у відповіді сервера")
public record PassengerResponse(
    @Schema(description = "Унікальний ідентифікатор пасажира", example = "1")
    Long id,

    @Schema(description = "Ім'я пасажира", example = "Олександр")
    String firstName,

    @Schema(description = "Прізвище пасажира", example = "Шевченко")
    String lastName,

    @Schema(description = "Електронна адреса пасажира", example = "o.shevchenko@email.com")
    String email,

    @Schema(description = "Номер паспорта пасажира", example = "FE123456")
    String passportNumber
) {}
