package com.example.offline.domain.services.jobs;

import android.support.annotation.WorkerThread;

import com.example.offline.domain.DeleteCommentUseCase;
import com.example.offline.domain.UpdateCommentUseCase;
import com.example.offline.domain.services.SyncCommentResponse;
import com.example.offline.domain.services.SyncCommentRxBus;
import com.example.offline.domain.services.SyncResponseEventType;
import com.example.offline.model.Comment;

import timber.log.Timber;

public class SyncCommentResponseObserver {

    private final UpdateCommentUseCase updateCommentUseCase;
    private final DeleteCommentUseCase deleteCommentUseCase;

    public SyncCommentResponseObserver(UpdateCommentUseCase updateCommentUseCase,
                                       DeleteCommentUseCase deleteCommentUseCase) {
        this.updateCommentUseCase = updateCommentUseCase;
        this.deleteCommentUseCase = deleteCommentUseCase;
    }

    void observeSyncResponse() {
        SyncCommentRxBus.getInstance().toObservable()
                .subscribe(syncResponse -> handleSyncResponse(syncResponse));
    }

    private void handleSyncResponse(SyncCommentResponse response) {
        if (response.eventType == SyncResponseEventType.SUCCESS) {
            onSyncCommentSuccess(response.comment);
        } else {
            onSyncCommentFailed(response.comment);
        }
    }

    @WorkerThread
    private void onSyncCommentSuccess(Comment comment) {
        Timber.d("received sync comment success event for comment %s", comment);
        updateCommentUseCase.updateComment(comment)
                .subscribe(() -> Timber.d("update comment success"),
                        t -> Timber.e(t, "update comment error"));
    }

    @WorkerThread
    private void onSyncCommentFailed(Comment comment) {
        Timber.d("received sync comment failed event for comment %s", comment);
        deleteCommentUseCase.deleteComment(comment)
                .subscribe(() -> Timber.d("delete comment success"),
                        t -> Timber.e(t, "delete comment error"));
    }
}
