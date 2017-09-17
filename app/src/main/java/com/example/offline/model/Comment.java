package com.example.offline.model;

public class Comment {
    private String text;
    private boolean syncPending;

    public Comment(String text) {
        this.text = text;
        this.syncPending = true;
    }
}
