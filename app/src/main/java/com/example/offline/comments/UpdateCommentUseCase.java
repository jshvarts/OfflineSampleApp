package com.example.offline.comments;

import com.example.offline.model.Comment;

import io.reactivex.Completable;

class UpdateCommentUseCase {
    private final CommentsRepository commentsRepository;

    UpdateCommentUseCase(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    Completable updateComment(Comment comment) {
        return commentsRepository.updateComment(comment);
    }
}
