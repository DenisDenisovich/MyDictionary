package com.dictionary.my.mydictionary.data.cloud;



import com.dictionary.my.mydictionary.data.entites.skyengapi.word.WordSkyEng;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by luxso on 22.02.2018.
 */

public interface ApiSkyEngWord {
    @GET("words/search?_format=json")
    Call<ArrayList<WordSkyEng>> getWord(@Query("search") String word);
}
