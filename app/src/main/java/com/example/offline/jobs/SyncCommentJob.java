package com.example.offline.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.example.offline.events.SyncCommentSuccessEvent;
import com.example.offline.model.Comment;
import com.example.offline.model.LocalCommentDataStore;
import com.example.offline.networking.RemoteSyncCommentService;
import com.example.offline.networking.RemoteSyncDataException;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class SyncCommentJob extends Job {

    private static final String TAG = SyncCommentJob.class.getSimpleName();

    private final RemoteSyncCommentService remoteSyncCommentService;
    private final LocalCommentDataStore localCommentDataStore;
    private final Comment comment;

    public SyncCommentJob(RemoteSyncCommentService remoteSyncCommentService,
                          LocalCommentDataStore localCommentDataStore,
                          Comment comment) {
        super(new Params(Priority.MID)
                .requireNetwork()
                .groupBy(TAG)
                .singleInstanceBy(TAG)
                .addTags(TAG));
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

        // execute remote call with Retrofit2 asynchronously
        Call<Comment> call = remoteSyncCommentService.addComment(comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    Timber.d("successful remote service response: " + response.body());
                    onRemoteSyncCommentSuccess();
                } else {
                    Timber.d("error remote service response: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                onRemoteSyncCommentFailure(t);
            }
        });
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

    private void onRemoteSyncCommentSuccess() {
        Timber.d("success syncing comment with remote service");

        // update the comment locally to reflect that sync is no longer pending
        Comment updatedComment = Comment.clone(comment, false);
        localCommentDataStore.update(updatedComment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> onLocalSyncCommentSuccess(),
                        t -> onLocalSyncCommentFailure(t));
    }

    private void onRemoteSyncCommentFailure(Throwable throwable) {
        Timber.e(throwable,"failure syncing comment with remote service");
    }

    private void onLocalSyncCommentSuccess() {
        Timber.d("success updating sync status");
        EventBus.getDefault().post(new SyncCommentSuccessEvent());
    }

    private void onLocalSyncCommentFailure(Throwable throwable) {
        Timber.e(throwable,"error updating sync status. No need to report it.");
    }
}
