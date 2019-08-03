package com.hfad.tapestrypoetry4.Data;

import java.util.List;

public class UserData {

    private List<String> keys;

    public UserData( List<String> keys) {
        this.keys = keys;
    }



    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
}
