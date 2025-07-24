package com.decolatech.easytravel.domain.booking.controller;

import com.decolatech.easytravel.common.Resposta;
import com.decolatech.easytravel.domain.booking.dto.PaymentDTO;
import com.decolatech.easytravel.domain.booking.service.PaymentService;
import com.decolatech.easytravel.domain.booking.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "API para gerenciamento de pagamentos")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "Listar todos os pagamentos")
    @GetMapping
    public ResponseEntity<?> getAllPayments() {
        try {
            List<PaymentDTO> payments = paymentService.getAllPayments();
            return ResponseEntity.status(HttpStatus.OK).body(payments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Resposta(500, "Erro interno do servidor: " + e.getMessage()));
        }
    }

    @Operation(summary = "Buscar pagamento por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Integer id) {
        try {
            PaymentDTO payment = paymentService.getPaymentById(id);
            return ResponseEntity.status(HttpStatus.OK).body(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Resposta(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Resposta(500, "Erro interno do servidor: " + e.getMessage()));
        }
    }

    @Operation(summary = "Criar novo pagamento")
    @PostMapping
    public ResponseEntity<?> createPayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        try {
            PaymentDTO createdPayment = paymentService.createPayment(paymentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPayment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Resposta(400, "Erro ao criar pagamento: " + e.getMessage()));
        }
    }

    @Operation(summary = "Atualizar pagamento")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayment(@PathVariable Integer id, @Valid @RequestBody PaymentDTO paymentDTO) {
        try {
            PaymentDTO updatedPayment = paymentService.updatePayment(id, paymentDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedPayment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Resposta(400, e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Resposta(404, "Pagamento não encontrado"));
        }
    }

    @Operation(summary = "Deletar pagamento")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Integer id) {
        try {
            paymentService.deletePayment(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new Resposta(204, "Pagamento deletado com sucesso"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Resposta(404, "Pagamento não encontrado"));
        }
    }

    @Operation(summary = "Buscar pagamentos por reserva")
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<?> getPaymentsByReservation(@PathVariable Integer reservationId) {
        try {
            List<PaymentDTO> payments = paymentService.getPaymentsByReservation(reservationId);
            return ResponseEntity.status(HttpStatus.OK).body(payments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Resposta(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Resposta(500, "Erro interno do servidor ao buscar pagamentos por reserva: " + e.getMessage()));
        }
    }

    @Operation(summary = "Buscar pagamentos por método")
    @GetMapping("/method/{method}")
    public ResponseEntity<?> getPaymentsByMethod(@PathVariable PaymentMethod method) {
        try {
            List<PaymentDTO> payments = paymentService.getPaymentsByMethod(method);
            return ResponseEntity.status(HttpStatus.OK).body(payments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Resposta(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Resposta(500, "Erro interno do servidor ao buscar pagamentos por método: " + e.getMessage()));
        }
    }

    @Operation(summary = "Obter total de pagamentos por período")
    @GetMapping("/total")
    public ResponseEntity<?> getTotalPaymentsByPeriod(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        try {
            Double total = paymentService.getTotalPaymentsByPeriod(startDate, endDate);
            return ResponseEntity.status(HttpStatus.OK).body(total);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Resposta(400, "Período inválido: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Resposta(500, "Erro interno do servidor ao calcular total de pagamentos: " + e.getMessage()));
        }
    }
}
