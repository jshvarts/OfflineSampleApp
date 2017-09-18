package com.example.offline.comments;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.offline.R;
import com.example.offline.model.Comment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class CommentsActivity extends LifecycleActivity {

    @Inject
    CommentsViewModelFactory viewModelFactory;

    @BindView(R.id.comments_recycler_view)
    RecyclerView recyclerView;

    private CommentListAdapter recyclerViewAdapter;

    private CommentsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_activity);

        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CommentsViewModel.class);

        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("comment1", false));
        comments.add(new Comment("comment2", false));
        comments.add(new Comment("comment3", false));
        comments.add(new Comment("comment1", false));
        comments.add(new Comment("comment2"));
        comments.add(new Comment("comment3"));
        comments.add(new Comment("comment1"));
        recyclerViewAdapter = new CommentListAdapter(comments);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}
