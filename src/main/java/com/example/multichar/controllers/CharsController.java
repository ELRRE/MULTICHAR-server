package com.example.multichar.controllers;

import com.example.multichar.Entity.CharacterSheet;
import com.example.multichar.Entity.GameSystem;
import com.example.multichar.service.CharsService;

import com.example.multichar.service.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/chars")
public class CharsController {
    final JwtProvider jwtProvider;
    private final CharsService charsService;
    @Autowired
    public CharsController(JwtProvider jwtProvider, CharsService charsService) {
        this.jwtProvider = jwtProvider;
        this.charsService = charsService;
    }
    @CrossOrigin
    @GetMapping(value = "/getAll")
    public ResponseEntity<List<CharacterSheet>> getAllCharsSheets(@RequestHeader("Token") String token) {
        List<CharacterSheet> characterSheetList = charsService.getAllForUser(jwtProvider.getAccessSubject(token));
        return new ResponseEntity<>(characterSheetList,HttpStatus.OK);
    }
    @GetMapping(value = "/getChar/{id}")
    public ResponseEntity<CharacterSheet> getCharSheet(@PathVariable Long id, @RequestHeader("Token") String token){
            CharacterSheet characterSheet = charsService.getCharSheet(id, jwtProvider.getAccessSubject(token));
            return new ResponseEntity<>(characterSheet,HttpStatus.OK);
    }
    @PutMapping(value = "/create")
    public ResponseEntity<CharacterSheet> createCharSheet( @RequestHeader("Token") String token, @RequestBody Map<String,String> body){
        CharacterSheet characterSheet = charsService.create(
                jwtProvider.getAccessSubject(token),
                GameSystem.valueOf(body.get("gameSystemType"))
        );
        return new ResponseEntity<>(characterSheet,HttpStatus.OK);
    }
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteCharSheet(@RequestHeader("Token") String token, @PathVariable Long id){
        if(!charsService.accessToCharSheet(id, jwtProvider.getAccessSubject(token)))
            throw new SecurityException("character sheet not found");
        charsService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/change/{id}")
    public ResponseEntity<?> changeCharSheet(@RequestHeader("Token") String token,
                                             @RequestBody CharacterSheet newCharacterSheet,
                                             @PathVariable Long id){
        if(!charsService.accessToCharSheet(id, jwtProvider.getAccessSubject(token)))
            throw new SecurityException("character sheet not found");
        charsService.updateCharSheet(newCharacterSheet, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
