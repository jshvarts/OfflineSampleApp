package com.example.offline.comments;

import com.example.offline.model.Comment;
import com.example.offline.model.LocalCommentDataStore;
import com.example.offline.model.ModelConstants;

import java.util.List;

import io.reactivex.Single;

class GetCommentsUseCase {
    private final LocalCommentDataStore localCommentDataStore;

    GetCommentsUseCase(LocalCommentDataStore localCommentDataStore) {
        this.localCommentDataStore = localCommentDataStore;
    }

    Single<List<Comment>> getComments() {
        return localCommentDataStore.getComments(ModelConstants.DUMMY_PHOTO_ID);
    }
}
