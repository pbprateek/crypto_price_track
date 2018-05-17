package com.prateek.crypto.utilities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;

import com.prateek.crypto.models.Coin;

/**
 * Created by prate on 20-01-2018.
 */

public class  CoinListSingleton {
    private ArrayList<Coin> coins=new ArrayList<>();
    private static CoinListSingleton coinListSingleton=null;

    public static CoinListSingleton getInstance(){
        if(coinListSingleton==null){
            coinListSingleton= new CoinListSingleton();}
        return coinListSingleton;
    }
    public void updateList(ArrayList<Coin> coins){
        coins.addAll(coins);
    }
    public ArrayList<Coin> getList(){
        return coins;
    }
}