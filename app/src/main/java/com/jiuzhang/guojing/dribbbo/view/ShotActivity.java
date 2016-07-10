package com.jiuzhang.guojing.dribbbo.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jiuzhang.guojing.dribbbo.R;
import com.jiuzhang.guojing.dribbbo.view.shot_detail.ShotFragment;

public class ShotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        ShotFragment shotFragment = new ShotFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, shotFragment)
                .commit();
    }
}
