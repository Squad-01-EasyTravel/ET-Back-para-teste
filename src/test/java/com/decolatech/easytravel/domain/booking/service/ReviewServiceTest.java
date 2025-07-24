package com.decolatech.easytravel.domain.booking.service;

import com.decolatech.easytravel.domain.booking.dto.ReviewDTO;
import com.decolatech.easytravel.domain.booking.entity.Review;
import com.decolatech.easytravel.domain.booking.enums.Rating;
import com.decolatech.easytravel.domain.booking.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Review sampleReview;
    private ReviewDTO sampleReviewDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        sampleReview = new Review();
        sampleReview.setId(1);
        sampleReview.setRating(Rating.FIVE_STARS);
        sampleReview.setComment("Excelente!");
        sampleReview.setAvaliationDate(now);

        sampleReviewDTO = new ReviewDTO();
        sampleReviewDTO.setId(1);
        sampleReviewDTO.setRating(Rating.FIVE_STARS);
        sampleReviewDTO.setComment("Excelente!");
        sampleReviewDTO.setAvaliationDate(now);
    }

    @Test
    void testGetAllReviews() {
        // Teste para buscar todas as reviews
        List<Review> reviews = Arrays.asList(sampleReview);
        when(reviewRepository.findAll()).thenReturn(reviews);

        List<ReviewDTO> result = reviewService.getAllReviews();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleReview.getId(), result.get(0).getId());
        assertEquals(sampleReview.getRating(), result.get(0).getRating());
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    void testGetReviewByIdFound() {
        // Teste para buscar review por ID existente
        when(reviewRepository.findById(1)).thenReturn(Optional.of(sampleReview));

        Optional<ReviewDTO> result = reviewService.getReviewById(1);

        assertTrue(result.isPresent());
        assertEquals(sampleReview.getId(), result.get().getId());
        assertEquals(sampleReview.getRating(), result.get().getRating());
        verify(reviewRepository, times(1)).findById(1);
    }

    @Test
    void testGetReviewByIdNotFound() {
        // Teste para buscar review por ID inexistente
        when(reviewRepository.findById(anyInt())).thenReturn(Optional.empty());

        Optional<ReviewDTO> result = reviewService.getReviewById(999);

        assertFalse(result.isPresent());
        verify(reviewRepository, times(1)).findById(999);
    }

    @Test
    void testCreateReview() {
        // Teste para criar nova review
        ReviewDTO inputDTO = new ReviewDTO();
        inputDTO.setRating(Rating.FOUR_STARS);
        inputDTO.setComment("Muito bom!");

        Review savedReview = new Review();
        savedReview.setId(2);
        savedReview.setRating(Rating.FOUR_STARS);
        savedReview.setComment("Muito bom!");
        savedReview.setAvaliationDate(LocalDateTime.now());

        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);

        ReviewDTO result = reviewService.createReview(inputDTO);

        assertNotNull(result);
        assertEquals(savedReview.getId(), result.getId());
        assertEquals(savedReview.getRating(), result.getRating());
        assertEquals(savedReview.getComment(), result.getComment());
        assertNotNull(result.getAvaliationDate());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testUpdateReviewSuccess() {
        // Teste para atualizar review existente
        ReviewDTO updateDTO = new ReviewDTO();
        updateDTO.setRating(Rating.THREE_STARS);
        updateDTO.setComment("Atualizado!");

        Review updatedReview = new Review();
        updatedReview.setId(1);
        updatedReview.setRating(Rating.THREE_STARS);
        updatedReview.setComment("Atualizado!");
        updatedReview.setAvaliationDate(sampleReview.getAvaliationDate());

        when(reviewRepository.findById(1)).thenReturn(Optional.of(sampleReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(updatedReview);

        ReviewDTO result = reviewService.updateReview(1, updateDTO);

        assertNotNull(result);
        assertEquals(Rating.THREE_STARS, result.getRating());
        assertEquals("Atualizado!", result.getComment());
        verify(reviewRepository, times(1)).findById(1);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testUpdateReviewNotFound() {
        // Teste para atualizar review inexistente
        ReviewDTO updateDTO = new ReviewDTO();
        updateDTO.setRating(Rating.TWO_STARS);

        when(reviewRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            reviewService.updateReview(999, updateDTO);
        });

        verify(reviewRepository, times(1)).findById(999);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void testDeleteReview() {
        // Teste para deletar review
        when(reviewRepository.existsById(1)).thenReturn(true);
        doNothing().when(reviewRepository).deleteById(1);

        assertDoesNotThrow(() -> reviewService.deleteReview(1));

        verify(reviewRepository, times(1)).deleteById(1);
    }
}
