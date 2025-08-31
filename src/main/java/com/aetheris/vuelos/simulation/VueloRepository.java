package com.aetheris.vuelos.simulation;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class VueloRepository {

    private final List<Vuelo> vuelos = new ArrayList<>();

    // =========================================
    // CRUD Operations
    // =========================================
    public Vuelo save(Vuelo vuelo) {
        vuelos.add(vuelo);
        return vuelo;
    }

    public Optional<Vuelo> findById(String id) {
        return vuelos.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst();
    }

    public Optional<Vuelo> findByCode(String flightCode) {
        return vuelos.stream()
                .filter(v -> v.getCodigoVuelo().equalsIgnoreCase(flightCode))
                .findFirst();
    }

    public List<Vuelo> findAll() {
        return new ArrayList<>(vuelos);
    }

    public Vuelo update(Vuelo vuelo) {
        for (int i = 0; i < vuelos.size(); i++) {
            if (vuelos.get(i).getId().equals(vuelo.getId())) {
                vuelos.set(i, vuelo);
                return vuelo;
            }
        }
        return null;
    }

    public boolean deleteById(String id) {
        return vuelos.removeIf(v -> v.getId().equals(id));
    }

    // =========================================
    // Search / Querying
    // =========================================
    public List<Vuelo> findByRoute(String origin, String destination) {
        return vuelos.stream()
                .filter(v -> v.getOrigen().equalsIgnoreCase(origin)
                        && v.getDestino().equalsIgnoreCase(destination))
                .toList();
    }

    public List<Vuelo> findByStatus(String status) {
        return vuelos.stream()
                .filter(v -> v.getEstado().equalsIgnoreCase(status))
                .toList();
    }

    public List<Vuelo> findByDate(LocalDate date) {
        return vuelos.stream()
                .filter(v -> v.getFecha().equals(date))
                .toList();
    }

    public List<Vuelo> findByDateRange(LocalDate start, LocalDate end) {
        return vuelos.stream()
                .filter(v -> !v.getFecha().isBefore(start) && !v.getFecha().isAfter(end))
                .toList();
    }

    public List<Vuelo> findByPlane(String planeId) {
        return vuelos.stream()
                .filter(v -> v.getAvionId().equals(planeId))
                .toList();
    }

    public List<Vuelo> findByPilot(String pilotId) {
        return vuelos.stream()
                .filter(v -> v.getPilotoId().equals(pilotId))
                .toList();
    }

    public List<Vuelo> findByRouteAndDate(String origin, String destination, LocalDate date) {
        return vuelos.stream()
                .filter(v -> v.getOrigen().equalsIgnoreCase(origin)
                        && v.getDestino().equalsIgnoreCase(destination)
                        && v.getFecha().equals(date))
                .toList();
    }

    public List<Vuelo> findByPriceRange(double min, double max) {
        return vuelos.stream()
                .filter(v -> v.getPrecioBase() >= min && v.getPrecioBase() <= max)
                .toList();
    }

    public List<Vuelo> findUpcomingFlights(LocalDate today, LocalTime currentTime, int hoursMargin) {
        return vuelos.stream()
                .filter(v -> v.getFecha().equals(today)
                        && !v.getHora().isBefore(currentTime)
                        && v.getHora().isBefore(currentTime.plusHours(hoursMargin)))
                .toList();
    }

    // =========================================
    // Seat Management
    // =========================================
    public boolean hasAvailability(String flightId, int requestedSeats) {
        return findById(flightId)
                .map(v -> v.getAsientosDisponibles() >= requestedSeats)
                .orElse(false);
    }

    public boolean reserveSeats(String flightId, int seats) {
        for (Vuelo v : vuelos) {
            if (v.getId().equals(flightId) && v.getAsientosDisponibles() >= seats) {
                v.setAsientosDisponibles(v.getAsientosDisponibles() - seats);
                return true;
            }
        }
        return false;
    }

    // =========================================
    // Flight State & Operations
    // =========================================
    public boolean updateStatus(String flightId, String newStatus) {
        return findById(flightId).map(v -> {
            v.setEstado(newStatus);
            return true;
        }).orElse(false);
    }

    public boolean cancelFlight(String flightId) {
        return findById(flightId).map(v -> {
            v.setEstado("CANCELADO");
            v.setAsientosDisponibles(0);
            return true;
        }).orElse(false);
    }

    public boolean reassignPlane(String flightId, String newPlaneId) {
        return findById(flightId).map(v -> {
            v.setAvionId(newPlaneId);
            return true;
        }).orElse(false);
    }

    // =========================================
    // Statistics
    // =========================================
    public long countByStatus(String status) {
        return vuelos.stream()
                .filter(v -> v.getEstado().equalsIgnoreCase(status))
                .count();
    }

    public Optional<Vuelo> findCheapestFlight() {
        return vuelos.stream()
                .min((v1, v2) -> Double.compare(v1.getPrecioBase(), v2.getPrecioBase()));
    }

    public List<Vuelo> findLongestFlights(int top) {
        return vuelos.stream()
                .sorted((v1, v2) -> Integer.compare(v2.getDuracionMinutos(), v1.getDuracionMinutos()))
                .limit(top)
                .toList();
    }
}
