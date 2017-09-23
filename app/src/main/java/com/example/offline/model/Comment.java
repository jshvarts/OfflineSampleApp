package com.example.offline.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Immutable POJO that represents a Comment
 */
@Entity
public class Comment implements Serializable {

    @Expose
    @PrimaryKey(autoGenerate = true)
    private long id;

    @Expose
    @ColumnInfo(name = "photo_id")
    private long photoId;

    @Expose
    @ColumnInfo(name = "comment_text")
    private String commentText;

    @Expose
    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "sync_pending")
    private boolean syncPending;

    public long getId() {
        return id;
    }

    public long getPhotoId() {
        return photoId;
    }

    public String getCommentText() {
        return commentText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isSyncPending() {
        return syncPending;
    }

    @Ignore
    public Comment(long photoId, String commentText) {
        this.photoId = photoId;
        this.commentText = commentText;
        this.timestamp = System.currentTimeMillis();
        this.syncPending = true;
    }

    public Comment(long id, long photoId, String commentText, long timestamp, boolean syncPending) {
        this.id = id;
        this.photoId = photoId;
        this.commentText = commentText;
        this.timestamp = timestamp;
        this.syncPending = syncPending;
    }

    @Override
    public String toString() {
        return String.format("Comment id: %s, text: %s, syncPending: %s", id, commentText, syncPending);
    }
}
