package com.decolatech.easytravel.domain.booking.enums;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class RatingJsonTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testRatingJsonSerialization() throws Exception {
        // Teste de serialização JSON - deve serializar como número
        assertEquals("0", objectMapper.writeValueAsString(Rating.ONE_STAR));
        assertEquals("1", objectMapper.writeValueAsString(Rating.TWO_STARS));
        assertEquals("2", objectMapper.writeValueAsString(Rating.THREE_STARS));
        assertEquals("3", objectMapper.writeValueAsString(Rating.FOUR_STARS));
        assertEquals("4", objectMapper.writeValueAsString(Rating.FIVE_STARS));
    }

    @Test
    void testRatingJsonDeserialization() throws Exception {
        // Teste de deserialização JSON - deve aceitar números
        assertEquals(Rating.ONE_STAR, objectMapper.readValue("0", Rating.class));
        assertEquals(Rating.TWO_STARS, objectMapper.readValue("1", Rating.class));
        assertEquals(Rating.THREE_STARS, objectMapper.readValue("2", Rating.class));
        assertEquals(Rating.FOUR_STARS, objectMapper.readValue("3", Rating.class));
        assertEquals(Rating.FIVE_STARS, objectMapper.readValue("4", Rating.class));
    }

    @Test
    void testRatingJsonDeserializationWithStrings() throws Exception {
        // Teste de deserialização JSON - deve aceitar strings também
        assertEquals(Rating.ONE_STAR, objectMapper.readValue("\"ONE_STAR\"", Rating.class));
        assertEquals(Rating.TWO_STARS, objectMapper.readValue("\"TWO_STARS\"", Rating.class));
        assertEquals(Rating.THREE_STARS, objectMapper.readValue("\"THREE_STARS\"", Rating.class));
        assertEquals(Rating.FOUR_STARS, objectMapper.readValue("\"FOUR_STARS\"", Rating.class));
        assertEquals(Rating.FIVE_STARS, objectMapper.readValue("\"FIVE_STARS\"", Rating.class));
    }

    @Test
    void testRatingJsonInvalidValues() {
        // Teste com valores inválidos
        assertThrows(Exception.class, () -> objectMapper.readValue("5", Rating.class));
        assertThrows(Exception.class, () -> objectMapper.readValue("-1", Rating.class));
        assertThrows(Exception.class, () -> objectMapper.readValue("\"INVALID\"", Rating.class));
        assertThrows(Exception.class, () -> objectMapper.readValue("null", Rating.class));
    }

    @Test
    void testRatingInComplexObject() throws Exception {
        // Teste em um objeto complexo
        String json = "{\"rating\": 4, \"comment\": \"Excelente!\"}";

        // Simular um objeto que contém Rating
        ObjectMapper mapper = new ObjectMapper();
        var node = mapper.readTree(json);

        assertEquals(4, node.get("rating").asInt());
        assertEquals("Excelente!", node.get("comment").asText());
    }

    @Test
    void testRatingArraySerialization() throws Exception {
        // Teste de serialização de array de ratings
        Rating[] ratings = {Rating.ONE_STAR, Rating.THREE_STARS, Rating.FIVE_STARS};
        String json = objectMapper.writeValueAsString(ratings);

        assertEquals("[0,2,4]", json);
    }

    @Test
    void testRatingArrayDeserialization() throws Exception {
        // Teste de deserialização de array de ratings
        String json = "[0,2,4]";
        Rating[] ratings = objectMapper.readValue(json, Rating[].class);

        assertEquals(3, ratings.length);
        assertEquals(Rating.ONE_STAR, ratings[0]);
        assertEquals(Rating.THREE_STARS, ratings[1]);
        assertEquals(Rating.FIVE_STARS, ratings[2]);
    }
}
