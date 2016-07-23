package com.jiuzhang.guojing.dribbbo.view.shot_list;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.jiuzhang.guojing.dribbbo.R;
import com.jiuzhang.guojing.dribbbo.dribbble.Dribbble;
import com.jiuzhang.guojing.dribbbo.model.Shot;
import com.jiuzhang.guojing.dribbbo.utils.ModelUtils;
import com.jiuzhang.guojing.dribbbo.view.base.InfiniteAdapter;
import com.jiuzhang.guojing.dribbbo.view.base.SpaceItemDecoration;
import com.jiuzhang.guojing.dribbbo.view.shot_detail.ShotFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShotListFragment extends Fragment {

    public static final int REQ_CODE_SHOT = 100;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout swipeRefreshLayout;

    private InfiniteAdapter adapter;

    private Set<String> likeTasks;
    private Set<String> unlikeTasks;

    private InfiniteAdapter.LoadMoreListener onLoadMore = new InfiniteAdapter.LoadMoreListener() {
        @Override
        public void onLoadMore() {
            if (Dribbble.isLoggedIn()) {
                AsyncTaskCompat.executeParallel(new LoadShotsTask());
            }
        }
    };

    public ShotListFragment() {
        likeTasks = new HashSet<>();
        unlikeTasks = new HashSet<>();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_SHOT && resultCode == Activity.RESULT_OK) {
            Shot updatedShot = ModelUtils.toObject(data.getStringExtra(ShotFragment.KEY_SHOT),
                                                   new TypeToken<Shot>(){});
            for (Shot shot : adapter.getData()) {
                if (TextUtils.equals(shot.id, updatedShot.id)) {
                    shot.liked = updatedShot.liked;
                    shot.likes_count = updatedShot.likes_count;
                    adapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shot_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTaskCompat.executeParallel(new RefreshTask());
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.spacing_medium)));

        adapter = new InfiniteAdapter(getContext(), this, new ArrayList<Shot>(), onLoadMore);
        recyclerView.setAdapter(adapter);
    }

    public void likeShot(String id) {
        if (!likeTasks.contains(id)) {
            likeTasks.add(id);
            AsyncTaskCompat.executeParallel(new LikeTask(id, true));
        }
    }

    public void unlikeShot(String id) {
        if (!unlikeTasks.contains(id)) {
            unlikeTasks.add(id);
            AsyncTaskCompat.executeParallel(new LikeTask(id, false));
        }
    }

    private void runCheckLikeTasks(List<Shot> shots) {
        for (Shot shot : shots) {
            AsyncTaskCompat.executeParallel(new CheckLikeTask(shot.id));
        }
    }

    private class LoadShotsTask extends AsyncTask<Void, Void, List<Shot>> {

        @Override
        protected List<Shot> doInBackground(Void... params) {
            try {
                return Dribbble.getShots(adapter.getItemCount() / 12 + 1);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Shot> shots) {
            swipeRefreshLayout.setEnabled(true);
            adapter.append(shots);

            runCheckLikeTasks(shots);
        }
    }

    private class RefreshTask extends AsyncTask<Void, Void, List<Shot>> {

        @Override
        protected List<Shot> doInBackground(Void... params) {
            try {
                return Dribbble.getShots(1);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Shot> shots) {
            swipeRefreshLayout.setRefreshing(false);
            adapter.setData(shots);

            runCheckLikeTasks(shots);
        }
    }

    private class LikeTask extends AsyncTask<Void, Void, String> {

        private String id;
        private boolean like;

        public LikeTask(String id, boolean like) {
            this.id = id;
            this.like = like;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                if (like) {
                    Dribbble.likeShot(id);
                } else {
                    Dribbble.unlikeShot(id);
                }
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String errorMsg) {
            if (like) {
                likeTasks.remove(id);
            } else {
                unlikeTasks.remove(id);
            }

            if (errorMsg == null) {
                for (Shot shot : adapter.getData()) {
                    if (TextUtils.equals(shot.id, id)) {
                        shot.liked = like;
                        shot.likes_count += like ? 1 : -1;
                        adapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        }
    }

    private class CheckLikeTask extends AsyncTask<Void, Void, Boolean> {

        private String id;

        public CheckLikeTask(String id) {
            this.id = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return Dribbble.isLikingShot(id);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result != null) {
                for (Shot shot : adapter.getData()) {
                    if (TextUtils.equals(shot.id, id)) {
                        shot.liked = result;
                        adapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        }
    }

}
