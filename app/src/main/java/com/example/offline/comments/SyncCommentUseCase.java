package com.example.offline.comments;

import com.birbit.android.jobqueue.JobManager;
import com.example.offline.jobs.SyncCommentJob;
import com.example.offline.model.Comment;
import com.example.offline.model.LocalCommentDataStore;
import com.example.offline.networking.RemoteSyncCommentService;

import io.reactivex.Completable;

/**
 * Responsible for syncing a comment over network.
 */
class SyncCommentUseCase {
    private final RemoteSyncCommentService remoteSyncCommentService;
    private final LocalCommentDataStore localCommentDataStore;
    private final JobManager jobManager;

    SyncCommentUseCase(RemoteSyncCommentService remoteSyncCommentService,
                       LocalCommentDataStore localCommentDataStore,
                       JobManager jobManager) {
        this.remoteSyncCommentService = remoteSyncCommentService;
        this.localCommentDataStore = localCommentDataStore;
        this.jobManager = jobManager;
    }

    Completable syncComment(Comment comment) {
        return Completable.fromAction(() ->
                jobManager.addJobInBackground(new SyncCommentJob(remoteSyncCommentService, localCommentDataStore, comment)));
    }
}
