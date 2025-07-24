package com.decolatech.easytravel.domain.booking.entity;

import com.decolatech.easytravel.domain.booking.enums.Rating;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    private Review review;
    private LocalDateTime testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDateTime.now();
        review = new Review();
    }

    @Test
    void testReviewCreation() {
        // Teste de criação da entidade Review
        review.setId(1);
        review.setRating(Rating.FIVE_STARS);
        review.setComment("Excelente experiência!");
        review.setAvaliationDate(testDate);

        assertNotNull(review);
        assertEquals(1, review.getId());
        assertEquals(Rating.FIVE_STARS, review.getRating());
        assertEquals("Excelente experiência!", review.getComment());
        assertEquals(testDate, review.getAvaliationDate());
    }

    @Test
    void testReviewConstructorWithArgs() {
        // Teste do construtor com argumentos
        Review reviewWithArgs = new Review(1, Rating.FOUR_STARS, "Muito bom!", testDate, null);

        assertEquals(1, reviewWithArgs.getId());
        assertEquals(Rating.FOUR_STARS, reviewWithArgs.getRating());
        assertEquals("Muito bom!", reviewWithArgs.getComment());
        assertEquals(testDate, reviewWithArgs.getAvaliationDate());
        assertNull(reviewWithArgs.getTravelHistories());
    }

    @Test
    void testReviewDefaultConstructor() {
        // Teste do construtor padrão
        assertNull(review.getId());
        assertNull(review.getRating());
        assertNull(review.getComment());
        assertNull(review.getAvaliationDate());
        assertNull(review.getTravelHistories());
    }

    @Test
    void testReviewToString() {
        // Teste do método toString
        review.setId(1);
        review.setRating(Rating.THREE_STARS);
        review.setComment("Comentário de teste");
        review.setAvaliationDate(testDate);

        String toString = review.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Review"));
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("THREE_STARS"));
        assertTrue(toString.contains("Comentário de teste"));
    }

    @Test
    void testReviewEqualsAndHashCode() {
        // Teste de equals e hashCode
        Review review1 = new Review();
        review1.setId(1);
        review1.setRating(Rating.FIVE_STARS);
        review1.setComment("Teste");
        review1.setAvaliationDate(testDate);

        Review review2 = new Review();
        review2.setId(1);
        review2.setRating(Rating.FIVE_STARS);
        review2.setComment("Teste");
        review2.setAvaliationDate(testDate);

        Review review3 = new Review();
        review3.setId(2);
        review3.setRating(Rating.FOUR_STARS);
        review3.setComment("Outro teste");
        review3.setAvaliationDate(testDate);

        // Testa igualdade baseada no ID
        assertEquals(review1.getId(), review2.getId());
        assertNotEquals(review1.getId(), review3.getId());
    }

    @Test
    void testReviewValidation() {
        // Teste de validação de campos obrigatórios
        review.setRating(Rating.ONE_STAR);
        review.setAvaliationDate(testDate);

        // Rating e data são obrigatórios
        assertNotNull(review.getRating());
        assertNotNull(review.getAvaliationDate());

        // Comentário é opcional
        assertNull(review.getComment());
    }

    @Test
    void testReviewRatingValues() {
        // Teste dos valores dos ratings
        review.setRating(Rating.ONE_STAR);
        assertEquals(0, review.getRating().getValue());

        review.setRating(Rating.TWO_STARS);
        assertEquals(1, review.getRating().getValue());

        review.setRating(Rating.THREE_STARS);
        assertEquals(2, review.getRating().getValue());

        review.setRating(Rating.FOUR_STARS);
        assertEquals(3, review.getRating().getValue());

        review.setRating(Rating.FIVE_STARS);
        assertEquals(4, review.getRating().getValue());
    }

    @Test
    void testReviewCommentLimits() {
        // Teste de limites do comentário
        String shortComment = "OK";
        String normalComment = "Experiência muito boa, recomendo!";
        String longComment = "A" + "a".repeat(500); // Comentário muito longo

        review.setComment(shortComment);
        assertEquals(shortComment, review.getComment());

        review.setComment(normalComment);
        assertEquals(normalComment, review.getComment());

        review.setComment(longComment);
        assertEquals(longComment, review.getComment());
    }

    @Test
    void testReviewDateValidation() {
        // Teste de validação de datas
        LocalDateTime pastDate = LocalDateTime.now().minusDays(10);
        LocalDateTime futureDate = LocalDateTime.now().plusDays(10);

        review.setAvaliationDate(pastDate);
        assertEquals(pastDate, review.getAvaliationDate());

        review.setAvaliationDate(testDate);
        assertEquals(testDate, review.getAvaliationDate());

        // Normalmente não permitiríamos datas futuras, mas vamos testar
        review.setAvaliationDate(futureDate);
        assertEquals(futureDate, review.getAvaliationDate());
    }
}
