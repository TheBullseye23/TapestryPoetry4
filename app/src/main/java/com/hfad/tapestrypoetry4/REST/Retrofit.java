package com.hfad.tapestrypoetry4.REST;

import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {

    private static retrofit2.Retrofit retrofit = null;
    private static final String BASE_URL = "https://api.datamuse.com/";


    public static retrofit2.Retrofit getRetrofitClient() {
        {
            if (retrofit == null) {
                retrofit = new retrofit2.Retrofit.Builder()
                        .baseUrl(BASE_URL).
                                addConverterFactory(GsonConverterFactory.create()).build();
            }

            return retrofit;
        }

    }

}
