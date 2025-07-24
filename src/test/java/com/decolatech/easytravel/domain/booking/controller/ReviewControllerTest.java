package com.decolatech.easytravel.domain.booking.controller;

import com.decolatech.easytravel.domain.booking.dto.ReviewDTO;
import com.decolatech.easytravel.domain.booking.enums.Rating;
import com.decolatech.easytravel.domain.booking.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    private ReviewDTO sampleReviewDTO;

    @BeforeEach
    void setUp() {
        sampleReviewDTO = new ReviewDTO();
        sampleReviewDTO.setId(1);
        sampleReviewDTO.setRating(Rating.FIVE_STARS);
        sampleReviewDTO.setComment("Excelente!");
        sampleReviewDTO.setAvaliationDate(LocalDateTime.now());
    }

    @Test
    @WithMockUser
    void testGetAllReviews() throws Exception {
        // Teste para buscar todas as reviews
        List<ReviewDTO> reviews = List.of(sampleReviewDTO);
        when(reviewService.getAllReviews()).thenReturn(reviews);

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].rating").value(4)) // FIVE_STARS tem valor 4
                .andExpect(jsonPath("$[0].comment").value("Excelente!"));

        verify(reviewService, times(1)).getAllReviews();
    }

    @Test
    @WithMockUser
    void testGetReviewByIdFound() throws Exception {
        // Teste para buscar review por ID existente
        when(reviewService.getReviewById(1)).thenReturn(Optional.of(sampleReviewDTO));

        mockMvc.perform(get("/api/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rating").value(4)) // FIVE_STARS tem valor 4
                .andExpect(jsonPath("$.comment").value("Excelente!"));

        verify(reviewService, times(1)).getReviewById(1);
    }

    @Test
    @WithMockUser
    void testGetReviewByIdNotFound() throws Exception {
        // Teste para buscar review por ID inexistente
        when(reviewService.getReviewById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reviews/999"))
                .andExpect(status().isNotFound());

        verify(reviewService, times(1)).getReviewById(999);
    }

    @Test
    @WithMockUser
    void testCreateReviewSuccess() throws Exception {
        // Teste para criar nova review com sucesso
        ReviewDTO inputDTO = new ReviewDTO();
        inputDTO.setRating(Rating.FOUR_STARS);
        inputDTO.setComment("Muito bom!");

        ReviewDTO createdDTO = new ReviewDTO();
        createdDTO.setId(2);
        createdDTO.setRating(Rating.FOUR_STARS);
        createdDTO.setComment("Muito bom!");
        createdDTO.setAvaliationDate(LocalDateTime.now());

        when(reviewService.createReview(any(ReviewDTO.class))).thenReturn(createdDTO);

        mockMvc.perform(post("/api/reviews")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.rating").value(3)) // FOUR_STARS tem valor 3
                .andExpect(jsonPath("$.comment").value("Muito bom!"));

        verify(reviewService, times(1)).createReview(any(ReviewDTO.class));
    }

    @Test
    @WithMockUser
    void testCreateReviewWithInvalidData() throws Exception {
        // Teste para criar review com dados inválidos
        ReviewDTO invalidDTO = new ReviewDTO();
        // Sem rating obrigatório

        mockMvc.perform(post("/api/reviews")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(reviewService, never()).createReview(any(ReviewDTO.class));
    }

    @Test
    @WithMockUser
    void testUpdateReviewSuccess() throws Exception {
        // Teste para atualizar review com sucesso
        ReviewDTO updateDTO = new ReviewDTO();
        updateDTO.setRating(Rating.THREE_STARS);
        updateDTO.setComment("Atualizado!");

        ReviewDTO updatedDTO = new ReviewDTO();
        updatedDTO.setId(1);
        updatedDTO.setRating(Rating.THREE_STARS);
        updatedDTO.setComment("Atualizado!");
        updatedDTO.setAvaliationDate(LocalDateTime.now());

        when(reviewService.updateReview(eq(1), any(ReviewDTO.class))).thenReturn(updatedDTO);

        mockMvc.perform(put("/api/reviews/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rating").value(2)) // THREE_STARS tem valor 2
                .andExpect(jsonPath("$.comment").value("Atualizado!"));

        verify(reviewService, times(1)).updateReview(eq(1), any(ReviewDTO.class));
    }

    @Test
    @WithMockUser
    void testDeleteReviewSuccess() throws Exception {
        // Teste para deletar review com sucesso
        doNothing().when(reviewService).deleteReview(1);

        mockMvc.perform(delete("/api/reviews/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(reviewService, times(1)).deleteReview(1);
    }

    @Test
    @WithMockUser
    void testCreateReviewServiceException() throws Exception {
        // Teste quando o service lança exceção
        ReviewDTO inputDTO = new ReviewDTO();
        inputDTO.setRating(Rating.ONE_STAR);
        inputDTO.setComment("Teste");

        when(reviewService.createReview(any(ReviewDTO.class)))
                .thenThrow(new RuntimeException("Erro interno"));

        mockMvc.perform(post("/api/reviews")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isBadRequest());

        verify(reviewService, times(1)).createReview(any(ReviewDTO.class));
    }

    @Test
    @WithMockUser
    void testGetReviewsByRating() throws Exception {
        // Teste para buscar reviews por rating específico
        List<ReviewDTO> fiveStarReviews = List.of(sampleReviewDTO);
        when(reviewService.getReviewsByRating(Rating.FIVE_STARS)).thenReturn(fiveStarReviews);

        mockMvc.perform(get("/api/reviews/rating/FIVE_STARS"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].rating").value(4));

        verify(reviewService, times(1)).getReviewsByRating(Rating.FIVE_STARS);
    }

    @Test
    @WithMockUser
    void testValidationErrors() throws Exception {
        // Teste para validação de campos obrigatórios
        ReviewDTO invalidDTO = new ReviewDTO();
        // Rating é obrigatório mas não foi definido

        mockMvc.perform(post("/api/reviews")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }
}
