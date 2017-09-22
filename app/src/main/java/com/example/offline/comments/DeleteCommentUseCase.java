package com.example.offline.comments;

import com.example.offline.model.Comment;
import com.example.offline.model.LocalCommentDataStore;

import io.reactivex.Completable;

class DeleteCommentUseCase {
    private final LocalCommentDataStore localCommentDataStore;

    DeleteCommentUseCase(LocalCommentDataStore localCommentDataStore) {
        this.localCommentDataStore = localCommentDataStore;
    }

    Completable deleteComment(Comment comment) {
        return localCommentDataStore.delete(comment);
    }
}
