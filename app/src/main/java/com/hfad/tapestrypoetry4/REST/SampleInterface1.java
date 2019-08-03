package com.hfad.tapestrypoetry4.REST;

import com.hfad.tapestrypoetry4.Data.WORD;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface SampleInterface1 {

        @GET()
        Call<List<WORD>> getSynonyms(@Url String Url);

        @GET()
        Call<List<WORD>> getRhyme(@Url String Url);

}
