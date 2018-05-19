package com.prateek.crypto.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@JsonAdapter(CoinListResponse.DataJsonAdapter.class)
public class CoinListResponse {

    private ArrayList<Coin> coinsList=new ArrayList<>();

    public ArrayList<Coin> getCoinsList(){
        return coinsList;

    }

    public void setCoinsList(ArrayList coinsList1) {
        this.coinsList=coinsList1;
    }




    public static class DataJsonAdapter extends TypeAdapter<CoinListResponse> {

        @Override
        public void write(JsonWriter out, CoinListResponse value) throws IOException {
            // TODO: Maybe implement
        }

        @Override
        public CoinListResponse read(JsonReader reader) throws IOException {
            Gson gson = new Gson();

            Type listType = new TypeToken<List<Coin>>(){}.getType();
            ArrayList<Coin> posts = gson.fromJson(reader, listType);

            CoinListResponse data = new CoinListResponse();
            // Can probably be simplified
            data.setCoinsList(posts);


            return data;
        }
    }
}
