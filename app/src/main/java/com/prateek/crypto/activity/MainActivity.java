package com.prateek.crypto.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.prateek.crypto.R;
import com.prateek.crypto.fragments.CryptoListFragment;
import com.prateek.crypto.prefrences.UserPrefrenceManager;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    CryptoListFragment cryptoListFragment=new CryptoListFragment();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.frame_container, cryptoListFragment).commit();

                    return true;
                case R.id.navigation_dashboard:
                    Toast.makeText(MainActivity.this,"2nd",Toast.LENGTH_SHORT).show();
                    Log.e("HERE", UserPrefrenceManager.getFavourite(MainActivity.this).toString());
                    return true;
                case R.id.navigation_notifications:
                    Toast.makeText(MainActivity.this,"2nd",Toast.LENGTH_SHORT).show();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
