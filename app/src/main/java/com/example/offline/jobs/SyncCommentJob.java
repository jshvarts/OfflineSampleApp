package com.example.offline.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.example.offline.events.SyncCommentSuccessEvent;
import com.example.offline.model.Comment;
import com.example.offline.model.LocalCommentDataStore;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SyncCommentJob extends Job {

    private static final String TAG = SyncCommentJob.class.getSimpleName();

    private final LocalCommentDataStore localCommentDataStore;
    private final Comment comment;

    public SyncCommentJob(LocalCommentDataStore localCommentDataStore, Comment comment) {
        super(new Params(Priority.MID)
                .requireNetwork()
                .groupBy(TAG)
                .singleInstanceBy(TAG)
                .addTags(TAG));
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

        // TODO do network call here: write the comment to the cloud db, etc.
        Thread.sleep(20000);

        // If network call succeeded, update local db to reflect that the comment was synced successfully

        Comment updatedComment = Comment.clone(comment, false);
        localCommentDataStore.update(updatedComment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> onSyncCommentSuccess(),
                        t -> onSyncCommentError(t));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        // no-op
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
        return RetryConstraint.RETRY;
    }

    private void onSyncCommentSuccess() {
        Timber.d("success updating sync status");
        EventBus.getDefault().post(new SyncCommentSuccessEvent());
    }

    private void onSyncCommentError(Throwable throwable) {
        Timber.e(throwable,"error updating sync status. No need to report it.");
    }
}
