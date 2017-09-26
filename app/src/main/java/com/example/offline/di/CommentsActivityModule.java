package com.example.offline.di;

import com.example.offline.domain.DeleteCommentUseCase;
import com.example.offline.domain.GetCommentsUseCase;
import com.example.offline.domain.LocalCommentRepository;
import com.example.offline.domain.RemoteCommentRepository;
import com.example.offline.domain.services.SyncCommentLifecycleObserver;
import com.example.offline.domain.AddCommentUseCase;
import com.example.offline.domain.SyncCommentUseCase;
import com.example.offline.domain.UpdateCommentUseCase;
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
    SyncCommentLifecycleObserver provideSyncCommentLifecycleObserver(UpdateCommentUseCase updateCommentUseCase,
                                                                     DeleteCommentUseCase deleteCommentUseCase,
                                                                     SchedulersFacade schedulersFacade) {
        return new SyncCommentLifecycleObserver(updateCommentUseCase, deleteCommentUseCase, schedulersFacade);
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
    UpdateCommentUseCase provideUpdateCommentUseCase(LocalCommentRepository localCommentRepository) {
        return new UpdateCommentUseCase(localCommentRepository);
    }

    @Provides
    DeleteCommentUseCase provideDeleteCommentUseCase(LocalCommentRepository localCommentRepository) {
        return new DeleteCommentUseCase(localCommentRepository);
    }

    @Provides
    SyncCommentUseCase provideSyncCommentUseCase(RemoteCommentRepository remoteCommentRepository) {
        return new SyncCommentUseCase(remoteCommentRepository);
    }
}
