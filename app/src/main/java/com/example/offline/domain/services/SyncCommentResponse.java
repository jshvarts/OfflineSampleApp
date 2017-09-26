package com.example.offline.domain.services;

import com.example.offline.model.Comment;

public class SyncCommentResponse {
    public final SyncResponseEventType eventType;
    public final Comment comment;

    public SyncCommentResponse(SyncResponseEventType eventType, Comment comment) {
        this.eventType = eventType;
        this.comment = comment;
    }
}
