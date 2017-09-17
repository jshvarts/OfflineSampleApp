package com.example.offline.comments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.example.offline.jobs.Priority;

import timber.log.Timber;

public class AddCommentJob extends Job {

    private static final String TAG = AddCommentJob.class.getSimpleName();

    public AddCommentJob() {
        super(new Params(Priority.MID)
                .requireNetwork()
                .groupBy(TAG)
                .singleInstanceBy(TAG)
                .addTags(TAG));
    }

    @Override
    public void onAdded() {
        // no-op
    }

    @Override
    public void onRun() throws Throwable {
        Timber.d("Executing onRun()");
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        // no-op
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.RETRY;
    }
}
