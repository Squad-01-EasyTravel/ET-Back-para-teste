package com.decolatech.easytravel.domain.booking.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RatingTest {

    @Test
    void testRatingValues() {
        // Teste dos valores numéricos dos ratings
        assertEquals(0, Rating.ONE_STAR.getValue());
        assertEquals(1, Rating.TWO_STARS.getValue());
        assertEquals(2, Rating.THREE_STARS.getValue());
        assertEquals(3, Rating.FOUR_STARS.getValue());
        assertEquals(4, Rating.FIVE_STARS.getValue());
    }

    @Test
    void testFromValueMethod() {
        // Teste do método fromValue
        assertEquals(Rating.ONE_STAR, Rating.fromValue(0));
        assertEquals(Rating.TWO_STARS, Rating.fromValue(1));
        assertEquals(Rating.THREE_STARS, Rating.fromValue(2));
        assertEquals(Rating.FOUR_STARS, Rating.fromValue(3));
        assertEquals(Rating.FIVE_STARS, Rating.fromValue(4));
    }

    @Test
    void testFromValueInvalidInput() {
        // Teste com valores inválidos
        assertThrows(IllegalArgumentException.class, () -> Rating.fromValue(-1));
        assertThrows(IllegalArgumentException.class, () -> Rating.fromValue(5));
        assertThrows(IllegalArgumentException.class, () -> Rating.fromValue(10));
        assertThrows(IllegalArgumentException.class, () -> Rating.fromValue(100));
    }

    @Test
    void testRatingNames() {
        // Teste dos nomes dos enums
        assertEquals("ONE_STAR", Rating.ONE_STAR.name());
        assertEquals("TWO_STARS", Rating.TWO_STARS.name());
        assertEquals("THREE_STARS", Rating.THREE_STARS.name());
        assertEquals("FOUR_STARS", Rating.FOUR_STARS.name());
        assertEquals("FIVE_STARS", Rating.FIVE_STARS.name());
    }

    @Test
    void testRatingOrdinal() {
        // Teste da ordem dos enums
        assertEquals(0, Rating.ONE_STAR.ordinal());
        assertEquals(1, Rating.TWO_STARS.ordinal());
        assertEquals(2, Rating.THREE_STARS.ordinal());
        assertEquals(3, Rating.FOUR_STARS.ordinal());
        assertEquals(4, Rating.FIVE_STARS.ordinal());
    }

    @Test
    void testRatingValuesArray() {
        // Teste do array de valores
        Rating[] ratings = Rating.values();
        assertEquals(5, ratings.length);
        assertEquals(Rating.ONE_STAR, ratings[0]);
        assertEquals(Rating.TWO_STARS, ratings[1]);
        assertEquals(Rating.THREE_STARS, ratings[2]);
        assertEquals(Rating.FOUR_STARS, ratings[3]);
        assertEquals(Rating.FIVE_STARS, ratings[4]);
    }

    @Test
    void testRatingValueOf() {
        // Teste do método valueOf
        assertEquals(Rating.ONE_STAR, Rating.valueOf("ONE_STAR"));
        assertEquals(Rating.TWO_STARS, Rating.valueOf("TWO_STARS"));
        assertEquals(Rating.THREE_STARS, Rating.valueOf("THREE_STARS"));
        assertEquals(Rating.FOUR_STARS, Rating.valueOf("FOUR_STARS"));
        assertEquals(Rating.FIVE_STARS, Rating.valueOf("FIVE_STARS"));
    }

    @Test
    void testRatingValueOfInvalid() {
        // Teste com nomes inválidos
        assertThrows(IllegalArgumentException.class, () -> Rating.valueOf("INVALID"));
        assertThrows(IllegalArgumentException.class, () -> Rating.valueOf("SIX_STARS"));
        assertThrows(IllegalArgumentException.class, () -> Rating.valueOf(""));
        assertThrows(NullPointerException.class, () -> Rating.valueOf(null));
    }

    @Test
    void testRatingComparison() {
        // Teste de comparação entre ratings
        assertTrue(Rating.ONE_STAR.getValue() < Rating.FIVE_STARS.getValue());
        assertTrue(Rating.TWO_STARS.getValue() > Rating.ONE_STAR.getValue());
        assertTrue(Rating.THREE_STARS.getValue() == Rating.THREE_STARS.getValue());

        // Teste de ordenação natural
        assertTrue(Rating.ONE_STAR.compareTo(Rating.FIVE_STARS) < 0);
        assertTrue(Rating.FIVE_STARS.compareTo(Rating.ONE_STAR) > 0);
        assertTrue(Rating.THREE_STARS.compareTo(Rating.THREE_STARS) == 0);
    }

    @Test
    void testRatingEquality() {
        // Teste de igualdade
        assertEquals(Rating.ONE_STAR, Rating.ONE_STAR);
        assertEquals(Rating.FIVE_STARS, Rating.FIVE_STARS);
        assertNotEquals(Rating.ONE_STAR, Rating.FIVE_STARS);
        assertNotEquals(Rating.TWO_STARS, Rating.THREE_STARS);
    }

    @Test
    void testRatingHashCode() {
        // Teste de hashCode
        assertEquals(Rating.ONE_STAR.hashCode(), Rating.ONE_STAR.hashCode());
        assertEquals(Rating.FIVE_STARS.hashCode(), Rating.FIVE_STARS.hashCode());

        // Diferentes ratings devem ter hashcodes diferentes
        assertNotEquals(Rating.ONE_STAR.hashCode(), Rating.FIVE_STARS.hashCode());
    }
}
