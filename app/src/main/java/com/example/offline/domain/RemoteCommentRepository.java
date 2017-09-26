package com.example.offline.domain;

import com.example.offline.model.Comment;

import io.reactivex.Completable;

public interface RemoteCommentRepository {
    Completable sync(Comment comment);
}
