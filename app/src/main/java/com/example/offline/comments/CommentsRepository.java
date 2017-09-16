package com.example.offline.comments;

import io.reactivex.Single;

class CommentsRepository {
    Single<String> getGreeting() {
        return Single.just("Hello from CommentsRepository");
    }
}
