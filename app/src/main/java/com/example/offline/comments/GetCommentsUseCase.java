package com.example.offline.comments;

import com.example.offline.model.Comment;
import com.example.offline.model.ModelConstants;
import com.example.offline.model.PhotoCommentsRepository;

import java.util.List;

import io.reactivex.Single;

class GetCommentsUseCase {
    private final PhotoCommentsRepository photoCommentsRepository;

    GetCommentsUseCase(PhotoCommentsRepository photoCommentsRepository) {
        this.photoCommentsRepository = photoCommentsRepository;
    }

    Single<List<Comment>> getComments() {
        return photoCommentsRepository.getComments(ModelConstants.DUMMY_PHOTO_ID);
    }
}
