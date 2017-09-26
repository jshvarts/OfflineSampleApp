package com.example.offline.comments;

import com.example.offline.model.LocalCommentDataStore;
import com.example.offline.model.ModelConstants;

import io.reactivex.Completable;

class AddCommentUseCase {
    private final LocalCommentDataStore localCommentDataStore;
    private final SyncCommentUseCase syncCommentUseCase;

    AddCommentUseCase(LocalCommentDataStore localCommentDataStore, SyncCommentUseCase syncCommentUseCase) {
        this.localCommentDataStore = localCommentDataStore;
        this.syncCommentUseCase = syncCommentUseCase;
    }

    Completable addComment(String commentText) {
        return localCommentDataStore.add(ModelConstants.DUMMY_PHOTO_ID, commentText)
                .flatMapCompletable(comment -> syncCommentUseCase.syncComment(comment));
    }
}
