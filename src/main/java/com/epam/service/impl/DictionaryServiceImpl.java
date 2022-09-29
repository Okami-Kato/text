package com.epam.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.config.TextProperties;
import com.epam.domain.WordEntity;
import com.epam.repository.DictionaryRepository;
import com.epam.repository.TextRepository;
import com.epam.service.DictionaryService;
import com.epam.service.dto.WordDto;
import com.epam.service.mapper.Mapper;

@Service
@Transactional
@RequiredArgsConstructor
public class DictionaryServiceImpl implements DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    private final TextRepository textRepository;

    private final Mapper<WordEntity, WordDto> mapper;

    private final TextProperties textProperties;

    @Override
    public void initDictionary(List<String> filePaths) {
        for (String filePath : filePaths) {
            Map<String, Long> dictionary = textRepository.getDictionary(filePath);
            dictionary.forEach(this::saveOrMerge);
        }
    }

    @Override
    public void destroyDictionary() {
        dictionaryRepository.deleteAll();
    }

    @Override
    public List<WordDto> findAll() {
        return dictionaryRepository.findAll().stream()
            .map(mapper::toDto)
            .toList();
    }

    @Override
    public List<WordDto> findByWordPart(String wordPart) {
        return dictionaryRepository.findByValueLikeIgnoreCase(wordPart).stream()
            .map(mapper::toDto)
            .toList();
    }

    @Override
    public void update(String oldWord, String newWord) {
        Optional<WordEntity> entity = dictionaryRepository.findByValueIgnoreCase(oldWord);
        if (entity.isPresent()) {
            saveOrMerge(newWord, entity.get().getFrequency());
            dictionaryRepository.delete(entity.get());
            for (String filePath : textProperties.getFilePaths()) {
                String text = textRepository.getText(filePath);
                String newText = text.replaceAll("(?i)%s".formatted(oldWord), newWord);
                textRepository.saveText(newText, filePath);
            }
        }
    }

    @Override
    public WordDto save(String word) {
        if (dictionaryRepository.existsByValue(word)) {
            throw new IllegalArgumentException("Word \"%s\" already exists in dictionary".formatted(word));
        }
        return mapper.toDto(dictionaryRepository.save(new WordEntity().setValue(word).setFrequency(0L)));
    }

    @Override
    public void delete(String word) {
        Optional<WordEntity> entity = dictionaryRepository.findByValueIgnoreCase(word);
        if (entity.isPresent()) {
            dictionaryRepository.delete(entity.get());
            for (String filePath : textProperties.getFilePaths()) {
                String text = textRepository.getText(filePath);
                String newText = text.replaceAll("(?i)%s".formatted(word), "");
                textRepository.saveText(newText, filePath);
            }
        }
    }

    private void saveOrMerge(String word, Long frequency) {
        Optional<WordEntity> entity = dictionaryRepository.findByValueIgnoreCase(word);
        if (entity.isPresent()) {
            dictionaryRepository.save(entity.get().setFrequency(frequency + entity.get().getFrequency()));
        } else {
            dictionaryRepository.save(new WordEntity().setValue(word).setFrequency(frequency));
        }
    }
}
