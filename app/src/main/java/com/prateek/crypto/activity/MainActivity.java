package com.prateek.crypto.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.prateek.crypto.R;
import com.prateek.crypto.fragments.CryptoListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CryptoListFragment cryptoListFragment = new CryptoListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_container, cryptoListFragment).commit();
    }

}
