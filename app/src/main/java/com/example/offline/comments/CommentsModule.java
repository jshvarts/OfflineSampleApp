package com.example.offline.comments;

import com.birbit.android.jobqueue.JobManager;
import com.example.offline.rx.SchedulersFacade;

import dagger.Module;
import dagger.Provides;

/**
 * Define CommentsActivity-specific dependencies here.
 */
@Module
public class CommentsModule {

    @Provides
    CommentsRepository provideCommentsRepository() {
        return new CommentsRepository();
    }

    @Provides
    CommentsViewModelFactory provideCommentsViewModelFactory(AddCommentUseCase addCommentUseCase,
                                                             SyncCommentUseCase syncCommentUseCase,
                                                             UpdateCommentUseCase updateCommentUseCase,
                                                             SchedulersFacade schedulersFacade) {
        return new CommentsViewModelFactory(addCommentUseCase, syncCommentUseCase, updateCommentUseCase, schedulersFacade);
    }

    @Provides
    AddCommentUseCase provideAddCommentUseCase(CommentsRepository commentsRepository) {
        return new AddCommentUseCase(commentsRepository);
    }

    @Provides
    UpdateCommentUseCase provideUpdateCommentUseCase(CommentsRepository commentsRepository) {
        return new UpdateCommentUseCase(commentsRepository);
    }

    @Provides
    SyncCommentUseCase provideSyncCommentUseCase(JobManager jobManager) {
        return new SyncCommentUseCase(jobManager);
    }
}
