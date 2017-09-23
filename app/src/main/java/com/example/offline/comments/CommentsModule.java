package com.example.offline.comments;

import com.birbit.android.jobqueue.JobManager;
import com.example.offline.model.LocalCommentDataStore;
import com.example.offline.rx.SchedulersFacade;

import dagger.Module;
import dagger.Provides;

/**
 * Define CommentsActivity-specific dependencies here.
 */
@Module
public class CommentsModule {
    @Provides
    CommentsViewModelFactory provideCommentsViewModelFactory(GetCommentsUseCase getCommentsUseCase,
                                                             AddCommentUseCase addCommentUseCase,
                                                             SyncCommentUseCase syncCommentUseCase,
                                                             SchedulersFacade schedulersFacade) {
        return new CommentsViewModelFactory(getCommentsUseCase, addCommentUseCase, syncCommentUseCase, schedulersFacade);
    }

    @Provides
    SyncCommentLifecycleObserver provideSyncCommentLifecycleObserver(UpdateCommentUseCase updateCommentUseCase,
                                                             DeleteCommentUseCase deleteCommentUseCase,
                                                             SchedulersFacade schedulersFacade) {
        return new SyncCommentLifecycleObserver(updateCommentUseCase, deleteCommentUseCase, schedulersFacade);
    }

    @Provides
    AddCommentUseCase provideAddCommentUseCase(LocalCommentDataStore localCommentDataStore) {
        return new AddCommentUseCase(localCommentDataStore);
    }

    @Provides
    GetCommentsUseCase provideGetCommentsUseCase(LocalCommentDataStore localCommentDataStore) {
        return new GetCommentsUseCase(localCommentDataStore);
    }

    @Provides
    SyncCommentUseCase provideSyncCommentUseCase(JobManager jobManager) {
        return new SyncCommentUseCase(jobManager);
    }

    @Provides
    UpdateCommentUseCase provideUpdateCommentUseCase(LocalCommentDataStore localCommentDataStore) {
        return new UpdateCommentUseCase(localCommentDataStore);
    }

    @Provides
    DeleteCommentUseCase provideDeleteCommentUseCase(LocalCommentDataStore localCommentDataStore) {
        return new DeleteCommentUseCase(localCommentDataStore);
    }
}
