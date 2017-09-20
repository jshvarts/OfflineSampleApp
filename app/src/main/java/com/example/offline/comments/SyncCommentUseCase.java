package com.example.offline.comments;

import com.birbit.android.jobqueue.JobManager;
import com.example.offline.jobs.SyncCommentJob;
import com.example.offline.model.Comment;
import com.example.offline.model.LocalCommentDataStore;

import io.reactivex.Completable;

/**
 * Responsible for syncing a comment over network.
 */
class SyncCommentUseCase {
    private final LocalCommentDataStore localCommentDataStore;
    private final JobManager jobManager;

    SyncCommentUseCase(LocalCommentDataStore localCommentDataStore, JobManager jobManager) {
        this.localCommentDataStore = localCommentDataStore;
        this.jobManager = jobManager;
    }

    Completable syncComment(Comment comment) {
        return Completable.fromAction(() ->
                jobManager.addJobInBackground(new SyncCommentJob(localCommentDataStore, comment)));
    }
}
