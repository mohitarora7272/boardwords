package com.boardwords.modal;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;


public class Character implements Serializable {
    private static final long serialVersionUID = -222864131214757024L;

    public static final String ID = "id";
    public static final String CHARACTERS = "characters";

    @DatabaseField(generatedId = true, columnName = ID)
    public int id;
    @DatabaseField(columnName = CHARACTERS)
    public String characters;

    /* Default Constructor */
    public Character() {
    }

    public String getCharacters() {
        return characters;
    }

    public void setCharacters(String characters) {
        this.characters = characters;
    }


}
