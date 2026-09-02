package ua.opnu.labwork21.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.opnu.labwork21.dto.ApiErrorResponse;
import ua.opnu.labwork21.dto.request.PassengerCreateRequest;
import ua.opnu.labwork21.dto.request.PassengerUpdateRequest;
import ua.opnu.labwork21.dto.response.PassengerResponse;
import ua.opnu.labwork21.model.Booking;
import ua.opnu.labwork21.service.PassengerService;

import java.util.List;

@Tag(name = "Пасажири", description = "Управління профілями пасажирів: реєстрація, пошук, оновлення та видалення")
@RestController
@RequestMapping("/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @Operation(
        summary = "Створити пасажира",
        description = """
            Реєструє нового пасажира в системі.
            Бізнес-правила:
            - Email повинен бути унікальним серед усіх пасажирів.
            - Номер паспорта повинен бути унікальним серед усіх пасажирів.
            - Ім'я та прізвище — від 2 до 50 символів.
            """
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пасажира успішно створено",
            content = @Content(schema = @Schema(implementation = PassengerResponse.class))),
        @ApiResponse(responseCode = "400", description = "Помилка валідації вхідних даних",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Пасажир з таким email або номером паспорта вже існує",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<PassengerResponse> create(@Valid @RequestBody PassengerCreateRequest request) {
        return ResponseEntity.ok(passengerService.create(request));
    }

    @Operation(summary = "Отримати всіх пасажирів", description = "Повертає список усіх зареєстрованих пасажирів.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список пасажирів",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PassengerResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<PassengerResponse>> getAll() {
        return ResponseEntity.ok(passengerService.getAll());
    }

    @Operation(summary = "Отримати пасажира за ID", description = "Повертає дані пасажира за його ідентифікатором.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Дані пасажира",
            content = @Content(schema = @Schema(implementation = PassengerResponse.class))),
        @ApiResponse(responseCode = "404", description = "Пасажира не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(passengerService.getById(id));
    }

    @Operation(
        summary = "Оновити дані пасажира",
        description = """
            Оновлює дані існуючого пасажира.
            Бізнес-правила:
            - Email повинен залишатися унікальним (виключаючи поточного пасажира).
            - Усі поля є обов'язковими.
            """
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Дані пасажира оновлено",
            content = @Content(schema = @Schema(implementation = PassengerResponse.class))),
        @ApiResponse(responseCode = "400", description = "Помилка валідації",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Пасажира не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Email вже використовується іншим пасажиром",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<PassengerResponse> update(@PathVariable Long id,
                                                     @Valid @RequestBody PassengerUpdateRequest request) {
        return ResponseEntity.ok(passengerService.update(id, request));
    }

    @Operation(
        summary = "Видалити пасажира",
        description = """
            Видаляє пасажира з системи.
            Бізнес-правила:
            - Неможливо видалити пасажира, який має активні бронювання (статус CREATED або CONFIRMED).
            """
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пасажира видалено"),
        @ApiResponse(responseCode = "404", description = "Пасажира не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Неможливо видалити пасажира з активними бронюваннями",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        passengerService.delete(id);
        return ResponseEntity.ok("Passenger with id " + id + " deleted");
    }

    @Operation(summary = "Бронювання пасажира", description = "Повертає список усіх бронювань вказаного пасажира.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список бронювань пасажира"),
        @ApiResponse(responseCode = "404", description = "Пасажира не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<Booking>> getBookings(@PathVariable Long id) {
        return ResponseEntity.ok(passengerService.getBookings(id));
    }
}
