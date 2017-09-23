package com.example.offline.comments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.offline.model.Comment;
import com.example.offline.rx.SchedulersFacade;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

class CommentsViewModel extends ViewModel {

    private final AddCommentUseCase addCommentUseCase;
    private final SyncCommentUseCase syncCommentUseCase;
    private final GetCommentsUseCase getCommentsUseCase;
    private final SchedulersFacade schedulersFacade;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private LiveData<List<Comment>> commentsLiveData = null;

    CommentsViewModel(AddCommentUseCase addCommentUseCase,
                      SyncCommentUseCase syncCommentUseCase,
                      GetCommentsUseCase getCommentsUseCase,
                      SchedulersFacade schedulersFacade) {
        this.addCommentUseCase = addCommentUseCase;
        this.syncCommentUseCase = syncCommentUseCase;
        this.getCommentsUseCase = getCommentsUseCase;
        this.schedulersFacade = schedulersFacade;

        commentsLiveData = getCommentsUseCase.getComments();
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }

    void addComment(String commentText) {
        Timber.d("adding comment " + commentText);
        disposables.add(addCommentUseCase.addComment(commentText)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(comment -> onAddCommentSuccess(comment),
                        t -> Timber.e(t, "add comment error")));
    }

    /**
     * Exposes the LiveData Comments query so the UI can observe it
     */
    LiveData<List<Comment>> getComments() {
        Timber.d("getting comments");
        return commentsLiveData;
    }

    private void onAddCommentSuccess(Comment comment) {
        Timber.d("add comment success");

        // send sync comment request
        disposables.add(syncCommentUseCase.syncComment(comment)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(() -> Timber.d("sync comment request sent success"),
                        t -> Timber.e(t, "sync comment request sent error")));
    }
}
