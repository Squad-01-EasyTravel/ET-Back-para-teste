package com.decolatech.easytravel.domain.booking.integration;

import com.decolatech.easytravel.domain.booking.dto.ReviewDTO;
import com.decolatech.easytravel.domain.booking.entity.Review;
import com.decolatech.easytravel.domain.booking.enums.Rating;
import com.decolatech.easytravel.domain.booking.repository.ReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class ReviewIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        // Limpar dados de teste
        reviewRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreateReviewIntegration() throws Exception {
        // Teste de integração completo para criar review
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setRating(Rating.FIVE_STARS);
        reviewDTO.setComment("Experiência incrível!");

        mockMvc.perform(post("/api/reviews")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating").value(4)) // FIVE_STARS = 4
                .andExpect(jsonPath("$.comment").value("Experiência incrível!"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.avaliationDate").exists());

        // Verificar se foi salvo no banco
        assertEquals(1, reviewRepository.count());
        Review savedReview = reviewRepository.findAll().get(0);
        assertEquals(Rating.FIVE_STARS, savedReview.getRating());
        assertEquals("Experiência incrível!", savedReview.getComment());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAllReviewsIntegration() throws Exception {
        // Criar dados de teste no banco
        Review review1 = new Review();
        review1.setRating(Rating.FOUR_STARS);
        review1.setComment("Muito bom!");
        review1.setAvaliationDate(LocalDateTime.now());

        Review review2 = new Review();
        review2.setRating(Rating.FIVE_STARS);
        review2.setComment("Excelente!");
        review2.setAvaliationDate(LocalDateTime.now());

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].rating").exists())
                .andExpect(jsonPath("$[1].rating").exists());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetReviewByIdIntegration() throws Exception {
        // Criar review no banco
        Review review = new Review();
        review.setRating(Rating.THREE_STARS);
        review.setComment("Regular");
        review.setAvaliationDate(LocalDateTime.now());
        Review savedReview = reviewRepository.save(review);

        mockMvc.perform(get("/api/reviews/" + savedReview.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedReview.getId()))
                .andExpect(jsonPath("$.rating").value(2)) // THREE_STARS = 2
                .andExpect(jsonPath("$.comment").value("Regular"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateReviewIntegration() throws Exception {
        // Criar review no banco
        Review review = new Review();
        review.setRating(Rating.TWO_STARS);
        review.setComment("Ruim");
        review.setAvaliationDate(LocalDateTime.now());
        Review savedReview = reviewRepository.save(review);

        // Atualizar review
        ReviewDTO updateDTO = new ReviewDTO();
        updateDTO.setRating(Rating.FOUR_STARS);
        updateDTO.setComment("Melhorou muito!");

        mockMvc.perform(put("/api/reviews/" + savedReview.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(3)) // FOUR_STARS = 3
                .andExpect(jsonPath("$.comment").value("Melhorou muito!"));

        // Verificar no banco
        Review updatedReview = reviewRepository.findById(savedReview.getId()).orElseThrow();
        assertEquals(Rating.FOUR_STARS, updatedReview.getRating());
        assertEquals("Melhorou muito!", updatedReview.getComment());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteReviewIntegration() throws Exception {
        // Criar review no banco
        Review review = new Review();
        review.setRating(Rating.ONE_STAR);
        review.setComment("Péssimo");
        review.setAvaliationDate(LocalDateTime.now());
        Review savedReview = reviewRepository.save(review);

        assertEquals(1, reviewRepository.count());

        mockMvc.perform(delete("/api/reviews/" + savedReview.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        // Verificar se foi deletado
        assertEquals(0, reviewRepository.count());
        assertFalse(reviewRepository.findById(savedReview.getId()).isPresent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetReviewsByRatingIntegration() throws Exception {
        // Criar reviews com diferentes ratings
        Review review1 = new Review();
        review1.setRating(Rating.FIVE_STARS);
        review1.setComment("Excelente 1");
        review1.setAvaliationDate(LocalDateTime.now());

        Review review2 = new Review();
        review2.setRating(Rating.FOUR_STARS);
        review2.setComment("Bom");
        review2.setAvaliationDate(LocalDateTime.now());

        Review review3 = new Review();
        review3.setRating(Rating.FIVE_STARS);
        review3.setComment("Excelente 2");
        review3.setAvaliationDate(LocalDateTime.now());

        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);

        mockMvc.perform(get("/api/reviews/rating/FIVE_STARS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].rating").value(4))
                .andExpect(jsonPath("$[1].rating").value(4));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testValidationErrorsIntegration() throws Exception {
        // Teste com dados inválidos
        ReviewDTO invalidDTO = new ReviewDTO();
        // Sem rating obrigatório

        mockMvc.perform(post("/api/reviews")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        // Verificar que nada foi salvo no banco
        assertEquals(0, reviewRepository.count());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testNotFoundIntegration() throws Exception {
        // Testar busca por ID inexistente
        mockMvc.perform(get("/api/reviews/999"))
                .andExpect(status().isNotFound());

        // Testar atualização de ID inexistente
        ReviewDTO updateDTO = new ReviewDTO();
        updateDTO.setRating(Rating.THREE_STARS);
        updateDTO.setComment("Teste");

        mockMvc.perform(put("/api/reviews/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());
    }
}
