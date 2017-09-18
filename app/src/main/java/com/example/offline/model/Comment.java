package com.example.offline.model;

public class Comment {

    private String id;
    private String photoId;
    private String text;
    private long timestamp;
    private boolean syncPending;


    public Comment(String id, String photoId, String text, long timestamp, boolean syncPending) {
        this.id = id;
        this.photoId = photoId;
        this.text = text;
        this.timestamp = timestamp;
        this.syncPending = syncPending;
    }

    public String getId() {
        return id;
    }

    public String getPhotoId() {
        return photoId;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isSyncPending() {
        return syncPending;
    }

    @Override
    public String toString() {
        return String.format("Comment id: %s, text: %s, syncPending: %s", id, text, syncPending);
    }
}
