package com.example.offline.domain.services;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.example.offline.domain.DeleteCommentUseCase;
import com.example.offline.domain.UpdateCommentUseCase;
import com.example.offline.model.Comment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Updates local database after remote comment sync requests
 */
public class SyncCommentLifecycleObserver implements LifecycleObserver {
    private final UpdateCommentUseCase updateCommentUseCase;
    private final DeleteCommentUseCase deleteCommentUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public SyncCommentLifecycleObserver(UpdateCommentUseCase updateCommentUseCase,
                                        DeleteCommentUseCase deleteCommentUseCase) {
        this.updateCommentUseCase = updateCommentUseCase;
        this.deleteCommentUseCase = deleteCommentUseCase;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        Timber.d("onResume lifecycle event.");
        disposables.add(SyncCommentRxBus.getInstance().toObservable()
                .subscribe(this::handleSyncResponse));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        Timber.d("onPause lifecycle event.");
        disposables.clear();
    }

    private void handleSyncResponse(SyncCommentResponse response) {
        if (response.eventType == SyncResponseEventType.SUCCESS) {
            onSyncCommentSuccess(response.comment);
        } else {
            onSyncCommentFailed(response.comment);
        }
    }

    private void onSyncCommentSuccess(Comment comment) {
        Timber.d("received sync comment success event for comment %s", comment);
        disposables.add(updateCommentUseCase.updateComment(comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Timber.d("update comment success"),
                        t -> Timber.e(t, "update comment error")));
    }

    private void onSyncCommentFailed(Comment comment) {
        Timber.d("received sync comment failed event for comment %s", comment);
        disposables.add(deleteCommentUseCase.deleteComment(comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Timber.d("delete comment success"),
                        t -> Timber.e(t, "delete comment error")));
    }
}
