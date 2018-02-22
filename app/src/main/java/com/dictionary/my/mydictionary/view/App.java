package com.dictionary.my.mydictionary.view;

import android.app.Application;

import com.dictionary.my.mydictionary.data.repository.cloud.SkyEngApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by luxso on 22.02.2018.
 */

public class App extends Application {
    private static SkyEngApi skyEngApi;
    private Retrofit retrofit;
    @Override
    public void onCreate() {
        super.onCreate();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://dictionary.skyeng.ru/api/public/v1/words/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        skyEngApi = retrofit.create(SkyEngApi.class);


    }

    public static SkyEngApi getSkyEngApi(){
        return skyEngApi;
    }
}
