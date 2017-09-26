package com.example.offline.domain.services.jobs;

import android.support.annotation.NonNull;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;

import javax.inject.Inject;

public class SchedulerJobService extends FrameworkJobSchedulerService {

    // we cannot do constructor injection (preferred way of injecting dependencies)
    // since manifest file requires a default no-arg constructor

    @Inject
    JobManager jobManager;

    @NonNull
    @Override
    protected JobManager getJobManager() {
        return jobManager;
    }
}
