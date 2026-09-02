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
import ua.opnu.labwork21.dto.request.FlightCreateRequest;
import ua.opnu.labwork21.dto.request.FlightUpdateRequest;
import ua.opnu.labwork21.dto.response.FlightResponse;
import ua.opnu.labwork21.model.Passenger;
import ua.opnu.labwork21.service.FlightService;

import java.util.List;

@Tag(name = "Рейси", description = "Управління авіарейсами: створення, оновлення, видалення та перегляд пасажирів рейсу")
@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @Operation(
        summary = "Створити рейс",
        description = """
            Реєструє новий авіарейс у системі.
            Бізнес-правила:
            - Номер рейсу (flightNumber) повинен бути унікальним.
            - Час відправлення повинен бути в майбутньому.
            - Якщо вказано aircraftId — відповідне повітряне судно повинно існувати.
            """
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Рейс успішно створено",
            content = @Content(schema = @Schema(implementation = FlightResponse.class))),
        @ApiResponse(responseCode = "400", description = "Некоректні дані або час відправлення в минулому",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Повітряне судно не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Рейс з таким номером вже існує",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<FlightResponse> create(@Valid @RequestBody FlightCreateRequest request) {
        return ResponseEntity.ok(flightService.create(request));
    }

    @Operation(summary = "Отримати всі рейси", description = "Повертає список усіх зареєстрованих рейсів.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список рейсів",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = FlightResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<FlightResponse>> getAll() {
        return ResponseEntity.ok(flightService.getAll());
    }

    @Operation(summary = "Отримати рейс за ID", description = "Повертає деталі рейсу за його ідентифікатором.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Дані рейсу",
            content = @Content(schema = @Schema(implementation = FlightResponse.class))),
        @ApiResponse(responseCode = "404", description = "Рейс не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getById(id));
    }

    @Operation(
        summary = "Оновити рейс",
        description = """
            Оновлює дані існуючого рейсу.
            Бізнес-правила:
            - Час відправлення повинен бути в майбутньому.
            - Номер рейсу повинен залишатися унікальним (виключаючи поточний рейс).
            """
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Рейс оновлено",
            content = @Content(schema = @Schema(implementation = FlightResponse.class))),
        @ApiResponse(responseCode = "400", description = "Некоректні дані",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Рейс або повітряне судно не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Номер рейсу вже використовується",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<FlightResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody FlightUpdateRequest request) {
        return ResponseEntity.ok(flightService.update(id, request));
    }

    @Operation(
        summary = "Видалити рейс",
        description = """
            Видаляє рейс з системи.
            Бізнес-правила:
            - Неможливо видалити рейс, якщо є активні бронювання (статус CREATED або CONFIRMED).
            """
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Рейс видалено"),
        @ApiResponse(responseCode = "404", description = "Рейс не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Неможливо видалити рейс з активними бронюваннями",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        flightService.delete(id);
        return ResponseEntity.ok("Flight deleted " + id);
    }

    @Operation(summary = "Рейси за повітряним судном", description = "Повертає всі рейси, призначені для вказаного повітряного судна.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список рейсів",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = FlightResponse.class))))
    })
    @GetMapping("/aircraft/{aircraftId}")
    public ResponseEntity<List<FlightResponse>> getByAircraft(@PathVariable Long aircraftId) {
        return ResponseEntity.ok(flightService.getByAircraftId(aircraftId));
    }

    @Operation(summary = "Пасажири рейсу", description = "Повертає список пасажирів, що летять даним рейсом.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список пасажирів"),
        @ApiResponse(responseCode = "404", description = "Рейс не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}/passengers")
    public ResponseEntity<List<Passenger>> getPassengers(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getPassengers(id));
    }
}
