package com.jiuzhang.guojing.dribbbo.view.bucket_list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiuzhang.guojing.dribbbo.R;
import com.jiuzhang.guojing.dribbbo.dribbble.Dribbble;
import com.jiuzhang.guojing.dribbbo.dribbble.DribbbleException;
import com.jiuzhang.guojing.dribbbo.model.Bucket;
import com.jiuzhang.guojing.dribbbo.view.base.DribbbleTask;
import com.jiuzhang.guojing.dribbbo.view.base.InfiniteAdapter;
import com.jiuzhang.guojing.dribbbo.view.base.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BucketListFragment extends Fragment {

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_CHOOSING_MODE = "choosing_mode";

    public static final int REQ_CODE_NEW_BUCKET = 100;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fab) FloatingActionButton fab;

    private BucketListAdapter adapter;

    private String userId;
    private boolean isChoosingMode;

    private InfiniteAdapter.LoadMoreListener onLoadMore = new InfiniteAdapter.LoadMoreListener() {
        @Override
        public void onLoadMore() {
            AsyncTaskCompat.executeParallel(new LoadBucketsTask(false));
        }
    };

    public static BucketListFragment newInstance(@Nullable String userId,
                                                 boolean isChoosingMode) {
        Bundle args = new Bundle();
        args.putString(KEY_USER_ID, userId);
        args.putBoolean(KEY_CHOOSING_MODE, isChoosingMode);

        BucketListFragment fragment = new BucketListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swipe_fab_recycler_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        userId = getArguments().getString(KEY_USER_ID);
        isChoosingMode = getArguments().getBoolean(KEY_CHOOSING_MODE);

        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTaskCompat.executeParallel(new LoadBucketsTask(true));
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.spacing_medium)));

        adapter = new BucketListAdapter(getContext(), new ArrayList<Bucket>(), onLoadMore);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewBucketDialogFragment dialogFragment = NewBucketDialogFragment.newInstance();
                dialogFragment.setTargetFragment(BucketListFragment.this, REQ_CODE_NEW_BUCKET);
                dialogFragment.show(getFragmentManager(), NewBucketDialogFragment.TAG);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_NEW_BUCKET && resultCode == Activity.RESULT_OK) {
            String bucketName = data.getStringExtra(NewBucketDialogFragment.KEY_BUCKET_NAME);
            String bucketDescription = data.getStringExtra(NewBucketDialogFragment.KEY_BUCKET_DESCRIPTION);
            if (!TextUtils.isEmpty(bucketName)) {
                AsyncTaskCompat.executeParallel(new NewBucketTask(bucketName, bucketDescription));
            }
        }
    }

    private class LoadBucketsTask extends DribbbleTask<Void, Void, List<Bucket>> {

        private boolean refresh;

        public LoadBucketsTask(boolean refresh) {
            this.refresh = refresh;
        }

        @Override
        protected List<Bucket> doJob(Void... params) throws DribbbleException {
            final int page = adapter.getData().size() / Dribbble.COUNT_PER_LOAD + 1;
            return userId == null
                    ? Dribbble.getUserBuckets(page)
                    : Dribbble.getUserBuckets(userId, page);
        }

        @Override
        protected void onSuccess(List<Bucket> buckets) {
            if (buckets.size() < Dribbble.COUNT_PER_LOAD) {
                adapter.setShowLoading(false);
            }

            if (refresh) {
                adapter.setData(buckets);
                swipeRefreshLayout.setRefreshing(false);
            } else {
                adapter.append(buckets);
            }

            swipeRefreshLayout.setEnabled(true);
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    private class NewBucketTask extends DribbbleTask<Void, Void, Bucket> {

        private String name;
        private String description;

        private NewBucketTask(String name, String description) {
            this.name = name;
            this.description = description;
        }

        @Override
        protected Bucket doJob(Void... params) throws DribbbleException {
            return Dribbble.newBucket(name, description);
        }

        @Override
        protected void onSuccess(Bucket bucket) {
            adapter.append(Collections.singletonList(bucket));
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

}
