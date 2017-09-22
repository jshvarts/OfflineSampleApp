package com.example.offline.di;

import android.content.Context;

import com.birbit.android.jobqueue.JobManager;
import com.example.offline.App;
import com.example.offline.model.CommentDao;
import com.example.offline.model.CommentDatabase;
import com.example.offline.model.LocalCommentDataStore;
import com.example.offline.services.GcmJobService;
import com.example.offline.jobs.JobManagerFactory;
import com.example.offline.services.SchedulerJobService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * This is where you will inject application-wide dependencies.
 */
@Module
public class AppModule {

    @Provides
    Context provideContext(App application) {
        return application.getApplicationContext();
    }

    @Singleton
    @Provides
    CommentDao provideCommentDao(Context context) {
        return CommentDatabase.getInstance(context).commentDao();
    }

    @Singleton
    @Provides
    LocalCommentDataStore provideLocalCommentDataStore(CommentDao commentDao) {
        return new LocalCommentDataStore(commentDao);
    }

    @Singleton
    @Provides
    JobManager provideJobManager(Context context) {
        return JobManagerFactory.getJobManager(context);
    }

    @Singleton
    @Provides
    SchedulerJobService provideSchedulerJobService() {
        return new SchedulerJobService();
    }

    @Singleton
    @Provides
    GcmJobService provideGcmJobService() {
        return new GcmJobService();
    }
}
