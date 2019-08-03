package com.hfad.tapestrypoetry4.Data;

import java.util.List;

public class PoemData {

    private String Title;
    private String time;
    private String poemLines;
    private List<String> Authors;
    private String key;
    private List<String> Latitudes;
    private List<String> Longitudes;

    public PoemData() {
    }

    public PoemData(String title, String time, String poemLines, List<String> authors, String key, List<String> latitudes, List<String> longitudes) {
        Title = title;
        this.time = time;
        this.poemLines = poemLines;
        Authors = authors;
        this.key = key;
        Latitudes = latitudes;
        Longitudes = longitudes;
    }

    public List<String> getLatitudes() {
        return Latitudes;
    }

    public void setLatitudes(List<String> latitudes) {
        Latitudes = latitudes;
    }

    public List<String> getLongitudes() {
        return Longitudes;
    }

    public void setLongitudes(List<String> longitudes) {
        Longitudes = longitudes;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPoemLines() {
        return poemLines;
    }

    public void setPoemLines(String poemLines) {
        this.poemLines = poemLines;
    }

    public List<String> getAuthors() {
        return Authors;
    }

    public void setAuthors(List<String> authors) {
        Authors = authors;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
