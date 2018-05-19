package com.prateek.crypto.network;

import com.prateek.crypto.models.CoinListResponse;

import retrofit2.Call;
import retrofit2.http.GET;


/***Created by prate on 23-03-2018.**/

public interface NetworkService {

    @GET("ticker/?limit=0")
    Call<CoinListResponse> getAllCoinList();
}
