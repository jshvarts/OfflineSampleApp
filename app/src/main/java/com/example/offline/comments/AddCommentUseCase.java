package com.example.offline.comments;

import com.example.offline.model.Comment;
import com.example.offline.model.PhotoCommentsRepository;

import io.reactivex.Single;

class AddCommentUseCase {
    private final PhotoCommentsRepository photoCommentsRepository;

    AddCommentUseCase(PhotoCommentsRepository photoCommentsRepository) {
        this.photoCommentsRepository = photoCommentsRepository;
    }

    Single<Comment> addComment(String commentText) {
        return photoCommentsRepository.addComment(commentText);
    }
}
