package com.example.multichar.service;

import com.example.multichar.Entity.CharacterSheet;
import com.example.multichar.Entity.GameSystem;
import com.example.multichar.factories.CharacterSheetFactory;
import com.example.multichar.repositories.CharSheetRepository;
import com.example.multichar.repositories.UserRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Component
public class CharsService {
    CharSheetRepository charSheetRepository;
    UserRepository userRepository;

    public CharsService(CharSheetRepository charSheetRepository, UserRepository userRepository) {
        this.charSheetRepository = charSheetRepository;
        this.userRepository = userRepository;
    }

    public List<CharacterSheet> getAllForUser(String username){
        if (!userRepository.existsByName(username))
            throw new EntityNotFoundException("user not found");
        return (List<CharacterSheet>) charSheetRepository.findAllByUser_Name(username);
    }
    public CharacterSheet getCharSheet(Long id,String username){
        return charSheetRepository.findByIdAndUser_Name(id,username)
                .orElseThrow(()->new EntityNotFoundException("character sheet not found"));
    }
    public CharacterSheet create(String username, GameSystem gameSystem){
        CharacterSheetFactory factory = new CharacterSheetFactory();
        CharacterSheet characterSheet = factory.createCharacterDataTemplate(gameSystem);
        characterSheet.setUser(userRepository.findByName(username)
                .orElseThrow(()->new EntityNotFoundException("user not found")));
        charSheetRepository.save(characterSheet);
        return characterSheet;
    }
    public void delete(Long id){
        charSheetRepository.delete(
                charSheetRepository.findById(id)
                        .orElseThrow(()->new EntityNotFoundException("character sheet not found"))
        );
    }
    public void updateCharSheet(CharacterSheet newCharacterSheet, Long oldCharacterSheetId) {
        CharacterSheet oldCharacterSheet= charSheetRepository.findById(oldCharacterSheetId)
                .orElseThrow(()->new EntityNotFoundException("character sheet not found"));
        oldCharacterSheet.setCharacterSheetDetails(newCharacterSheet);
        charSheetRepository.save(oldCharacterSheet);
    }
    public boolean accessToCharSheet(Long oldCharacterSheetId, String username){
        return charSheetRepository.existsByIdAndUser_Name(oldCharacterSheetId,username);
    }
}
