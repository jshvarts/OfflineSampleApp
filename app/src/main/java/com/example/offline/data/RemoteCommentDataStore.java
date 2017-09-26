package com.example.offline.data;

import com.birbit.android.jobqueue.JobManager;
import com.example.offline.domain.RemoteCommentRepository;
import com.example.offline.domain.services.jobs.SyncCommentJob;
import com.example.offline.model.Comment;

import io.reactivex.Completable;

public class RemoteCommentDataStore implements RemoteCommentRepository {
    private final JobManager jobManager;

    public RemoteCommentDataStore(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @Override
    public Completable sync(Comment comment) {
        return Completable.fromAction(() ->
                jobManager.addJobInBackground(new SyncCommentJob(comment)));
    }
}
