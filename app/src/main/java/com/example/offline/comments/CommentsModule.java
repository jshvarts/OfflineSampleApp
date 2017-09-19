package com.example.offline.comments;

import com.birbit.android.jobqueue.JobManager;
import com.example.offline.model.PhotoCommentsRepository;
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
    AddCommentUseCase provideAddCommentUseCase(PhotoCommentsRepository photoCommentsRepository) {
        return new AddCommentUseCase(photoCommentsRepository);
    }

    @Provides
    GetCommentsUseCase provideGetCommentsUseCase(PhotoCommentsRepository photoCommentsRepository) {
        return new GetCommentsUseCase(photoCommentsRepository);
    }

    @Provides
    SyncCommentUseCase provideSyncCommentUseCase(PhotoCommentsRepository photoCommentsRepository,
                                                 JobManager jobManager) {
        return new SyncCommentUseCase(photoCommentsRepository, jobManager);
    }
}
