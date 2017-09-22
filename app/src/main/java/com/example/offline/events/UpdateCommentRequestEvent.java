package com.example.offline.events;

import com.example.offline.model.Comment;

public class UpdateCommentRequestEvent {
    private final Comment comment;

    public UpdateCommentRequestEvent(Comment comment) {
        this.comment = comment;
    }

    public Comment getComment() {
        return comment;
    }
}
