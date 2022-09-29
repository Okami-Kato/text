package com.epam.service;

import java.util.List;

import com.epam.service.dto.WordDto;

public interface DictionaryService {

    void initDictionary(List<String> filePaths);

    void destroyDictionary();

    List<WordDto> findAll();

    List<WordDto> findByWordPart(String wordPart);

    void update(String oldWord, String newWord);

    WordDto save(String word);

    void delete(String word);
}
