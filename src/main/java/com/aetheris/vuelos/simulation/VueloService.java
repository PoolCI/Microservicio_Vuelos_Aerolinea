package com.aetheris.vuelos.simulation;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class VueloService {

    private final VueloRepository vueloRepository;

    public VueloService(VueloRepository vueloRepository) {
        this.vueloRepository = vueloRepository;
    }

    @PostConstruct
    public void initData() {
        vueloRepository.save(Vuelo.builder()
                .codigoVuelo("AV1234")
                .aerolinea("Avianca")
                .origen("Bogotá")
                .destino("Medellín")
                .avionId("A320-001")
                .pilotoId("PIL-001")
                .fecha(LocalDate.now().plusDays(1))
                .hora(LocalTime.of(8, 30))
                .duracionMinutos(60)
                .asientosDisponibles(150)
                .precioBase(350000.0)
                .estado("SCHEDULED")
                .build());

        vueloRepository.save(Vuelo.builder()
                .codigoVuelo("AV5678")
                .aerolinea("LATAM")
                .origen("Medellín")
                .destino("Cartagena")
                .avionId("A320-002")
                .pilotoId("PIL-002")
                .fecha(LocalDate.now().plusDays(2))
                .hora(LocalTime.of(10, 45))
                .duracionMinutos(70)
                .asientosDisponibles(180)
                .precioBase(420000.0)
                .estado("SCHEDULED")
                .build());

        vueloRepository.save(Vuelo.builder()
                .codigoVuelo("AV9012")
                .aerolinea("Viva Air")
                .origen("Bogotá")
                .destino("Cali")
                .avionId("B737-003")
                .pilotoId("PIL-003")
                .fecha(LocalDate.now().plusDays(3))
                .hora(LocalTime.of(14, 15))
                .duracionMinutos(75)
                .asientosDisponibles(160)
                .precioBase(390000.0)
                .estado("SCHEDULED")
                .build());

        vueloRepository.save(Vuelo.builder()
                .codigoVuelo("AV3456")
                .aerolinea("Avianca")
                .origen("Cali")
                .destino("Miami")
                .avionId("B787-004")
                .pilotoId("PIL-004")
                .fecha(LocalDate.now().plusDays(5))
                .hora(LocalTime.of(9, 0))
                .duracionMinutos(240)
                .asientosDisponibles(250)
                .precioBase(1500000.0)
                .estado("MANTENIMIENTO")
                .build());

        vueloRepository.save(Vuelo.builder()
                .codigoVuelo("AV7890")
                .aerolinea("Iberia")
                .origen("Bogotá")
                .destino("Madrid")
                .avionId("A350-005")
                .pilotoId("PIL-005")
                .fecha(LocalDate.now().plusDays(10))
                .hora(LocalTime.of(19, 45))
                .duracionMinutos(600)
                .asientosDisponibles(300)
                .precioBase(2800000.0)
                .estado("MANTENIMIENTO")
                .build());
    }

    // ----- CRUD -----
    public Vuelo createFlight(Vuelo vuelo) {
        return vueloRepository.save(vuelo);
    }

    public Vuelo getFlightById(String id) {
        return vueloRepository.findById(id).orElse(null);
    }

    public Vuelo getFlightByCode(String flightCode) {
        return vueloRepository.findByCode(flightCode).orElse(null);
    }

    public List<Vuelo> getAllFlights() {
        return vueloRepository.findAll();
    }

    public Vuelo updateFlight(Vuelo vuelo) {
        return vueloRepository.update(vuelo);
    }

    public boolean deleteFlight(String id) {
        return vueloRepository.deleteById(id);
    }

    // ----- QUERIES -----
    public List<Vuelo> findFlightsByRoute(String origin, String destination) {
        return vueloRepository.findByRoute(origin, destination);
    }

    public List<Vuelo> findFlightsByStatus(String status) {
        return vueloRepository.findByStatus(status);
    }

    public List<Vuelo> findFlightsByDate(LocalDate date) {
        return vueloRepository.findByDate(date);
    }

    public List<Vuelo> findFlightsByDateRange(LocalDate start, LocalDate end) {
        return vueloRepository.findByDateRange(start, end);
    }

    public List<Vuelo> findFlightsByPlane(String planeId) {
        return vueloRepository.findByPlane(planeId);
    }

    public List<Vuelo> findFlightsByPilot(String pilotId) {
        return vueloRepository.findByPilot(pilotId);
    }

    public List<Vuelo> findFlightsByRouteAndDate(String origin, String destination, LocalDate date) {
        return vueloRepository.findByRouteAndDate(origin, destination, date);
    }

    public List<Vuelo> findFlightsByPriceRange(double min, double max) {
        return vueloRepository.findByPriceRange(min, max);
    }

    public List<Vuelo> findUpcomingFlights(LocalDate today, LocalTime currentTime, int hoursMargin) {
        return vueloRepository.findUpcomingFlights(today, currentTime, hoursMargin);
    }

    // ----- BUSINESS OPERATIONS -----
    public boolean checkAvailability(String flightId, int requestedSeats) {
        return vueloRepository.hasAvailability(flightId, requestedSeats);
    }

    public boolean reserveSeats(String flightId, int seats) {
        return vueloRepository.reserveSeats(flightId, seats);
    }

    public boolean updateFlightStatus(String flightId, String newStatus) {
        return vueloRepository.updateStatus(flightId, newStatus);
    }

    public boolean cancelFlight(String flightId) {
        return vueloRepository.cancelFlight(flightId);
    }

    public boolean reassignPlane(String flightId, String newPlaneId) {
        return vueloRepository.reassignPlane(flightId, newPlaneId);
    }

    public long countFlightsByStatus(String status) {
        return vueloRepository.countByStatus(status);
    }

    public Vuelo getCheapestFlight() {
        return vueloRepository.findCheapestFlight().orElse(null);
    }

    public List<Vuelo> getLongestFlights(int top) {
        return vueloRepository.findLongestFlights(top);
    }
}
