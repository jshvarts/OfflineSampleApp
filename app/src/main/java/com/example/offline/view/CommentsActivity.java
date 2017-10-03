package com.example.offline.view;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.offline.R;
import com.example.offline.domain.services.SyncCommentLifecycleObserver;
import com.example.offline.presentation.CommentsViewModel;
import com.example.offline.presentation.CommentsViewModelFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class CommentsActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    @Inject
    CommentsViewModelFactory viewModelFactory;

    @Inject
    SyncCommentLifecycleObserver syncCommentLifecycleObserver;

    @BindView(R.id.add_comment_edittext)
    EditText addCommentEditText;

    @BindView(R.id.comments_recycler_view)
    RecyclerView recyclerView;

    private CommentListAdapter recyclerViewAdapter;

    private CommentsViewModel viewModel;

    private LifecycleRegistry registry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return registry;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_activity);

        ButterKnife.bind(this);

        initRecyclerView();

        getLifecycle().addObserver(syncCommentLifecycleObserver);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CommentsViewModel.class);

        viewModel.comments().observe(this, recyclerViewAdapter::updateCommentList);
    }

    @OnClick(R.id.add_comment_button)
    void onAddCommentButtonClicked() {

        hideKeyboard();

        // TODO add comment validation
        viewModel.addComment(addCommentEditText.getText().toString());

        clearEditText();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void clearEditText() {
        addCommentEditText.getText().clear();
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        recyclerViewAdapter = new CommentListAdapter(new ArrayList<>());
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}
