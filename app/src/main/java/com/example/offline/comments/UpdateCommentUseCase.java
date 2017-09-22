package com.example.offline.comments;

import com.example.offline.model.Comment;
import com.example.offline.model.LocalCommentDataStore;

import io.reactivex.Completable;

class UpdateCommentUseCase {
    private final LocalCommentDataStore localCommentDataStore;

    UpdateCommentUseCase(LocalCommentDataStore localCommentDataStore) {
        this.localCommentDataStore = localCommentDataStore;
    }

    Completable updateComment(Comment comment) {
        return localCommentDataStore.update(comment);
    }
}
