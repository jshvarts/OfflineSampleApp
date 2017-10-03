package com.example.offline.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.offline.domain.AddCommentUseCase;
import com.example.offline.domain.GetCommentsUseCase;
import com.example.offline.model.Comment;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class CommentsViewModel extends ViewModel {

    private final GetCommentsUseCase getCommentsUseCase;
    private final AddCommentUseCase addCommentUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<List<Comment>> commentsLiveData = new MutableLiveData<>();

    public CommentsViewModel(GetCommentsUseCase getCommentsUseCase, AddCommentUseCase addCommentUseCase) {
        this.getCommentsUseCase = getCommentsUseCase;
        this.addCommentUseCase = addCommentUseCase;

        loadComments();
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
    /**
     * Adds new comment
     */
    public void addComment(String commentText) {
        disposables.add(addCommentUseCase.addComment(commentText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Timber.d("add comment success"),
                        t -> Timber.e(t, "add comment error")));
    }

    /**
     * Exposes the latest comments so the UI can observe it
     */
    public LiveData<List<Comment>> comments() {
        return commentsLiveData;
    }

    void loadComments() {
        disposables.add(getCommentsUseCase.getComments()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(commentList -> commentsLiveData.setValue(commentList),
                        t -> Timber.e(t, "get comments error")));
    }
}
