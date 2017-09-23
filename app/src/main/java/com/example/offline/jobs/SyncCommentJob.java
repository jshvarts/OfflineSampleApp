package com.example.offline.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.example.offline.events.DeleteCommentRequestEvent;
import com.example.offline.events.UpdateCommentRequestEvent;
import com.example.offline.model.Comment;
import com.example.offline.networking.RemoteSyncCommentService;
import com.example.offline.networking.RemoteSyncDataException;

import org.greenrobot.eventbus.EventBus;

import timber.log.Timber;

public class SyncCommentJob extends Job {

    private static final String TAG = SyncCommentJob.class.getCanonicalName();
    private final Comment comment;

    public SyncCommentJob(Comment comment) {
        super(new Params(Priority.MID)
                .requireNetwork()
                .groupBy(TAG)
                .persist());
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
        RemoteSyncCommentService.getInstance().addComment(comment);

        // remote call was successful--update the Comment locally to notify that sync is no longer pending
        Comment updatedComment = Comment.clone(comment, false);
        EventBus.getDefault().post(new UpdateCommentRequestEvent(updatedComment));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        Timber.d("canceling job. reason: %d, throwable: %s", cancelReason, throwable);
        // sync to remote failed--remove comment from local db
        EventBus.getDefault().post(new DeleteCommentRequestEvent(comment));
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
}
