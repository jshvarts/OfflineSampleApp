package com.example.offline.comments;

import javax.inject.Inject;

import io.reactivex.Single;

class AddCommentUseCase {
    private final CommentsRepository greetingRepository;

    @Inject
    AddCommentUseCase(CommentsRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }

    Single<String> execute() {
        return greetingRepository.getGreeting();
    }
}
