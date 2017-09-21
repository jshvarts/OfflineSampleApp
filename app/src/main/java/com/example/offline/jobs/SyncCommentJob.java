package com.example.offline.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.example.offline.events.DeleteCommentSuccessEvent;
import com.example.offline.events.SyncCommentSuccessEvent;
import com.example.offline.model.Comment;
import com.example.offline.model.LocalCommentDataStore;
import com.example.offline.networking.RemoteSyncCommentService;
import com.example.offline.networking.RemoteSyncDataException;

import org.greenrobot.eventbus.EventBus;

import timber.log.Timber;

public class SyncCommentJob extends Job {

    private static final String TAG = SyncCommentJob.class.getCanonicalName();

    private final RemoteSyncCommentService remoteSyncCommentService;
    private final LocalCommentDataStore localCommentDataStore;
    private final Comment comment;

    public SyncCommentJob(RemoteSyncCommentService remoteSyncCommentService,
                          LocalCommentDataStore localCommentDataStore,
                          Comment comment) {
        super(new Params(Priority.MID)
                .requireNetwork()
                .groupBy(TAG));

        this.remoteSyncCommentService = remoteSyncCommentService;
        this.localCommentDataStore = localCommentDataStore;
        this.comment = comment;
    }

    @Override
    public void onAdded() {
        Timber.d("Executing onAdded() for comment " + comment);
    }

    @Override
    public void onRun() throws Throwable {
        Timber.d("Executing onRun() for comment " + comment);

        // if any exception is thrown, it will be handled by shouldReRunOnThrowable()
        remoteSyncCommentService.addComment(comment);

        // remote call was successful--update the Comment locally to notify that sync is no longer pending
        // since the job is already executed on background thread, no need to manage threads in the following Rx chain
        Comment updatedComment = Comment.clone(comment, false);
        localCommentDataStore.update(updatedComment)
                .subscribe(() -> onLocalSyncCommentSuccess(), t -> onLocalSyncCommentFailure(t));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        Timber.d("canceling job. reason: %d, throwable: %s", cancelReason, throwable);
        // since sync to remote did not succeed, remove comment from local db
        localCommentDataStore.delete(comment)
                .subscribe(() -> onLocalDeleteCommentSuccess(), t -> onLocalDeleteCommentFailure(t));
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        if(throwable instanceof RemoteSyncDataException) {
            RemoteSyncDataException exception = (RemoteSyncDataException) throwable;

            int statusCode = exception.getResponse().code();
            if (statusCode >= 400 && statusCode < 500) {
                return RetryConstraint.CANCEL;
            }
        }
        // if we are here, most likely the connection was lost during job execution
        return RetryConstraint.RETRY;
    }

    private void onLocalSyncCommentSuccess() {
        Timber.d("success updating sync status");
        EventBus.getDefault().post(new SyncCommentSuccessEvent());
    }

    private void onLocalSyncCommentFailure(Throwable throwable) {
        Timber.e(throwable,"error updating sync status.");
    }

    private void onLocalDeleteCommentSuccess() {
        Timber.d("success deleting job from local db");
        EventBus.getDefault().post(new DeleteCommentSuccessEvent());
    }

    private void onLocalDeleteCommentFailure(Throwable throwable) {
        Timber.e(throwable,"error deleting job from local db.");
    }
}
