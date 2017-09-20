package com.example.offline.comments;

import com.example.offline.model.Comment;
import com.example.offline.model.LocalCommentDataStore;
import com.example.offline.model.ModelConstants;

import io.reactivex.Single;

class AddCommentUseCase {
    private final LocalCommentDataStore localCommentDataStore;

    AddCommentUseCase(LocalCommentDataStore localCommentDataStore) {
        this.localCommentDataStore = localCommentDataStore;
    }

    Single<Comment> addComment(String commentText) {
        return localCommentDataStore.add(ModelConstants.DUMMY_PHOTO_ID, commentText);
    }
}
