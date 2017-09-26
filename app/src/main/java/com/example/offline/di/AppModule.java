package com.example.offline.di;

import android.content.Context;

import com.birbit.android.jobqueue.JobManager;
import com.example.offline.App;
import com.example.offline.data.CommentDao;
import com.example.offline.data.CommentDatabase;
import com.example.offline.data.LocalCommentDataStore;
import com.example.offline.data.RemoteCommentDataStore;
import com.example.offline.domain.LocalCommentRepository;
import com.example.offline.domain.RemoteCommentRepository;
import com.example.offline.domain.services.jobs.GcmJobService;
import com.example.offline.domain.services.jobs.JobManagerFactory;
import com.example.offline.domain.services.jobs.SchedulerJobService;

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

    @Singleton
    @Provides
    CommentDao provideCommentDao(Context context) {
        return CommentDatabase.getInstance(context).commentDao();
    }

    @Singleton
    @Provides
    LocalCommentRepository provideLocalCommentRepository(CommentDao commentDao) {
        return new LocalCommentDataStore(commentDao);
    }

    @Singleton
    @Provides
    RemoteCommentRepository provideRemoteCommentRepository(JobManager jobManager) {
        return new RemoteCommentDataStore(jobManager);
    }
}
