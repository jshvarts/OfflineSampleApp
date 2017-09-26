package com.example.offline.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.offline.domain.AddCommentUseCase;
import com.example.offline.domain.GetCommentsUseCase;
import com.example.offline.model.Comment;
import com.example.offline.rx.SchedulersFacade;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class CommentsViewModel extends ViewModel {

    private final GetCommentsUseCase getCommentsUseCase;
    private final AddCommentUseCase addCommentUseCase;
    private final SchedulersFacade schedulersFacade;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<List<Comment>> commentsLiveData = new MutableLiveData<>();

    public CommentsViewModel(GetCommentsUseCase getCommentsUseCase,
                      AddCommentUseCase addCommentUseCase,
                      SchedulersFacade schedulersFacade) {
        this.getCommentsUseCase = getCommentsUseCase;
        this.addCommentUseCase = addCommentUseCase;
        this.schedulersFacade = schedulersFacade;

        loadComments();
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }

    public void loadComments() {
        disposables.add(getCommentsUseCase.getComments()
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(commentList -> commentsLiveData.setValue(commentList),
                        t -> Timber.e(t, "get comments error")));
    }

    public void addComment(String commentText) {
        disposables.add(addCommentUseCase.addComment(commentText)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(() -> Timber.d("add comment success"),
                        t -> Timber.e(t, "add comment error")));
    }

    /**
     * Exposes the LiveData Comments query so the UI can observe it
     */
    public LiveData<List<Comment>> getComments() {
        return commentsLiveData;
    }
}
