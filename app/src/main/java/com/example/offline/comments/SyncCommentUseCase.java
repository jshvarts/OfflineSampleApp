package com.example.offline.comments;

import com.birbit.android.jobqueue.JobManager;
import com.example.offline.jobs.AddCommentJob;
import com.example.offline.model.Comment;
import com.example.offline.model.PhotoCommentsRepository;

import io.reactivex.Completable;

/**
 * Responsible for syncing a comment over network.
 */
class SyncCommentUseCase {
    private final PhotoCommentsRepository photoCommentsRepository;
    private final JobManager jobManager;

    SyncCommentUseCase(PhotoCommentsRepository photoCommentsRepository, JobManager jobManager) {
        this.photoCommentsRepository = photoCommentsRepository;
        this.jobManager = jobManager;
    }

    Completable syncComment(Comment comment) {
        return Completable.fromAction(() ->
                jobManager.addJobInBackground(new AddCommentJob(photoCommentsRepository, comment)));
    }
}
