package com.example.offline.comments;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.offline.common.viewmodel.Response;
import com.example.offline.rx.SchedulersFacade;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

class CommentsViewModel extends ViewModel {

    private final AddCommentUseCase addCommentUseCase;

    private final SchedulersFacade schedulersFacade;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<Response<String>> response = new MutableLiveData<>();

    private final MutableLiveData<Boolean> commentAdded = new MutableLiveData<>();

    CommentsViewModel(AddCommentUseCase addCommentUseCase,
                      SchedulersFacade schedulersFacade) {
        this.addCommentUseCase = addCommentUseCase;
        this.schedulersFacade = schedulersFacade;
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }

    void addComment() {
        addComment(addCommentUseCase.execute());
    }

    MutableLiveData<Boolean> getAddCommentStatus() {
        return commentAdded;
    }

    private void addComment(Single<String> single) {
        disposables.add(single
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .doOnSubscribe(s -> commentAdded.setValue(true))
                .doAfterTerminate(() -> commentAdded.setValue(false))
                .subscribe(
                        greeting -> response.setValue(Response.success(greeting)),
                        throwable -> response.setValue(Response.error(throwable))
                )
        );
    }
}
