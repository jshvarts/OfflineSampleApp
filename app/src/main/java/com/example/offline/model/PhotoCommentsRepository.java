package com.example.offline.model;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Single;
import timber.log.Timber;

public class PhotoCommentsRepository {

    private static final String DUMMY_PHOTO_ID = "1";

    // basic in-memory data store implementation
    private Map<String, Comment> comments = new TreeMap<>();

    public Single<Comment> addComment(String commentText) {
        Timber.d("creating comment with text " + commentText);

        Comment comment = createComment(commentText);

        // add new comment
        comments.put(comment.getId(), comment);

        return Single.just(comment);
    }

    public Completable updateCommentSyncStatus(String commentId) {
        Timber.d("updating comment sync status for comment id " + commentId);
        if (!comments.containsKey(commentId)) {
            return Completable.error(new RepositoryDataException("No comment exists for id: " + commentId));
        } else {
            Comment comment = createCommentFromExisting(comments.get(commentId), false);

            // replace existing comment
            comments.put(commentId, comment);

            return Completable.complete();
        }
    }

    /**
     * Generates new comment with default syncPending = true
     */
    private Comment createComment(String commentText) {
        String commentId = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();
        return new Comment(commentId, DUMMY_PHOTO_ID, commentText, timestamp, false);
    }

    /**
     * Generates new comment based on existing one
     */
    private Comment createCommentFromExisting(Comment existing, boolean syncPending) {
        return new Comment(existing.getId(), existing.getPhotoId(),
                existing.getText(), existing.getTimestamp(), syncPending);
    }
}
