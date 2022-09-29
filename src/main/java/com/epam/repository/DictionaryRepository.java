package com.epam.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.domain.WordEntity;

public interface DictionaryRepository extends JpaRepository<WordEntity, Long> {

    Optional<WordEntity> findByValueIgnoreCase(String value);

    List<WordEntity> findByValueLikeIgnoreCase(String valuePart);

    boolean existsByValue(String value);

}
