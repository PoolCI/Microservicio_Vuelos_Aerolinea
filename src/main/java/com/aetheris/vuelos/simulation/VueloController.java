package com.aetheris.vuelos.simulation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
@Tag(name = "Flights", description = "API for flight management")
public class VueloController {

    private final VueloService vueloService;

    @Autowired
    public VueloController(VueloService vueloService) {
        this.vueloService = vueloService;
    }

    @GetMapping
    @Operation(summary = "Get all flights", description = "Returns a list of all registered flights.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flights retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Vuelo>> getAllFlights() {
        return ResponseEntity.ok(vueloService.getAllFlights());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get flight by ID", description = "Returns a flight by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flight found"),
            @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public ResponseEntity<Vuelo> getFlightById(
            @PathVariable @Parameter(description = "Flight ID") String id) {
        Vuelo vuelo = vueloService.getFlightById(id);
        return (vuelo != null) ? ResponseEntity.ok(vuelo) : ResponseEntity.notFound().build();
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get flight by code", description = "Returns a flight by its flight code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flight found"),
            @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public ResponseEntity<Vuelo> getFlightByCode(
            @PathVariable @Parameter(description = "Flight code") String code) {
        Vuelo vuelo = vueloService.getFlightByCode(code);
        return (vuelo != null) ? ResponseEntity.ok(vuelo) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new flight", description = "Creates a new flight with provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Flight created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid flight data")
    })
    public ResponseEntity<Vuelo> createFlight(
            @RequestBody @Parameter(description = "Flight data") Vuelo vuelo) {
        Vuelo newVuelo = vueloService.createFlight(vuelo);
        return new ResponseEntity<>(newVuelo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update flight", description = "Updates an existing flight.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flight updated successfully"),
            @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public ResponseEntity<Vuelo> updateFlight(
            @PathVariable @Parameter(description = "Flight ID") String id,
            @RequestBody @Parameter(description = "Updated flight data") Vuelo vuelo) {
        Vuelo existing = vueloService.getFlightById(id);
        if (existing != null) {
            vuelo.setId(id);
            Vuelo updated = vueloService.updateFlight(vuelo);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete flight", description = "Deletes a flight by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Flight deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public ResponseEntity<Void> deleteFlight(
            @PathVariable @Parameter(description = "Flight ID") String id) {
        Vuelo existing = vueloService.getFlightById(id);
        if (existing != null) {
            vueloService.deleteFlight(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search flights", description = "Search flights by origin, destination and date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flights found"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters")
    })
    public ResponseEntity<List<Vuelo>> searchFlights(
            @RequestParam @Parameter(description = "Origin city") String origin,
            @RequestParam @Parameter(description = "Destination city") String destination,
            @RequestParam(required = false) @Parameter(description = "Flight date (yyyy-MM-dd)") String date) {

        List<Vuelo> vuelos;
        if (date != null) {
            vuelos = vueloService.findFlightsByRouteAndDate(origin, destination, LocalDate.parse(date));
        } else {
            vuelos = vueloService.findFlightsByRoute(origin, destination);
        }
        return ResponseEntity.ok(vuelos);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get flights by status", description = "Returns all flights with a given status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flights found"),
            @ApiResponse(responseCode = "404", description = "No flights found with this status")
    })
    public ResponseEntity<List<Vuelo>> getFlightsByStatus(
            @PathVariable @Parameter(description = "Flight status") String status) {
        List<Vuelo> vuelos = vueloService.findFlightsByStatus(status);
        return vuelos.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(vuelos);
    }
}
