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
import ua.opnu.labwork21.dto.request.SeatClassCreateRequest;
import ua.opnu.labwork21.dto.request.SeatClassUpdateRequest;
import ua.opnu.labwork21.dto.response.SeatClassResponse;
import ua.opnu.labwork21.model.Flight;
import ua.opnu.labwork21.service.FlightService;
import ua.opnu.labwork21.service.SeatClassService;

import java.util.List;

@Tag(name = "Класи обслуговування", description = "Управління класами обслуговування (економ, бізнес тощо) та їх прив'язка до рейсів")
@RestController
public class SeatClassController {

    private final SeatClassService seatClassService;
    private final FlightService flightService;

    public SeatClassController(SeatClassService seatClassService, FlightService flightService) {
        this.seatClassService = seatClassService;
        this.flightService = flightService;
    }

    @Operation(summary = "Створити клас обслуговування", description = "Додає новий клас обслуговування (наприклад, «Економ», «Бізнес»).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Клас створено",
            content = @Content(schema = @Schema(implementation = SeatClassResponse.class))),
        @ApiResponse(responseCode = "400", description = "Помилка валідації",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/seat-classes")
    public ResponseEntity<SeatClassResponse> create(@Valid @RequestBody SeatClassCreateRequest request) {
        return ResponseEntity.ok(seatClassService.create(request));
    }

    @Operation(summary = "Отримати всі класи", description = "Повертає список усіх класів обслуговування.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список класів",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SeatClassResponse.class))))
    })
    @GetMapping("/seat-classes")
    public ResponseEntity<List<SeatClassResponse>> getAll() {
        return ResponseEntity.ok(seatClassService.getAll());
    }

    @Operation(summary = "Отримати клас за ID", description = "Повертає клас обслуговування за ідентифікатором.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Дані класу",
            content = @Content(schema = @Schema(implementation = SeatClassResponse.class))),
        @ApiResponse(responseCode = "404", description = "Клас не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/seat-classes/{id}")
    public ResponseEntity<SeatClassResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(seatClassService.getById(id));
    }

    @Operation(summary = "Оновити клас обслуговування", description = "Змінює назву та опис існуючого класу обслуговування.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Клас оновлено",
            content = @Content(schema = @Schema(implementation = SeatClassResponse.class))),
        @ApiResponse(responseCode = "400", description = "Помилка валідації",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Клас не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/seat-classes/{id}")
    public ResponseEntity<SeatClassResponse> update(@PathVariable Long id,
                                                     @Valid @RequestBody SeatClassUpdateRequest request) {
        return ResponseEntity.ok(seatClassService.update(id, request));
    }

    @Operation(summary = "Видалити клас обслуговування", description = "Видаляє клас обслуговування із системи.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Клас видалено"),
        @ApiResponse(responseCode = "404", description = "Клас не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/seat-classes/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        seatClassService.delete(id);
        return ResponseEntity.ok("SeatClass deleted " + id);
    }

    @Operation(
        summary = "Додати клас до рейсу",
        description = """
            Прив'язує клас обслуговування до конкретного рейсу.
            Бізнес-правила:
            - Один і той самий клас не може бути доданий до рейсу двічі.
            """
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Клас додано до рейсу"),
        @ApiResponse(responseCode = "404", description = "Рейс або клас не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Клас вже прив'язано до цього рейсу",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/flights/{id}/seat-classes/{classId}")
    public ResponseEntity<Flight> addToFlight(@PathVariable Long id, @PathVariable Long classId) {
        return ResponseEntity.ok(flightService.addSeatClass(id, classId));
    }

    @Operation(summary = "Видалити клас з рейсу", description = "Відв'язує клас обслуговування від рейсу.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Клас видалено з рейсу"),
        @ApiResponse(responseCode = "404", description = "Рейс не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/flights/{id}/seat-classes/{classId}")
    public ResponseEntity<Flight> removeFromFlight(@PathVariable Long id, @PathVariable Long classId) {
        return ResponseEntity.ok(flightService.removeSeatClass(id, classId));
    }
}
