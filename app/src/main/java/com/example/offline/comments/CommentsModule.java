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
                                                             UpdateCommentUseCase updateCommentUseCase,
                                                             SchedulersFacade schedulersFacade) {
        return new CommentsViewModelFactory(addCommentUseCase, syncCommentUseCase, updateCommentUseCase, schedulersFacade);
    }

    @Provides
    AddCommentUseCase provideAddCommentUseCase(PhotoCommentsRepository photoCommentsRepository) {
        return new AddCommentUseCase(photoCommentsRepository);
    }

    @Provides
    UpdateCommentUseCase provideUpdateCommentUseCase(PhotoCommentsRepository photoCommentsRepository) {
        return new UpdateCommentUseCase(photoCommentsRepository);
    }

    @Provides
    SyncCommentUseCase provideSyncCommentUseCase(JobManager jobManager) {
        return new SyncCommentUseCase(jobManager);
    }
}
