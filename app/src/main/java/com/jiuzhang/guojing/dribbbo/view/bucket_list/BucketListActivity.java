package com.jiuzhang.guojing.dribbbo.view.bucket_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.jiuzhang.guojing.dribbbo.R;

public class BucketListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Choose bucket");

        if (savedInstanceState == null) {
            boolean isChoosingMode = getIntent().getExtras().getBoolean(
                    BucketListFragment.KEY_CHOOSING_MODE);
            BucketListFragment bucketListFragment = BucketListFragment.newInstance(
                    null, isChoosingMode);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, bucketListFragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
