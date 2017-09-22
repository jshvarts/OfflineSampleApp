package com.example.offline.events;

import com.example.offline.model.Comment;

public class DeleteCommentRequestEvent {
    private final Comment comment;

    public DeleteCommentRequestEvent(Comment comment) {
        this.comment = comment;
    }

    public Comment getComment() {
        return comment;
    }
}
