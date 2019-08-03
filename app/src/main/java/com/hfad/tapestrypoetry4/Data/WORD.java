package com.hfad.tapestrypoetry4.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WORD {

    @SerializedName("word")
    @Expose
    private String word;
    @SerializedName("score")
    @Expose
    private Integer score;
    @SerializedName("tags")
    @Expose
    private List<String> tags = null;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
