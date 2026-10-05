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
import ua.opnu.labwork21.dto.request.AircraftCreateRequest;
import ua.opnu.labwork21.dto.request.AircraftUpdateRequest;
import ua.opnu.labwork21.dto.response.AircraftResponse;
import ua.opnu.labwork21.dto.response.FlightResponse;
import ua.opnu.labwork21.model.Flight;
import ua.opnu.labwork21.service.AircraftService;

import java.util.List;

@Tag(name = "Повітряні судна", description = "Управління парком повітряних суден авіакомпанії")
@RestController
@RequestMapping("/aircraft")
public class AircraftController {

    private final AircraftService aircraftService;

    public AircraftController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    @Operation(
        summary = "Додати повітряне судно",
        description = "Реєструє нове повітряне судно. Усі поля (модель, місткість, виробник) є обов'язковими."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Повітряне судно додано",
            content = @Content(schema = @Schema(implementation = AircraftResponse.class))),
        @ApiResponse(responseCode = "400", description = "Помилка валідації",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<AircraftResponse> create(@Valid @RequestBody AircraftCreateRequest request) {
        return ResponseEntity.ok(aircraftService.create(request));
    }

    @Operation(summary = "Отримати всі судна", description = "Повертає список усіх зареєстрованих повітряних суден.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список суден",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AircraftResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<AircraftResponse>> getAll() {
        return ResponseEntity.ok(aircraftService.getAll());
    }

    @Operation(summary = "Отримати судно за ID", description = "Повертає дані повітряного судна за його ідентифікатором.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Дані судна",
            content = @Content(schema = @Schema(implementation = AircraftResponse.class))),
        @ApiResponse(responseCode = "404", description = "Судно не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<AircraftResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(aircraftService.getById(id));
    }

    @Operation(summary = "Оновити дані судна", description = "Оновлює модель, місткість та виробника повітряного судна.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Судно оновлено",
            content = @Content(schema = @Schema(implementation = AircraftResponse.class))),
        @ApiResponse(responseCode = "400", description = "Помилка валідації",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Судно не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<AircraftResponse> update(@PathVariable Long id,
                                                    @Valid @RequestBody AircraftUpdateRequest request) {
        return ResponseEntity.ok(aircraftService.update(id, request));
    }

    @Operation(
        summary = "Видалити судно",
        description = """
            Видаляє повітряне судно з системи.
            Бізнес-правила:
            - Неможливо видалити судно, якщо до нього призначено рейси.
            """
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Судно видалено"),
        @ApiResponse(responseCode = "404", description = "Судно не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Неможливо видалити судно з призначеними рейсами",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        aircraftService.delete(id);
        return ResponseEntity.ok("Aircraft deleted " + id);
    }

    @Operation(summary = "Рейси судна", description = "Повертає список рейсів, призначених для вказаного повітряного судна.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список рейсів"),
        @ApiResponse(responseCode = "404", description = "Судно не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}/flights")
    public ResponseEntity<List<FlightResponse>> getFlights(@PathVariable Long id) {
        return ResponseEntity.ok(aircraftService.getFlightsByAircraft(id));
    }
}
