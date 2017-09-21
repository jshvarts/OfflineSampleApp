package com.example.offline.comments;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.offline.R;
import com.example.offline.events.DeleteCommentSuccessEvent;
import com.example.offline.events.SyncCommentSuccessEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import timber.log.Timber;

public class CommentsActivity extends LifecycleActivity {

    @Inject
    CommentsViewModelFactory viewModelFactory;

    @BindView(R.id.add_comment_edittext)
    EditText addCommentEditText;

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

        viewModel.getLiveComments().observe(this, commentList -> recyclerViewAdapter.updateCommentList(commentList));

        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.add_comment_button)
    void onAddCommentButtonClicked() {

        hideKeyboard();

        // TODO add comment text validation
        viewModel.addComment(addCommentEditText.getText().toString());

        clearEditText();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void onSyncCommentSuccessEvent(SyncCommentSuccessEvent event) {
        Timber.d("received sync comment success event");

        // re-query the model to update UI
        viewModel.queryComments();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void onDeleteCommentSuccessEvent(DeleteCommentSuccessEvent event) {
        Timber.d("received delete comment success event");

        // re-query the model to update UI
        viewModel.queryComments();
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
