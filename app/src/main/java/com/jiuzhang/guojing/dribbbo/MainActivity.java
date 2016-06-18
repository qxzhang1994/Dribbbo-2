package com.jiuzhang.guojing.dribbbo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        ShotListFragment shotListFragment = new ShotListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, shotListFragment)
                .commit();
    }
}
