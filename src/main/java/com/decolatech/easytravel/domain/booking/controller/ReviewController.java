package com.decolatech.easytravel.domain.booking.controller;

import com.decolatech.easytravel.common.Resposta;
import com.decolatech.easytravel.domain.booking.dto.ReviewDTO;
import com.decolatech.easytravel.domain.booking.service.ReviewService;
import com.decolatech.easytravel.domain.booking.enums.Rating;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "API para gerenciamento de avaliações")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Operation(summary = "Listar todas as avaliações")
    @GetMapping
    public ResponseEntity<?> getAllReviews() {
        try {
            List<ReviewDTO> reviews = reviewService.getAllReviews();
            return ResponseEntity.status(HttpStatus.OK).body(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Resposta(500, "Erro interno do servidor: " + e.getMessage()));
        }
    }

    @Operation(summary = "Buscar avaliação por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                .body(reviewService.getReviewById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Resposta(404, "Avaliação não encontrada"));
        }
    }

    @Operation(summary = "Criar nova avaliação")
    @PostMapping
    public ResponseEntity<?> createReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        try {
            ReviewDTO createdReview = reviewService.createReview(reviewDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Resposta(400, "Erro ao criar avaliação: " + e.getMessage()));
        }
    }

    @Operation(summary = "Atualizar avaliação")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Integer id, @Valid @RequestBody ReviewDTO reviewDTO) {
        try {
            ReviewDTO updatedReview = reviewService.updateReview(id, reviewDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedReview);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Resposta(400, e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Resposta(404, "Avaliação não encontrada"));
        }
    }

    @Operation(summary = "Deletar avaliação")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new Resposta(204, "Avaliação deletada com sucesso"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Resposta(404, "Avaliação não encontrada"));
        }
    }

    @Operation(summary = "Buscar avaliações por rating")
    @GetMapping("/rating/{rating}")
    public ResponseEntity<?> getReviewsByRating(@PathVariable Rating rating) {
        try {
            List<ReviewDTO> reviews = reviewService.getReviewsByRating(rating);
            return ResponseEntity.status(HttpStatus.OK).body(reviews);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Resposta(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Resposta(500, "Erro interno do servidor ao buscar avaliações por rating: " + e.getMessage()));
        }
    }

    @Operation(summary = "Buscar avaliações com rating mínimo")
    @GetMapping("/minimum-rating/{minRating}")
    public ResponseEntity<?> getReviewsByMinimumRating(@PathVariable Rating minRating) {
        try {
            List<ReviewDTO> reviews = reviewService.getReviewsByMinimumRating(minRating);
            return ResponseEntity.status(HttpStatus.OK).body(reviews);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Resposta(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Resposta(500, "Erro interno do servidor ao buscar avaliações por rating mínimo: " + e.getMessage()));
        }
    }

    @Operation(summary = "Buscar avaliações com comentário")
    @GetMapping("/with-comment")
    public ResponseEntity<?> getReviewsWithComment() {
        try {
            List<ReviewDTO> reviews = reviewService.getReviewsWithComment();
            return ResponseEntity.status(HttpStatus.OK).body(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Resposta(500, "Erro interno do servidor ao buscar avaliações com comentário: " + e.getMessage()));
        }
    }

    @Operation(summary = "Obter média de avaliações")
    @GetMapping("/average")
    public ResponseEntity<?> getAverageRating() {
        try {
            Double average = reviewService.getAverageRating();
            return ResponseEntity.status(HttpStatus.OK).body(average);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Resposta(500, "Erro interno do servidor ao calcular média das avaliações: " + e.getMessage()));
        }
    }
}
