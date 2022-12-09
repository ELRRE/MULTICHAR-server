package com.example.multichar.repositories;

import com.example.multichar.Entity.CharacterSheet;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CharSheetRepository extends CrudRepository<CharacterSheet, Long> {
    Iterable<CharacterSheet> findAllByUser_Name(String username);

    Optional<CharacterSheet> findByIdAndUser_Name(Long id,String username);

    boolean existsByIdAndUser_Name(Long id, String username);
}
