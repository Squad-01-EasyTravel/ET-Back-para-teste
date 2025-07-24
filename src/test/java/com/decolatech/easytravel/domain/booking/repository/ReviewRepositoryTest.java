package com.decolatech.easytravel.domain.booking.repository;

import com.decolatech.easytravel.domain.booking.entity.Review;
import com.decolatech.easytravel.domain.booking.enums.Rating;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void testFindByRating() {
        // Criar reviews de teste
        Review review1 = new Review();
        review1.setRating(Rating.FIVE_STARS);
        review1.setComment("Excelente!");
        review1.setAvaliationDate(LocalDateTime.now());

        Review review2 = new Review();
        review2.setRating(Rating.FOUR_STARS);
        review2.setComment("Muito bom!");
        review2.setAvaliationDate(LocalDateTime.now());

        Review review3 = new Review();
        review3.setRating(Rating.FIVE_STARS);
        review3.setComment("Perfeito!");
        review3.setAvaliationDate(LocalDateTime.now());

        entityManager.persistAndFlush(review1);
        entityManager.persistAndFlush(review2);
        entityManager.persistAndFlush(review3);

        // Testar busca por rating
        List<Review> fiveStarReviews = reviewRepository.findByRating(Rating.FIVE_STARS);
        List<Review> fourStarReviews = reviewRepository.findByRating(Rating.FOUR_STARS);

        assertEquals(2, fiveStarReviews.size());
        assertEquals(1, fourStarReviews.size());
        assertTrue(fiveStarReviews.stream().allMatch(r -> r.getRating() == Rating.FIVE_STARS));
        assertTrue(fourStarReviews.stream().allMatch(r -> r.getRating() == Rating.FOUR_STARS));
    }

    @Test
    void testFindByAvaliationDateRange() {
        // Criar reviews com datas diferentes
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        LocalDateTime tomorrow = now.plusDays(1);

        Review review1 = new Review();
        review1.setRating(Rating.THREE_STARS);
        review1.setComment("Ok");
        review1.setAvaliationDate(yesterday);

        Review review2 = new Review();
        review2.setRating(Rating.FOUR_STARS);
        review2.setComment("Bom");
        review2.setAvaliationDate(now);

        Review review3 = new Review();
        review3.setRating(Rating.FIVE_STARS);
        review3.setComment("Excelente");
        review3.setAvaliationDate(tomorrow);

        entityManager.persistAndFlush(review1);
        entityManager.persistAndFlush(review2);
        entityManager.persistAndFlush(review3);

        // Testar busca por intervalo de datas usando o método correto
        List<Review> reviewsInRange = reviewRepository.findByAvaliationDateRange(
                yesterday.minusHours(1),
                now.plusHours(1)
        );

        assertEquals(2, reviewsInRange.size());
        assertTrue(reviewsInRange.stream().anyMatch(r -> r.getComment().equals("Ok")));
        assertTrue(reviewsInRange.stream().anyMatch(r -> r.getComment().equals("Bom")));
    }

    @Test
    void testFindByCommentIsNotNull() {
        // Criar reviews com e sem comentário
        Review reviewWithComment = new Review();
        reviewWithComment.setRating(Rating.FOUR_STARS);
        reviewWithComment.setComment("Com comentário");
        reviewWithComment.setAvaliationDate(LocalDateTime.now());

        Review reviewWithoutComment = new Review();
        reviewWithoutComment.setRating(Rating.THREE_STARS);
        reviewWithoutComment.setComment(null);
        reviewWithoutComment.setAvaliationDate(LocalDateTime.now());

        entityManager.persistAndFlush(reviewWithComment);
        entityManager.persistAndFlush(reviewWithoutComment);

        // Testar busca por reviews com comentário
        List<Review> reviewsWithComments = reviewRepository.findByCommentIsNotNull();

        assertEquals(1, reviewsWithComments.size());
        assertEquals("Com comentário", reviewsWithComments.get(0).getComment());
    }

    @Test
    void testFindByRatingGreaterThanEqual() {
        // Criar reviews com diferentes ratings
        Review review1 = new Review();
        review1.setRating(Rating.TWO_STARS);
        review1.setComment("Ruim");
        review1.setAvaliationDate(LocalDateTime.now());

        Review review2 = new Review();
        review2.setRating(Rating.FOUR_STARS);
        review2.setComment("Bom");
        review2.setAvaliationDate(LocalDateTime.now());

        Review review3 = new Review();
        review3.setRating(Rating.FIVE_STARS);
        review3.setComment("Excelente");
        review3.setAvaliationDate(LocalDateTime.now());

        entityManager.persistAndFlush(review1);
        entityManager.persistAndFlush(review2);
        entityManager.persistAndFlush(review3);

        // Testar busca por rating mínimo usando o método correto
        List<Review> goodReviews = reviewRepository.findByMinimumRating(Rating.FOUR_STARS);

        assertEquals(2, goodReviews.size());
        assertTrue(goodReviews.stream().allMatch(r ->
            r.getRating() == Rating.FOUR_STARS || r.getRating() == Rating.FIVE_STARS));
    }

    @Test
    void testCountByRating() {
        // Criar múltiplas reviews com mesmo rating
        for (int i = 0; i < 3; i++) {
            Review review = new Review();
            review.setRating(Rating.FIVE_STARS);
            review.setComment("Review " + i);
            review.setAvaliationDate(LocalDateTime.now());
            entityManager.persistAndFlush(review);
        }

        // Contar reviews por rating usando o método correto
        long count = reviewRepository.countReviewsByRating(Rating.FIVE_STARS);
        assertEquals(3, count);

        long countZero = reviewRepository.countReviewsByRating(Rating.ONE_STAR);
        assertEquals(0, countZero);
    }

    @Test
    void testFindAllByOrderByAvaliationDateDesc() {
        // Criar reviews com datas diferentes
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        LocalDateTime tomorrow = now.plusDays(1);

        Review review1 = new Review();
        review1.setRating(Rating.THREE_STARS);
        review1.setComment("Primeira");
        review1.setAvaliationDate(yesterday);

        Review review2 = new Review();
        review2.setRating(Rating.FOUR_STARS);
        review2.setComment("Segunda");
        review2.setAvaliationDate(now);

        Review review3 = new Review();
        review3.setRating(Rating.FIVE_STARS);
        review3.setComment("Terceira");
        review3.setAvaliationDate(tomorrow);

        entityManager.persistAndFlush(review1);
        entityManager.persistAndFlush(review2);
        entityManager.persistAndFlush(review3);

        // Testar ordenação por data descendente
        List<Review> orderedReviews = reviewRepository.findAllByOrderByAvaliationDateDesc();

        assertEquals(3, orderedReviews.size());
        assertEquals("Terceira", orderedReviews.get(0).getComment()); // Mais recente primeiro
        assertEquals("Segunda", orderedReviews.get(1).getComment());
        assertEquals("Primeira", orderedReviews.get(2).getComment()); // Mais antiga por último
    }

    @Test
    void testGetAverageRating() {
        // Criar reviews com diferentes ratings
        Review review1 = new Review();
        review1.setRating(Rating.TWO_STARS); // valor 1
        review1.setComment("Ruim");
        review1.setAvaliationDate(LocalDateTime.now());

        Review review2 = new Review();
        review2.setRating(Rating.FOUR_STARS); // valor 3
        review2.setComment("Bom");
        review2.setAvaliationDate(LocalDateTime.now());

        Review review3 = new Review();
        review3.setRating(Rating.FIVE_STARS); // valor 4
        review3.setComment("Excelente");
        review3.setAvaliationDate(LocalDateTime.now());

        entityManager.persistAndFlush(review1);
        entityManager.persistAndFlush(review2);
        entityManager.persistAndFlush(review3);

        // Testar cálculo da média
        Double averageRating = reviewRepository.getAverageRating();

        assertNotNull(averageRating);
        // Média de (1 + 3 + 4) / 3 = 2.67 aproximadamente
        assertEquals(2.67, averageRating, 0.1);
    }
}
