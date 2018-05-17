package com.prateek.crypto.prefrences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by prate on 23-03-2018.
 */

public class UserPrefrenceManager {
    private static SharedPreferences mSharedPreferences;
    private static final String PREF_NAME = "CRYPTONET";

    private static String FAV_CRYPTOS = "fav_cryptos";

    private static void init(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

    }

    public static void setFavourite(Context mContext,String favCoins){
        if(mSharedPreferences==null)
            init(mContext);
        SharedPreferences.Editor editor=mSharedPreferences.edit();
        Set<String> set=  mSharedPreferences.getStringSet(FAV_CRYPTOS,new HashSet<String>());
        set.add(favCoins);
        editor.clear();
        editor.putStringSet(FAV_CRYPTOS, set);
        editor.apply();



    }

    public static Set<String> getFavourite(Context mContext){
        if(mSharedPreferences==null)
            init(mContext);

        Set<String> set=  mSharedPreferences.getStringSet(FAV_CRYPTOS,new HashSet<String>());
        return set;

    }

    public static void removeFavourite(Context mContext,String str){
        if(mSharedPreferences==null)
            init(mContext);
        Set<String> defValue=new HashSet<>();
        Set<String> set= mSharedPreferences.getStringSet(FAV_CRYPTOS,defValue);
        set.remove(str);
        SharedPreferences.Editor editor=mSharedPreferences.edit();
        editor.clear();
        editor.putStringSet(FAV_CRYPTOS, set);
        editor.apply();

    }
}
