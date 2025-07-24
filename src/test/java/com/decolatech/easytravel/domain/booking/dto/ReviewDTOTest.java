package com.decolatech.easytravel.domain.booking.dto;

import com.decolatech.easytravel.domain.booking.enums.Rating;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ReviewDTOTest {

    private ReviewDTO reviewDTO;
    private LocalDateTime testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDateTime.now();
        reviewDTO = new ReviewDTO();
    }

    @Test
    void testReviewDTOCreation() {
        // Teste de criação do DTO
        reviewDTO.setId(1);
        reviewDTO.setRating(Rating.FIVE_STARS);
        reviewDTO.setComment("Excelente experiência!");
        reviewDTO.setAvaliationDate(testDate);

        assertNotNull(reviewDTO);
        assertEquals(1, reviewDTO.getId());
        assertEquals(Rating.FIVE_STARS, reviewDTO.getRating());
        assertEquals("Excelente experiência!", reviewDTO.getComment());
        assertEquals(testDate, reviewDTO.getAvaliationDate());
    }

    @Test
    void testReviewDTODefaultConstructor() {
        // Teste do construtor padrão
        assertNull(reviewDTO.getId());
        assertNull(reviewDTO.getRating());
        assertNull(reviewDTO.getComment());
        assertNull(reviewDTO.getAvaliationDate());
    }

    @Test
    void testReviewDTOGettersAndSetters() {
        // Teste dos getters e setters
        Integer id = 42;
        Rating rating = Rating.THREE_STARS;
        String comment = "Comentário de teste";
        LocalDateTime date = LocalDateTime.now();

        reviewDTO.setId(id);
        reviewDTO.setRating(rating);
        reviewDTO.setComment(comment);
        reviewDTO.setAvaliationDate(date);

        assertEquals(id, reviewDTO.getId());
        assertEquals(rating, reviewDTO.getRating());
        assertEquals(comment, reviewDTO.getComment());
        assertEquals(date, reviewDTO.getAvaliationDate());
    }

    @Test
    void testReviewDTOValidation() {
        // Teste de validação - rating obrigatório
        reviewDTO.setRating(Rating.FOUR_STARS);
        reviewDTO.setAvaliationDate(testDate);

        assertNotNull(reviewDTO.getRating());
        assertNotNull(reviewDTO.getAvaliationDate());

        // Comentário é opcional
        assertNull(reviewDTO.getComment());
    }

    @Test
    void testReviewDTOAllRatings() {
        // Teste com todos os ratings disponíveis
        Rating[] allRatings = Rating.values();

        for (Rating rating : allRatings) {
            reviewDTO.setRating(rating);
            assertEquals(rating, reviewDTO.getRating());
        }
    }

    @Test
    void testReviewDTOCommentHandling() {
        // Teste de tratamento de comentários

        // Comentário nulo
        reviewDTO.setComment(null);
        assertNull(reviewDTO.getComment());

        // Comentário vazio
        reviewDTO.setComment("");
        assertEquals("", reviewDTO.getComment());

        // Comentário normal
        String normalComment = "Experiência satisfatória";
        reviewDTO.setComment(normalComment);
        assertEquals(normalComment, reviewDTO.getComment());

        // Comentário longo
        String longComment = "Este é um comentário muito longo que testa se o DTO consegue lidar com textos maiores sem problemas";
        reviewDTO.setComment(longComment);
        assertEquals(longComment, reviewDTO.getComment());
    }

    @Test
    void testReviewDTODateHandling() {
        // Teste de tratamento de datas
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime past = now.minusDays(30);
        LocalDateTime future = now.plusDays(1);

        // Data atual
        reviewDTO.setAvaliationDate(now);
        assertEquals(now, reviewDTO.getAvaliationDate());

        // Data passada
        reviewDTO.setAvaliationDate(past);
        assertEquals(past, reviewDTO.getAvaliationDate());

        // Data futura (para teste)
        reviewDTO.setAvaliationDate(future);
        assertEquals(future, reviewDTO.getAvaliationDate());
    }

    @Test
    void testReviewDTOToString() {
        // Teste do método toString se existir
        reviewDTO.setId(1);
        reviewDTO.setRating(Rating.FIVE_STARS);
        reviewDTO.setComment("Teste toString");
        reviewDTO.setAvaliationDate(testDate);

        String toString = reviewDTO.toString();

        if (toString != null) {
            assertNotNull(toString);
            assertTrue(toString.length() > 0);
        }
    }

    @Test
    void testReviewDTOEquality() {
        // Teste de igualdade entre DTOs
        ReviewDTO dto1 = new ReviewDTO();
        dto1.setId(1);
        dto1.setRating(Rating.FOUR_STARS);
        dto1.setComment("Teste");

        ReviewDTO dto2 = new ReviewDTO();
        dto2.setId(1);
        dto2.setRating(Rating.FOUR_STARS);
        dto2.setComment("Teste");

        ReviewDTO dto3 = new ReviewDTO();
        dto3.setId(2);
        dto3.setRating(Rating.THREE_STARS);
        dto3.setComment("Outro");

        // Se houver implementação de equals, teste aqui
        assertEquals(dto1.getId(), dto2.getId());
        assertNotEquals(dto1.getId(), dto3.getId());
    }
}
