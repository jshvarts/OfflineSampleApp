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

public class AddCommentJob extends Job {

    private static final String TAG = AddCommentJob.class.getSimpleName();

    private final PhotoCommentsRepository photoCommentsRepository;
    private final Comment comment;

    public AddCommentJob(PhotoCommentsRepository photoCommentsRepository, Comment comment) {
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
        // no-op
    }

    @Override
    public void onRun() throws Throwable {
        Timber.d("Executing onRun() for comment " + comment);

        // TODO do network call here: write the comment to the cloud db, etc.

        // simulate delay in processing by sleeping for 2 seconds
        Thread.sleep(2000);

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
        return RetryConstraint.RETRY;
    }

    private void onSyncCommentSuccess() {
        Timber.d("success updating sync status");
        EventBus.getDefault().post(new SyncCommentSuccessEvent());
    }

    private void onSyncCommentError(Throwable t) {
        Timber.e(t,"error updating sync status");
    }
}
