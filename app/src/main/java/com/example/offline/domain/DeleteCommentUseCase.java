package com.example.offline.domain;

import com.example.offline.model.Comment;

import io.reactivex.Completable;

public class DeleteCommentUseCase {
    private final LocalCommentRepository localCommentRepository;

    public DeleteCommentUseCase(LocalCommentRepository localCommentRepository) {
        this.localCommentRepository = localCommentRepository;
    }

    public Completable deleteComment(Comment comment) {
        return localCommentRepository.delete(comment);
    }
}
