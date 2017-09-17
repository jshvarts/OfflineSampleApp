package com.example.offline.comments;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.example.offline.R;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class CommentsActivity extends LifecycleActivity {

    @Inject
    CommentsViewModelFactory viewModelFactory;

    private CommentsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_activity);

        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CommentsViewModel.class);
    }

    @OnClick(R.id.add_comment_button)
    void onAddCommentButtonClicked() {
        viewModel.addComment();
    }
}
