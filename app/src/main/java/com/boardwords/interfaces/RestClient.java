package com.boardwords.interfaces;


import com.boardwords.modal.WordsApi;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

@SuppressWarnings("all")
public interface RestClient {


    @GET("./")
    Call<WordsApi> wordsRequest();

}
