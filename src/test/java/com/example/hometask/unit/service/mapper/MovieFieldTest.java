package com.example.hometask.unit.service.mapper;

import com.example.hometask.service.mapper.MovieField;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieFieldTest {

    @Test
    void parseFields() {
        String input = "title,genre,duration";
        List<MovieField> result = MovieField.parseFields(input);

        assertEquals(3, result.size());
        assertTrue(result.contains(MovieField.TITLE));
        assertTrue(result.contains(MovieField.GENRE));
        assertTrue(result.contains(MovieField.DURATION));

    }

    @Test
    void testParseFields_InvalidValue() {
        String input = "title,SomethingShouldNotBeHere,releaseYear";
        List<MovieField> result = MovieField.parseFields(input);

        assertNotNull(result);
        assertTrue(result.contains(MovieField.TITLE));
        assertTrue(result.contains(MovieField.YEAR));
        assertEquals(2, result.size());
    }


    @Test
    void testParseFields_Empty() {
        String input = "";
        List<MovieField> result = MovieField.parseFields(input);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseFields_Null() {
        String input = null;
        List<MovieField> result = MovieField.parseFields(input);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseFields_Whitespace() {
        String input = "  ";
        List<MovieField> result = MovieField.parseFields(input);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseFields_BadDelimiter() {
        String input = "title;genre;title";
        List<MovieField> result = MovieField.parseFields(input);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseFields_CaseInsensitive() {
        String input = "TITLE,Genre,RATING";
        List<MovieField> result = MovieField.parseFields(input);

        assertNotNull(result);
        assertTrue(result.contains(MovieField.TITLE));
        assertTrue(result.contains(MovieField.GENRE));
        assertTrue(result.contains(MovieField.RATING));
        assertEquals(3, result.size());
    }

    @Test
    void testParseFields_TrimmedInput() {
        String input = " title , genre , rating ";
        List<MovieField> result = MovieField.parseFields(input);

        assertNotNull(result);
        assertTrue(result.contains(MovieField.TITLE));
        assertTrue(result.contains(MovieField.GENRE));
        assertTrue(result.contains(MovieField.RATING));
        assertEquals(3, result.size());
    }


    @Test
    void getFieldName() {
        assertEquals("title", MovieField.TITLE.getFieldName());
        assertEquals("genre", MovieField.GENRE.getFieldName());
        assertEquals("duration", MovieField.DURATION.getFieldName());
        assertEquals("rating", MovieField.RATING.getFieldName());
        assertEquals("releaseYear", MovieField.YEAR.getFieldName());
    }

    @Test
    void values() {
        assertEquals(5, MovieField.values().length);
    }

    @Test
    void valueOf() {
            assertEquals(MovieField.TITLE, MovieField.valueOf("TITLE"));
            assertEquals(MovieField.YEAR, MovieField.valueOf("YEAR"));
            assertEquals(MovieField.DURATION, MovieField.valueOf("DURATION"));
    }
}