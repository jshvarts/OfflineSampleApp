package com.example.offline.model;

public class Comment {

    private int id;
    private int photoId;
    private String text;
    private long timestamp;
    private boolean syncPending;

    public int getId() {
        return id;
    }

    public int getPhotoId() {
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

    private Comment(Builder builder) {
        this.id = builder.id;
        this.photoId = builder.photoId;
        this.text = builder.text;
        this.timestamp = builder.timestamp;
        this.syncPending = builder.syncPending;
    }

    public static class Builder {
        private final int id;
        private int photoId;
        private String text;
        private long timestamp;
        private boolean syncPending;

        public Builder(int id) {
            this.id = id;
        }

        public Builder photoId(int photoId) {
            this.photoId = photoId;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder syncPending(boolean syncPending) {
            this.syncPending = syncPending;
            return this;
        }

        public Comment build() {
            return new Comment(this);
        }
    }

    @Override
    public String toString() {
        return String.format("Comment id: %s, text: %s, syncPending: %s", id, text, syncPending);
    }
}
