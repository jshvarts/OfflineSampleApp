package com.example.offline.comments;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.example.offline.events.DeleteCommentRequestEvent;
import com.example.offline.events.UpdateCommentRequestEvent;
import com.example.offline.rx.SchedulersFacade;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * Updates local database after remote comment sync requests
 */
public class SyncCommentLifecycleObserver implements LifecycleObserver {
    private final UpdateCommentUseCase updateCommentUseCase;
    private final DeleteCommentUseCase deleteCommentUseCase;
    private final SchedulersFacade schedulersFacade;

    private final CompositeDisposable disposables = new CompositeDisposable();

    public SyncCommentLifecycleObserver(UpdateCommentUseCase updateCommentUseCase,
                                        DeleteCommentUseCase deleteCommentUseCase,
                                        SchedulersFacade schedulersFacade) {
        this.updateCommentUseCase = updateCommentUseCase;
        this.deleteCommentUseCase = deleteCommentUseCase;
        this.schedulersFacade = schedulersFacade;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        Timber.d("onResume lifecycle event.");
        EventBus.getDefault().register(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        Timber.d("onPause lifecycle event.");
        EventBus.getDefault().unregister(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onCleared() {
        Timber.d("onDestroy lifecycle event.");
        disposables.clear();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateCommentRequestEvent(UpdateCommentRequestEvent event) {
        Timber.d("received update comment request event for comment %s", event.getComment());
        disposables.add(updateCommentUseCase.updateComment(event.getComment())
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(() -> Timber.d("update comment success"),
                        t -> Timber.e(t, "update comment error")));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteCommentRequestEvent(DeleteCommentRequestEvent event) {
        Timber.d("received delete comment request event for comment %s", event.getComment());
        disposables.add(deleteCommentUseCase.deleteComment(event.getComment())
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(() -> Timber.d("delete comment success"),
                        t -> Timber.e(t, "delete comment error")));
    }


}
