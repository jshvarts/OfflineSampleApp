package com.example.offline.comments;

import com.example.offline.model.Comment;

import io.reactivex.Completable;
import timber.log.Timber;

class CommentsRepository {
    Completable addComment(Comment comment) {
        Timber.d("adding comment " + comment);
        return Completable.complete();
    }

    Completable updateComment(Comment comment) {
        Timber.d("updating comment " + comment);
        return Completable.complete();
    }
}
