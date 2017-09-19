package com.example.offline.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.example.offline.events.SyncCommentSuccessEvent;
import com.example.offline.model.Comment;
import com.example.offline.model.PhotoCommentsRepository;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SyncCommentJob extends Job {

    private static final String TAG = SyncCommentJob.class.getSimpleName();

    private final PhotoCommentsRepository photoCommentsRepository;
    private final Comment comment;

    public SyncCommentJob(PhotoCommentsRepository photoCommentsRepository, Comment comment) {
        super(new Params(Priority.MID)
                .requireNetwork()
                .groupBy(TAG)
                .singleInstanceBy(TAG)
                .addTags(TAG));
        this.photoCommentsRepository = photoCommentsRepository;
        this.comment = comment;
    }

    @Override
    public void onAdded() {
        Timber.d("Executing onAdded() for comment " + comment);
        try {
            // simulate delay in processing by sleeping for 20 seconds
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRun() throws Throwable {
        Timber.d("Executing onRun() for comment " + comment);

        // TODO do network call here: write the comment to the cloud db, etc.

        // If network call succeeded, update local db to reflect that the comment was synced successfully
        photoCommentsRepository.updateCommentSyncStatus(comment.getId())
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

            // If our sync job used Retrofit2, we'd use the statusCode check below to see
            // if the http exception is recoverable (worth a retry)

            /*
            int statusCode = exception.getResponse().raw().code();
            if (statusCode >= 400 && statusCode < 500) {
                return RetryConstraint.CANCEL;
            }
            */
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
