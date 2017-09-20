package com.example.offline.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Comment.class}, version = 1, exportSchema = false)
public abstract class CommentDatabase extends RoomDatabase {
    public abstract CommentDao commentDao();

    private static CommentDatabase instance;

    public static synchronized CommentDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context.getApplicationContext(), CommentDatabase.class, "offlinedb")
                    .build();
        }
        return instance;
    }
}
