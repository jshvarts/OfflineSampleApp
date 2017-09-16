package com.example.offline.common.viewmodel;

import android.support.annotation.Nullable;

import static com.example.offline.common.viewmodel.Status.SUCCESS;
import static com.example.offline.common.viewmodel.Status.ERROR;

/**
 * Response holder provided to the UI
 *
 * @param <T>
 */
public class Response<T> {

    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final Throwable error;

    private Response(Status status, @Nullable T data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(SUCCESS, data, null);
    }

    public static <T> Response<T> error(Throwable error) {
        return new Response<>(ERROR, null, error);
    }
}
