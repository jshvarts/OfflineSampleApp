package com.example.offline.comments;

import com.birbit.android.jobqueue.JobManager;
import com.example.offline.model.Comment;

import io.reactivex.Completable;

/**
 * Responsible for syncing a comment over network.
 */
class SyncCommentUseCase {
    private final JobManager jobManager;

    SyncCommentUseCase(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    Completable syncComment(Comment comment) {
        return Completable.fromAction(() ->
                jobManager.addJobInBackground(new AddCommentJob(comment)));
    }
}
