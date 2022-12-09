package com.example.multichar.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.Type;
import org.hibernate.type.descriptor.sql.LobTypeMappings;

@Entity
public class CharacterSheet {
    @Transient
    ObjectMapper objectMapper = new ObjectMapper();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String characterName;
    private String levelOrAnalogy;
    private String classOrAnalogy;
    private String raceOrAnalogy;
    private GameSystem gameSystemType;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String data;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    public void setCharacterSheetDetails(CharacterSheet newCharacterSheet){
        characterName = newCharacterSheet.characterName;
        levelOrAnalogy = newCharacterSheet.levelOrAnalogy;
        classOrAnalogy = newCharacterSheet.classOrAnalogy;
        raceOrAnalogy = newCharacterSheet.raceOrAnalogy;
        gameSystemType = newCharacterSheet.gameSystemType;
        data = newCharacterSheet.data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getLevelOrAnalogy() {
        return levelOrAnalogy;
    }

    public void setLevelOrAnalogy(String levelOrAnalogy) {
        this.levelOrAnalogy = levelOrAnalogy;
    }

    public String getClassOrAnalogy() {
        return classOrAnalogy;
    }

    public void setClassOrAnalogy(String classOrAnalogy) {
        this.classOrAnalogy = classOrAnalogy;
    }

    public String getRaceOrAnalogy() {
        return raceOrAnalogy;
    }

    public void setRaceOrAnalogy(String raceOrAnalogy) {
        this.raceOrAnalogy = raceOrAnalogy;
    }

    public GameSystem getGameSystemType() {
        return gameSystemType;
    }

    public void setGameSystemType(GameSystem gameSystemType) {
        this.gameSystemType = gameSystemType;
    }

    public HashMap<String, Object> getData(){
        try {
            return objectMapper.readValue(data, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("character data is not get");
        }
    }

    public void setData(HashMap<String,Object> data){
        try {
            this.data = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        } catch (Exception e) {
            throw new RuntimeException("character data is not set");
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        user.getCharacterSheet().add(this);
    }
}
