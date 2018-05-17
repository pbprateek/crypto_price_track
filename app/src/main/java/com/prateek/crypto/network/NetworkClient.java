package com.prateek.crypto.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by prate on 23-03-2018.
 */

public class NetworkClient {
    public static final String TEST_BASE_URL = "";
    public static final String LIVE_BASE_URL = "https://min-api.cryptocompare.com/data/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(LIVE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
