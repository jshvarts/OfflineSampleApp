package com.example.offline.di;

import com.example.offline.domain.GetCommentsUseCase;
import com.example.offline.domain.LocalCommentRepository;
import com.example.offline.domain.RemoteCommentRepository;
import com.example.offline.domain.AddCommentUseCase;
import com.example.offline.domain.SyncCommentUseCase;
import com.example.offline.presentation.CommentsViewModelFactory;
import com.example.offline.rx.SchedulersFacade;

import dagger.Module;
import dagger.Provides;

/**
 * Define CommentsActivity-specific dependencies here.
 */
@Module
public class CommentsActivityModule {
    @Provides
    CommentsViewModelFactory provideCommentsViewModelFactory(GetCommentsUseCase getCommentsUseCase,
                                                             AddCommentUseCase addCommentUseCase,
                                                             SchedulersFacade schedulersFacade) {
        return new CommentsViewModelFactory(getCommentsUseCase, addCommentUseCase, schedulersFacade);
    }

    @Provides
    AddCommentUseCase provideAddCommentUseCase(LocalCommentRepository localCommentRepository, SyncCommentUseCase syncCommentUseCase) {
        return new AddCommentUseCase(localCommentRepository, syncCommentUseCase);
    }

    @Provides
    GetCommentsUseCase provideGetCommentsUseCase(LocalCommentRepository localCommentRepository) {
        return new GetCommentsUseCase(localCommentRepository);
    }

    @Provides
    SyncCommentUseCase provideSyncCommentUseCase(RemoteCommentRepository remoteCommentRepository) {
        return new SyncCommentUseCase(remoteCommentRepository);
    }
}
