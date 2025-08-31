package com.aetheris.vuelos.simulation;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vuelo {

    @Builder.Default
    private String id = UUID.randomUUID().toString(); // ID único interno

    private String codigoVuelo;       // Ej: "AV1234"
    private String aerolinea;         // Nombre de la aerolínea
    private String origen;            // Ciudad/país origen
    private String destino;           // Ciudad/país destino
    private String avionId;           // Relación con microservicio Avión
    private String pilotoId;          // Relación con microservicio Empleados

    private LocalDate fecha;          // Fecha del vuelo
    private LocalTime hora;           // Hora de salida
    private int duracionMinutos;      // Duración estimada

    @Builder.Default
    private String estado = "PROGRAMADO"; // PROGRAMADO, EN_VUELO, ATERRIZADO, CANCELADO

    private int asientosDisponibles;  // Capacidad actual
    private double precioBase;        // Precio base del vuelo
}
