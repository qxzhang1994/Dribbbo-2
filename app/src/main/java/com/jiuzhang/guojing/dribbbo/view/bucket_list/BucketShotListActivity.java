package com.jiuzhang.guojing.dribbbo.view.bucket_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.jiuzhang.guojing.dribbbo.R;
import com.jiuzhang.guojing.dribbbo.dribbble.Dribbble;
import com.jiuzhang.guojing.dribbbo.view.shot_list.ShotListFragment;

public class BucketShotListActivity extends AppCompatActivity {

    public static final String KEY_BUCKET_NAME = "bucketName";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Bundle extras = getIntent().getExtras();
        String bucketName = extras.getString(KEY_BUCKET_NAME);
        setTitle(bucketName);

        if (savedInstanceState == null) {
            String bucketId = extras.getString(ShotListFragment.KEY_BUCKET_ID);
            ShotListFragment shotListFragment = bucketId == null
                    ? ShotListFragment.newInstance(ShotListFragment.LIST_TYPE_POPULAR)
                    : ShotListFragment.newBucketListInstance(bucketId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, shotListFragment)
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
