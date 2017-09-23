package com.example.offline.comments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.offline.model.Comment;
import com.example.offline.rx.SchedulersFacade;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

class CommentsViewModel extends ViewModel {

    private final GetCommentsUseCase getCommentsUseCase;
    private final AddCommentUseCase addCommentUseCase;
    private final SyncCommentUseCase syncCommentUseCase;
    private final SchedulersFacade schedulersFacade;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<List<Comment>> commentsLiveData = new MutableLiveData<>();

    CommentsViewModel(GetCommentsUseCase getCommentsUseCase,
                      AddCommentUseCase addCommentUseCase,
                      SyncCommentUseCase syncCommentUseCase,
                      SchedulersFacade schedulersFacade) {
        this.getCommentsUseCase = getCommentsUseCase;
        this.addCommentUseCase = addCommentUseCase;
        this.syncCommentUseCase = syncCommentUseCase;
        this.schedulersFacade = schedulersFacade;

        loadComments();
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }

    void loadComments() {
        disposables.add(getCommentsUseCase.getComments()
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(commentList -> commentsLiveData.setValue(commentList),
                        t -> Timber.e(t, "get comments error")));
    }

    void addComment(String commentText) {
        disposables.add(addCommentUseCase.addComment(commentText)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(comment -> syncComment(comment),
                        t -> Timber.e(t, "add comment error")));
    }

    /**
     * Exposes the LiveData Comments query so the UI can observe it
     */
    LiveData<List<Comment>> getComments() {
        return commentsLiveData;
    }

    private void syncComment(Comment comment) {
        disposables.add(syncCommentUseCase.syncComment(comment)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(() -> Timber.d("sync comment request sent"),
                        t -> Timber.e(t, "sync comment request error")));
    }
}
