package com.example.offline.services;

import android.support.annotation.NonNull;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.scheduling.GcmJobSchedulerService;

import javax.inject.Inject;

public class GcmJobService extends GcmJobSchedulerService {

    // we cannot do constructor injection (prefered way of injecting dependencies)
    // since manifest file requires a default constructor

    @Inject
    JobManager jobManager;

    @NonNull
    @Override
    protected JobManager getJobManager() {
        return jobManager;
    }
}
