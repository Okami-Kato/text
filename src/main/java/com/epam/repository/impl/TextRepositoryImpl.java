package com.epam.repository.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.io.Files;
import org.springframework.stereotype.Repository;

import com.epam.repository.TextRepository;

@Repository
public class TextRepositoryImpl implements TextRepository {

    @Override
    public Map<String, Long> getDictionary(String filePath) {
        Map<String, Long> result = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.lines()
                .map(line -> {
                    Pattern pattern = Pattern.compile("([a-zA-Z]+'?[a-zA-Z]+)");
                    return pattern.matcher(line);
                })
                .flatMap(matcher -> {
                    List<String> words = new ArrayList<>();
                    while(matcher.find()) {
                        words.add(matcher.group(0).toLowerCase());
                    }
                    return words.stream();
                })
                .forEach(word -> result.put(word.toLowerCase(), result.getOrDefault(word.toLowerCase(), 0L) + 1));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found : %s".formatted(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while reading file %s".formatted(filePath));
        }
        return result;
    }

    @Override
    public String getText(String filePath) {
        try {
            File file = new File(filePath);
            return Files.asCharSource(file, StandardCharsets.UTF_8).read();
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while reading file %s".formatted(filePath));
        }
    }

    @Override
    public void saveText(String text, String filePath) {
        try (FileWriter writer = new FileWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write(text);
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while reading file %s".formatted(filePath));
        }
    }
}
