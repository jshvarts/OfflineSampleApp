package com.example.offline.comments;

import android.arch.lifecycle.ViewModel;

import com.example.offline.model.Comment;
import com.example.offline.rx.SchedulersFacade;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

class CommentsViewModel extends ViewModel {

    private final AddCommentUseCase addCommentUseCase;

    private final SyncCommentUseCase syncCommentUseCase;

    private final UpdateCommentUseCase updateCommentUseCase;

    private final SchedulersFacade schedulersFacade;

    private final CompositeDisposable disposables = new CompositeDisposable();

    CommentsViewModel(AddCommentUseCase addCommentUseCase,
                      SyncCommentUseCase syncCommentUseCase,
                      UpdateCommentUseCase updateCommentUseCase,
                      SchedulersFacade schedulersFacade) {
        this.addCommentUseCase = addCommentUseCase;
        this.syncCommentUseCase = syncCommentUseCase;
        this.updateCommentUseCase = updateCommentUseCase;
        this.schedulersFacade = schedulersFacade;
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }

    void addComment() {
        Comment comment = new Comment("comment text");
        disposables.add(addCommentUseCase.addComment(comment)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(() -> onAddCommentSuccess(comment),
                        t -> Timber.e(t, "error")));
    }

    private void onAddCommentSuccess(Comment comment) {
        Timber.d("add comment success");
        disposables.add(syncCommentUseCase.syncComment(comment)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(() -> onSyncCommentSuccess(comment),
                        t -> Timber.e(t, "sync comment error")));
    }

    private void onSyncCommentSuccess(Comment comment) {
        Timber.d("sync comment success");
        disposables.add(updateCommentUseCase.updateComment(comment)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(() -> Timber.d("update comment success"),
                        t -> Timber.e(t, "sync comment error")));

    }
}
