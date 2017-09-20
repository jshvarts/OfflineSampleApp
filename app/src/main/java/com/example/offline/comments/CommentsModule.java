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
    CommentsViewModelFactory provideCommentsViewModelFactory(AddCommentUseCase addCommentUseCase,
                                                             SyncCommentUseCase syncCommentUseCase,
                                                             GetCommentsUseCase getCommentsUseCase,
                                                             SchedulersFacade schedulersFacade) {
        return new CommentsViewModelFactory(addCommentUseCase, syncCommentUseCase, getCommentsUseCase, schedulersFacade);
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
    SyncCommentUseCase provideSyncCommentUseCase(LocalCommentDataStore localCommentDataStore,
                                                 JobManager jobManager) {
        return new SyncCommentUseCase(localCommentDataStore, jobManager);
    }
}
