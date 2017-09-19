package com.example.offline.comments;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.offline.rx.SchedulersFacade;

class CommentsViewModelFactory implements ViewModelProvider.Factory {

    private final AddCommentUseCase addCommentUseCase;

    private final SyncCommentUseCase syncCommentUseCase;

    private final GetCommentsUseCase getCommentsUseCase;

    private final SchedulersFacade schedulersFacade;

    CommentsViewModelFactory(AddCommentUseCase addCommentUseCase,
                             SyncCommentUseCase syncCommentUseCase,
                             GetCommentsUseCase getCommentsUseCase,
                             SchedulersFacade schedulersFacade) {
        this.addCommentUseCase = addCommentUseCase;
        this.syncCommentUseCase = syncCommentUseCase;
        this.getCommentsUseCase = getCommentsUseCase;
        this.schedulersFacade = schedulersFacade;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CommentsViewModel.class)) {
            return (T) new CommentsViewModel(addCommentUseCase,
                    syncCommentUseCase,
                    getCommentsUseCase,
                    schedulersFacade);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
