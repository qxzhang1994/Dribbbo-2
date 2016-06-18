package com.jiuzhang.guojing.dribbbo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShotListFragment extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout swipeRefreshLayout;

    private List<Shot> data;
    private InfiniteAdapter adapter;

    private InfiniteAdapter.LoadMoreListener onLoadMore = new InfiniteAdapter.LoadMoreListener() {
        @Override
        public void onLoadMore() {
            Toast.makeText(getContext(), "Loading more", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<Shot> moreShots = mockData(10);
                    adapter.append(moreShots);
//                    adapter.setShowLoading(false);
                }
            }, 2000);
        }
    };

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
        data = mockData(10);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), "Refreshing!", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.spacing_medium)));

        adapter = new InfiniteAdapter(getContext(), data, onLoadMore);
        recyclerView.setAdapter(adapter);
//        recyclerView.setAdapter(new RecyclerView.Adapter<ShotViewHolder>() {
//            @Override
//            public ShotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View view = LayoutInflater
//                        .from(getContext())
//                        .inflate(R.layout.list_item_shot, parent, false);
//                return new ShotViewHolder(view);
//            }
//
//            @Override
//            public void onBindViewHolder(ShotViewHolder holder, int position) {
//                final Shot shot = data.get(position);
//                holder.root.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getContext(), ShotActivity.class);
//                        startActivity(intent);
//                    }
//                });
//                holder.actionBucket.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(v.getContext(), "bucket clicked", Toast.LENGTH_LONG).show();
//                    }
//                });
//                holder.image.setImageDrawable(getResources().getDrawable(R.mipmap.artboard_5));
//                holder.likeCount.setText(String.valueOf(shot.likeCount));
//                holder.bucketCount.setText(String.valueOf(shot.bucketCount));
//                holder.viewCount.setText(String.valueOf(shot.viewCount));
//            }
//
//            @Override
//            public int getItemCount() {
//                return data.size();
//            }
//        });
    }

    private List<Shot> mockData(int count) {
        List<Shot> data = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < count; ++i) {
            Shot shot = new Shot();
            shot.bucketCount = r.nextInt(100);
            shot.likeCount = r.nextInt(1000);
            shot.viewCount = r.nextInt(5000);
            data.add(shot);
        }
        return data;
    }

}
