package com.example.offline.comments;

import android.arch.lifecycle.LiveData;

import com.example.offline.model.Comment;
import com.example.offline.model.LocalCommentDataStore;
import com.example.offline.model.ModelConstants;

import java.util.List;

class GetCommentsUseCase {
    private final LocalCommentDataStore localCommentDataStore;

    GetCommentsUseCase(LocalCommentDataStore localCommentDataStore) {
        this.localCommentDataStore = localCommentDataStore;
    }

    LiveData<List<Comment>> getComments() {
        return localCommentDataStore.getComments(ModelConstants.DUMMY_PHOTO_ID);
    }
}
