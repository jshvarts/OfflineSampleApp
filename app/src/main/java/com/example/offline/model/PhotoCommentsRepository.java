package com.example.offline.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Completable;
import io.reactivex.Single;
import timber.log.Timber;

public class PhotoCommentsRepository {

    private AtomicInteger idGenerator = new AtomicInteger();

    private Comparator<Integer> sortByCommentIdDesc = (Comparator<Integer>) (Integer o1, Integer o2) -> Integer.compare(o2, o1);

    // basic in-memory data store sorted by comment ids
    private Map<Integer, Comment> comments = new TreeMap<>(sortByCommentIdDesc);

    /**
     * Adds a comment to a given photo
     */
    public Single<Comment> addComment(int photoId, String commentText) {
        Timber.d("creating comment for photo id %s, comment text %s", photoId, commentText);

        Comment comment = createComment(photoId, commentText);

        // add new comment
        comments.put(comment.getId(), comment);

        return Single.just(comment);
    }

    /**
     * Updates syncPending to false for a given comment
     */
    public Completable updateCommentSyncStatus(int commentId) {
        Timber.d("updating comment sync status for comment id %s", commentId);

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
     * Returns comments for a given photo
     */
    public Single<List<Comment>> getComments(int photoId) {
        Timber.d("getting comments for photo id %s", photoId);

        List<Comment> result = new ArrayList<>();
        for(Map.Entry<Integer, Comment> entry : comments.entrySet()) {
            Comment comment = entry.getValue();
            if (comment.getPhotoId() == photoId) {
                result.add(comment);
            }
        }
        return Single.just(result);
    }

    /**
     * Generates new comment with default syncPending = true
     */
    private Comment createComment(int photoId, String commentText) {
        return new Comment.Builder(idGenerator.incrementAndGet())
                .photoId(photoId)
                .text(commentText)
                .timestamp(System.currentTimeMillis())
                .syncPending(true)
                .build();
    }

    /**
     * Generates new comment based on the existing one
     */
    private Comment createCommentFromExisting(Comment existing, boolean syncPending) {
        return new Comment.Builder(existing.getId())
                .photoId(existing.getPhotoId())
                .text(existing.getText())
                .timestamp(existing.getTimestamp())
                .syncPending(syncPending)
                .build();
    }
}
