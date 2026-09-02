package ua.opnu.labwork21.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String flightNumber;

    @Column(nullable = false)
    private String departureCity;

    @Column(nullable = false)
    private String arrivalCity;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    // Aircraft -> Flight (1:N)
    @ManyToOne
    @JoinColumn(name = "aircraft_id")
    private Aircraft aircraft;

    // Flight <-> SeatClass (M:N)
    @ManyToMany
    @JoinTable(
        name = "flight_seat_classes",
        joinColumns = @JoinColumn(name = "flight_id"),
        inverseJoinColumns = @JoinColumn(name = "seat_class_id")
    )
    private List<SeatClass> seatClasses;

    // Booking <-> Flight (M:N) — зворотній бік
    @ManyToMany(mappedBy = "flights")
    @JsonIgnore
    private List<Booking> bookings;

    // Passenger <-> Flight (M:N) — зворотній бік
    @ManyToMany(mappedBy = "flights")
    @JsonIgnore
    private List<Passenger> passengers;

    public Flight() {}

    public Flight(Long id, String flightNumber, String departureCity,
                  String arrivalCity, LocalDateTime departureTime) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureTime = departureTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    public String getDepartureCity() { return departureCity; }
    public void setDepartureCity(String departureCity) { this.departureCity = departureCity; }
    public String getArrivalCity() { return arrivalCity; }
    public void setArrivalCity(String arrivalCity) { this.arrivalCity = arrivalCity; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }
    public Aircraft getAircraft() { return aircraft; }
    public void setAircraft(Aircraft aircraft) { this.aircraft = aircraft; }
    public List<SeatClass> getSeatClasses() { return seatClasses; }
    public void setSeatClasses(List<SeatClass> seatClasses) { this.seatClasses = seatClasses; }
    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
    public List<Passenger> getPassengers() { return passengers; }
    public void setPassengers(List<Passenger> passengers) { this.passengers = passengers; }
}
