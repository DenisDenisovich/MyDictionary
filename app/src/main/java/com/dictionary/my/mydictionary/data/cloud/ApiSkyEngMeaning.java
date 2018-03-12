package com.dictionary.my.mydictionary.data.cloud;

import com.dictionary.my.mydictionary.data.entites.skyengapi.meaning.MeaningSkyEng;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by luxso on 11.03.2018.
 */

public interface ApiSkyEngMeaning {
    @GET("meanings?_format=json")
    Call<ArrayList<MeaningSkyEng>> getMeaning(@Query("ids") Integer id);
}
