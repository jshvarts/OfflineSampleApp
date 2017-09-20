package com.example.offline.model;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import timber.log.Timber;

public class LocalCommentDataStore {

    private final CommentDao commentDao;

    public LocalCommentDataStore(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    /**
     * Adds a comment to a given photo
     */
    public Single<Comment> add(long photoId, String commentText) {
        Timber.d("creating comment for photo id %s, comment text %s", photoId, commentText);

        Comment comment = new Comment(photoId, commentText);

        return Single.fromCallable(() -> {
            long rowId = commentDao.add(comment);
            Timber.d("comment stored " + rowId);
            return Comment.clone(comment, rowId);
        });
    }

    /**
     * Updates a comment
     */
    public Completable update(Comment comment) {
        Timber.d("updating comment sync status for comment id %s", comment.getId());

        return Completable.fromAction(() -> commentDao.update(comment));
    }

    /**
     * Returns comments for a given photo
     */
    public Single<List<Comment>> getComments(long photoId) {
        Timber.d("getting comments for photo id %s", photoId);

        return Single.fromCallable(() -> commentDao.getComments(photoId));
    }
}
