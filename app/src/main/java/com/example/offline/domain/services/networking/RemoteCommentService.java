package com.example.offline.domain.services.networking;

import com.example.offline.BuildConfig;
import com.example.offline.model.Comment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public final class RemoteCommentService {

    private static RemoteCommentService instance;

    private final Retrofit retrofit;

    public RemoteCommentService() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BuildConfig.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static synchronized RemoteCommentService getInstance() {
        if (instance == null) {
            instance = new RemoteCommentService();
        }
        return instance;
    }

    public void addComment(Comment comment) throws IOException, RemoteException {
        RemoteCommentEndpoint service = retrofit.create(RemoteCommentEndpoint.class);

        // Remote call can be executed synchronously since the job calling it is already backgrounded.
        Response<Comment> response = service.addComment(comment).execute();

        if (response == null || !response.isSuccessful() || response.errorBody() != null) {
            throw new RemoteException(response);
        }

        Timber.d("successful remote response: " + response.body());
    }
}
