package com.example.offline.comments;

import com.example.offline.rx.SchedulersFacade;

import dagger.Module;
import dagger.Provides;

/**
 * Define CommentsActivity-specific dependencies here.
 */
@Module
public class CommentsModule {

    @Provides
    CommentsRepository provideLobbyGreetingRepository() {
        return new CommentsRepository();
    }

    @Provides
    CommentsViewModelFactory provideLobbyViewModelFactory(AddCommentUseCase addCommentUseCase,
                                                          SchedulersFacade schedulersFacade) {
        return new CommentsViewModelFactory(addCommentUseCase, schedulersFacade);
    }
}
