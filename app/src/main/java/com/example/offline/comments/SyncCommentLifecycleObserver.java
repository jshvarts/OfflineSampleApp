package com.example.offline.comments;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.example.offline.events.SyncCommentResponse;
import com.example.offline.events.SyncCommentRxBus;
import com.example.offline.events.SyncResponseEventType;
import com.example.offline.model.Comment;
import com.example.offline.rx.SchedulersFacade;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * Updates local database after remote comment sync requests
 */
public class SyncCommentLifecycleObserver implements LifecycleObserver {
    private final UpdateCommentUseCase updateCommentUseCase;
    private final DeleteCommentUseCase deleteCommentUseCase;
    private final SchedulersFacade schedulersFacade;

    private final CompositeDisposable disposables = new CompositeDisposable();

    public SyncCommentLifecycleObserver(UpdateCommentUseCase updateCommentUseCase,
                                        DeleteCommentUseCase deleteCommentUseCase,
                                        SchedulersFacade schedulersFacade) {
        this.updateCommentUseCase = updateCommentUseCase;
        this.deleteCommentUseCase = deleteCommentUseCase;
        this.schedulersFacade = schedulersFacade;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        Timber.d("onResume lifecycle event.");
        disposables.add(SyncCommentRxBus.getInstance().observe()
                .subscribe(syncResponse -> handleSyncResponse(syncResponse)));
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
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(() -> Timber.d("update comment success"),
                        t -> Timber.e(t, "update comment error")));
    }

    private void onSyncCommentFailed(Comment comment) {
        Timber.d("received sync comment failed event for comment %s", comment);
        disposables.add(deleteCommentUseCase.deleteComment(comment)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(() -> Timber.d("delete comment success"),
                        t -> Timber.e(t, "delete comment error")));
    }
}
