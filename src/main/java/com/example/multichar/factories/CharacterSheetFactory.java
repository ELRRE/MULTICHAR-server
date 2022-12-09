package com.example.multichar.factories;

import com.example.multichar.Entity.CharacterSheet;
import com.example.multichar.Entity.GameSystem;
import com.example.multichar.advices.CharSheetGenerateException;

import java.util.HashMap;

public class CharacterSheetFactory {
    public CharacterSheet createCharacterDataTemplate(GameSystem gameSystem) {
        CharacterSheet characterSheet = new CharacterSheet();
        characterSheet.setCharacterName("name");
        characterSheet.setRaceOrAnalogy("race");
        characterSheet.setLevelOrAnalogy("1");
        characterSheet.setClassOrAnalogy("class");
        characterSheet.setGameSystemType(gameSystem);
        switch (gameSystem)
        {
            case DnD:
                characterSheet.setData(createDnDTemplate());
                break;
            case VtM:
                createVtMTemplate();
                break;
            case savageWorlds:
                createSavageWorldsTemplate();
                break;
            default:
                throw new CharSheetGenerateException("character sheet is not generated");
        }
        return characterSheet;
    }

    private HashMap<String,Object> createDnDTemplate(){
        HashMap<String, Object> data = new HashMap<>();
        data.put("strength",8);
        data.put("Dexterity",8);
        data.put("Constitution",8);
        data.put("Wisdom",8);
        data.put("Charisma",8);
        data.put("intelligence",8);
        return data;
    }
    private HashMap<String,Object> createVtMTemplate(){
        return new HashMap<>();
    }
    private HashMap<String,Object> createSavageWorldsTemplate(){
        return new HashMap<>();
    }
}
