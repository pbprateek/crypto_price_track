
package com.prateek.crypto.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;


@JsonAdapter(Data.DataJsonAdapter.class)
public class Data {
    private Coin[] coins;
    private ArrayList<Coin> coinsList=new ArrayList<>();

    public ArrayList<Coin> getCoinsList(){
        return coinsList;

    }

    public void setCoinsList(ArrayList coinsList1) {
        this.coinsList=coinsList1;
    }

    public Coin[] getCoin() {
        return coins;
    }

    public void setCoins(Coin[] coins) {
        this.coins = coins;
    }

    public static class DataJsonAdapter extends TypeAdapter<Data> {
        @Override
        public void write(JsonWriter out, Data value) throws IOException {
            // TODO: Maybe implement
        }

        @Override
        public Data read(JsonReader reader) throws IOException {
            ArrayList<Object> coins = new ArrayList<>();
            Gson gson = new Gson();
            reader.beginObject();
            while (reader.hasNext()) {
                reader.nextName(); // Read "42", "365", ...
                coins.add(gson.fromJson(reader, Coin.class));
            }
            reader.endObject();

            Data data = new Data();
            // Can probably be simplified
            Coin[] array = new Coin[coins.size()];
            coins.toArray(array); // fill the array
            data.setCoins(array);
            data.setCoinsList(coins);


            return data;
        }
    }
}
