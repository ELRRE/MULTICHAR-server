package com.example.multichar.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String pass;
    @JsonIgnore
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)

    @PrimaryKeyJoinColumn
    private Tokens tokens;
    //@JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CharacterSheet> characterSheet;

    public Long getId() {
        return id;
    }

    public User() {
    }

    public User(String name, String email, String pass) {
        this.name = name;
        this.email = email;
        this.pass = pass;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Tokens getTokens() {
        return tokens;
    }

    public void setTokens(Tokens tokens) {
        this.tokens=tokens;
        this.tokens.setUser(this);
    }

    public Set<CharacterSheet> getCharacterSheet() {
        return characterSheet;
    }

    public void setCharacterSheet(Set<CharacterSheet> characterSheet) {
        this.characterSheet = characterSheet;
    }

}
