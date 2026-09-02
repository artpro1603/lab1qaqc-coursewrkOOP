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
import ua.opnu.labwork21.dto.request.BookingCreateRequest;
import ua.opnu.labwork21.dto.request.BookingUpdateRequest;
import ua.opnu.labwork21.dto.response.BookingResponse;
import ua.opnu.labwork21.service.BookingService;

import java.util.List;

@Tag(name = "Бронювання", description = "Управління бронюваннями квитків: створення, зміна статусу та скасування")
@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(
        summary = "Створити бронювання",
        description = """
            Створює нове бронювання для пасажира на один або кілька рейсів.
            Бізнес-правила:
            - Пасажир та всі вказані рейси повинні існувати.
            - Не можна створити дубль: активне бронювання (CREATED/CONFIRMED) для того самого пасажира і рейсу вже не допускається.
            - Початковий статус не може бути CANCELLED.
            - Дата бронювання є обов'язковою.
            """
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Бронювання успішно створено",
            content = @Content(schema = @Schema(implementation = BookingResponse.class))),
        @ApiResponse(responseCode = "400", description = "Некоректні дані або недопустимий статус",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Пасажира або рейс не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Активне бронювання для цього пасажира та рейсу вже існує",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<BookingResponse> create(@Valid @RequestBody BookingCreateRequest request) {
        return ResponseEntity.ok(bookingService.create(request));
    }

    @Operation(summary = "Отримати всі бронювання", description = "Повертає список усіх бронювань у системі.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список бронювань",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookingResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAll() {
        return ResponseEntity.ok(bookingService.getAll());
    }

    @Operation(summary = "Отримати бронювання за ID", description = "Повертає деталі бронювання за його ідентифікатором.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Дані бронювання",
            content = @Content(schema = @Schema(implementation = BookingResponse.class))),
        @ApiResponse(responseCode = "404", description = "Бронювання не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getById(id));
    }

    @Operation(
        summary = "Оновити бронювання",
        description = """
            Оновлює дату та статус існуючого бронювання.
            Бізнес-правила:
            - Скасоване бронювання (CANCELLED) не може бути змінено.
            """
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Бронювання оновлено",
            content = @Content(schema = @Schema(implementation = BookingResponse.class))),
        @ApiResponse(responseCode = "400", description = "Некоректні дані",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Бронювання не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Неможливо оновити скасоване бронювання",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody BookingUpdateRequest request) {
        return ResponseEntity.ok(bookingService.update(id, request));
    }

    @Operation(
        summary = "Скасувати бронювання",
        description = "Скасовує бронювання, встановлюючи статус CANCELLED. Фізичного видалення запису не відбувається."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Бронювання скасовано"),
        @ApiResponse(responseCode = "404", description = "Бронювання не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.ok("Booking cancelled " + id);
    }

    @Operation(summary = "Бронювання пасажира", description = "Повертає всі бронювання вказаного пасажира.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список бронювань",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookingResponse.class)))),
        @ApiResponse(responseCode = "404", description = "Пасажира не знайдено",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<BookingResponse>> getByPassenger(@PathVariable Long passengerId) {
        return ResponseEntity.ok(bookingService.getByPassengerId(passengerId));
    }
}
