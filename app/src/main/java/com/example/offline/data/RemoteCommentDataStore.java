package com.example.offline.data;

import com.example.offline.domain.RemoteCommentRepository;
import com.example.offline.domain.services.jobs.JobManagerFactory;
import com.example.offline.domain.services.jobs.SyncCommentJob;
import com.example.offline.model.Comment;

import io.reactivex.Completable;

public class RemoteCommentDataStore implements RemoteCommentRepository {

    @Override
    public Completable sync(Comment comment) {
        return Completable.fromAction(() ->
                JobManagerFactory.getJobManager().addJobInBackground(new SyncCommentJob(comment)));
    }
}
