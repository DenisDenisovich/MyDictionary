package com.dictionary.my.mydictionary.data.repository.cloud;



import com.dictionary.my.mydictionary.data.entites.skyengapi.WordSkyEng;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by luxso on 22.02.2018.
 */

public interface SkyEngApi {
    @GET("search?_format=json")
    Call<ArrayList<WordSkyEng>> getAlterTranslations(@Query("search") String word);
}
