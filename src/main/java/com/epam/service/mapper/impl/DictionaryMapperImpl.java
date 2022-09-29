package com.epam.service.mapper.impl;

import org.springframework.stereotype.Component;

import com.epam.domain.WordEntity;
import com.epam.service.dto.WordDto;
import com.epam.service.mapper.DictionaryMapper;

@Component
public class DictionaryMapperImpl implements DictionaryMapper {

    @Override
    public WordEntity toEntity(WordDto dto) {
        return new WordEntity()
            .setId(dto.getId())
            .setValue(dto.getValue())
            .setFrequency(dto.getFrequency());
    }

    @Override
    public WordDto toDto(WordEntity entity) {
        return new WordDto()
            .setId(entity.getId())
            .setValue(entity.getValue())
            .setFrequency(entity.getFrequency());
    }
}
