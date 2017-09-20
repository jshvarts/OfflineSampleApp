package com.example.offline.jobs;

import okhttp3.Response;

public class RemoteSyncDataException extends Exception {

    private final Response response;

    public RemoteSyncDataException(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
