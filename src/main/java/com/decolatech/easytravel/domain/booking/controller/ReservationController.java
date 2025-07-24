package com.decolatech.easytravel.domain.booking.controller;

import com.decolatech.easytravel.common.Resposta;
import com.decolatech.easytravel.domain.booking.dto.ReservationDTO;
import com.decolatech.easytravel.domain.booking.service.ReservationService;
import com.decolatech.easytravel.domain.booking.enums.ReservationStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservations", description = "API para gerenciamento de reservas")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Operation(summary = "Listar todas as reservas")
    @GetMapping
    public ResponseEntity<?> getAllReservations() {
        try {
            List<ReservationDTO> reservations = reservationService.getAllReservations();
            return ResponseEntity.status(HttpStatus.OK).body(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Resposta(500, "Erro interno do servidor: " + e.getMessage()));
        }
    }

    @Operation(summary = "Buscar reserva por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable Integer id) {
        try {
            ReservationDTO reservation = reservationService.getReservationById(id);
            return ResponseEntity.status(HttpStatus.OK).body(reservation);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Resposta(404, "Reserva não encontrada"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Resposta(500, "Erro interno do servidor: " + e.getMessage()));
        }
    }

    @Operation(summary = "Criar nova reserva")
    @PostMapping
    public ResponseEntity<?> createReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        try {
            ReservationDTO createdReservation = reservationService.createReservation(reservationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Resposta(400, "Erro ao criar reserva: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Resposta(404, "Erro ao criar reserva: " + e.getMessage()));
        }
    }

    @Operation(summary = "Atualizar reserva")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable Integer id, @Valid @RequestBody ReservationDTO reservationDTO) {
        try {
            ReservationDTO updatedReservation = reservationService.updateReservation(id, reservationDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedReservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Resposta(400, e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Resposta(404, "Reserva não encontrada"));
        }
    }

    @Operation(summary = "Deletar reserva")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Integer id) {
        try {
            reservationService.deleteReservation(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new Resposta(204, "Reserva deletada com sucesso"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Resposta(404, "Reserva não encontrada"));
        }
    }

    @Operation(summary = "Buscar reservas por usuário")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReservationsByUser(@PathVariable Integer userId) {
        try {
            List<ReservationDTO> reservations = reservationService.getReservationsByUser(userId);
            return ResponseEntity.status(HttpStatus.OK).body(reservations);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Resposta(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Resposta(500, "Erro interno do servidor ao buscar reservas por usuário: " + e.getMessage()));
        }
    }

    @Operation(summary = "Buscar reservas por status")
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getReservationsByStatus(@PathVariable ReservationStatus status) {
        try {
            List<ReservationDTO> reservations = reservationService.getReservationsByStatus(status);
            return ResponseEntity.status(HttpStatus.OK).body(reservations);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Resposta(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Resposta(500, "Erro interno do servidor ao buscar reservas por status: " + e.getMessage()));
        }
    }

    @Operation(summary = "Confirmar reserva")
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<?> confirmReservation(@PathVariable Integer id) {
        try {
            ReservationDTO confirmedReservation = reservationService.confirmReservation(id);
            return ResponseEntity.status(HttpStatus.OK).body(confirmedReservation);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Resposta(404, "Reserva não encontrada"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Resposta(500, "Erro interno do servidor ao confirmar reserva: " + e.getMessage()));
        }
    }

    @Operation(summary = "Cancelar reserva")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable Integer id) {
        try {
            ReservationDTO cancelledReservation = reservationService.cancelReservation(id);
            return ResponseEntity.ok(cancelledReservation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
