package com.example.offline.domain.services.jobs;

import android.content.Context;

public class SyncCommentJobManagerInitializer {

    private final SyncCommentResponseObserver syncCommentResponseObserver;

    public SyncCommentJobManagerInitializer(SyncCommentResponseObserver syncCommentResponseObserver) {
        this.syncCommentResponseObserver = syncCommentResponseObserver;
    }

    public void initialize(Context context) {
        JobManagerFactory.getJobManager(context);
        syncCommentResponseObserver.observeSyncResponse();
    }
}
