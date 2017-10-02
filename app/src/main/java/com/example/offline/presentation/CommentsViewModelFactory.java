package com.example.offline.presentation;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.offline.domain.AddCommentUseCase;
import com.example.offline.domain.GetCommentsUseCase;

public class CommentsViewModelFactory implements ViewModelProvider.Factory {

    private final GetCommentsUseCase getCommentsUseCase;
    private final AddCommentUseCase addCommentUseCase;

    public CommentsViewModelFactory(GetCommentsUseCase getCommentsUseCase,
                             AddCommentUseCase addCommentUseCase) {
        this.getCommentsUseCase = getCommentsUseCase;
        this.addCommentUseCase = addCommentUseCase;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CommentsViewModel.class)) {
            return (T) new CommentsViewModel(getCommentsUseCase, addCommentUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
