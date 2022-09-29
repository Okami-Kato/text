package com.epam.repository;

import java.util.Map;

public interface TextRepository {

    Map<String, Long> getDictionary(String filePath);

    String getText(String filePath);

    void saveText(String text, String filePath);
}
