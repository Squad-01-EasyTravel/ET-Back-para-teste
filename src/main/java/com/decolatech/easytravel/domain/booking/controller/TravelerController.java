package com.decolatech.easytravel.domain.booking.controller;

import com.decolatech.easytravel.common.Resposta;
import com.decolatech.easytravel.domain.booking.dto.TravelerDTO;
import com.decolatech.easytravel.domain.booking.service.TravelerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travelers")
@Tag(name = "Travelers", description = "API para gerenciamento de viajantes")
public class TravelerController {

    @Autowired
    private TravelerService travelerService;

    @Operation(summary = "Listar todos os viajantes")
    @GetMapping
    public ResponseEntity<?> getAllTravelers() {
        try {
            List<TravelerDTO> travelers = travelerService.getAllTravelers();
            return ResponseEntity.status(HttpStatus.OK).body(travelers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Resposta(500, "Erro interno do servidor: " + e.getMessage()));
        }
    }

    @Operation(summary = "Buscar viajante por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getTravelerById(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                .body(travelerService.getTravelerById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Resposta(404, "Viajante não encontrado"));
        }
    }

    @Operation(summary = "Criar novo viajante")
    @PostMapping
    public ResponseEntity<?> createTraveler(@Valid @RequestBody TravelerDTO travelerDTO) {
        try {
            TravelerDTO createdTraveler = travelerService.createTraveler(travelerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTraveler);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Resposta(400, "Erro ao criar viajante: " + e.getMessage()));
        }
    }

    @Operation(summary = "Atualizar viajante")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTraveler(@PathVariable Integer id, @Valid @RequestBody TravelerDTO travelerDTO) {
        try {
            TravelerDTO updatedTraveler = travelerService.updateTraveler(id, travelerDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedTraveler);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Resposta(400, e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Resposta(404, "Viajante não encontrado"));
        }
    }

    @Operation(summary = "Deletar viajante")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTraveler(@PathVariable Integer id) {
        try {
            travelerService.deleteTraveler(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new Resposta(204, "Viajante deletado com sucesso"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Resposta(404, "Viajante não encontrado"));
        }
    }

    @Operation(summary = "Buscar viajantes por reserva")
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<?> getTravelersByReservation(@PathVariable Integer reservationId) {
        try {
            List<TravelerDTO> travelers = travelerService.getTravelersByReservation(reservationId);
            return ResponseEntity.status(HttpStatus.OK).body(travelers);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Resposta(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Resposta(500, "Erro interno do servidor ao buscar viajantes por reserva: " + e.getMessage()));
        }
    }

    @Operation(summary = "Buscar viajantes por nome")
    @GetMapping("/search")
    public ResponseEntity<?> searchTravelersByName(@RequestParam String name) {
        try {
            List<TravelerDTO> travelers = travelerService.searchTravelersByName(name);
            return ResponseEntity.status(HttpStatus.OK).body(travelers);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Resposta(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Resposta(500, "Erro interno do servidor ao buscar viajantes por nome: " + e.getMessage()));
        }
    }
}
