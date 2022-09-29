package com.epam.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.epam.config.TextProperties;
import com.epam.repository.TextRepository;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@TestInstance(PER_CLASS)
class TextRepositoryImplTest {

    @Autowired
    private TextProperties properties;

    @Autowired
    private TextRepository textRepository;

    private String filePath;

    private final Map<String, Long> expectedDictionary = Map.of(
        "hello", 1L,
        "my", 2L,
        "name", 1L,
        "mark", 1L,
        "can't", 1L,
        "it's", 1L
    );

    private final String expectedText = "Hello, my name my Mark! can't? It's)";

    @BeforeAll
    void setUp() {
        filePath = properties.getFilePaths().get(0);
    }

    @Test
    void getDictionary() {
        Map<String, Long> actualDictionary = textRepository.getDictionary(filePath);
        assertEquals(expectedDictionary, actualDictionary);
    }

    @Test
    void getText() {
        assertEquals(expectedText, textRepository.getText(filePath));
    }

    @Test
    void saveText() {
        String newText = "New text";
        textRepository.saveText(newText, filePath);
        assertEquals(newText, textRepository.getText(filePath));
    }

    @AfterEach
    void finish() {
        textRepository.saveText(expectedText, filePath);
    }
}