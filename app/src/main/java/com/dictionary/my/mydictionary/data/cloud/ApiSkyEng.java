package com.dictionary.my.mydictionary.data.cloud;



import com.dictionary.my.mydictionary.data.cloud.pojo.meaning.MeaningSkyEng;
import com.dictionary.my.mydictionary.data.cloud.pojo.word.WordSkyEng;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by luxso on 22.02.2018.
 */

public interface ApiSkyEng {
    @GET("words/search?_format=json")
    Call<ArrayList<WordSkyEng>> getWord(@Query("search") String word);

    @GET("meanings?_format=json")
    Call<ArrayList<MeaningSkyEng>> getMeaning(@Query("ids") String id);
}
