package com.example.offline.comments;

import com.example.offline.model.PhotoCommentsRepository;

import io.reactivex.Completable;

class UpdateCommentUseCase {
    private final PhotoCommentsRepository photoCommentsRepository;

    UpdateCommentUseCase(PhotoCommentsRepository photoCommentsRepository) {
        this.photoCommentsRepository = photoCommentsRepository;
    }

    Completable updateCommentSyncStatus(String commentId) {
        return photoCommentsRepository.updateCommentSyncStatus(commentId);
    }
}
