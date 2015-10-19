package com.mr_faton.core.table;

import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 19.10.2015
 */
public class Synonym {
    private int id;
    private String word;
    private List<String> synonyms;
    private int used;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }
    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public int getUsed() {
        return used;
    }
    public void setUsed(int used) {
        this.used = used;
    }

    public String getSynonymsAsString() {
        String synonymsStr = "";
        for (String synonym : synonyms) {
            synonymsStr += synonym + ",";
        }
        return synonymsStr.substring(0, synonymsStr.lastIndexOf(","));
    }

    @Override
    public String toString() {
        return word + " = " + getSynonymsAsString();
    }
}
