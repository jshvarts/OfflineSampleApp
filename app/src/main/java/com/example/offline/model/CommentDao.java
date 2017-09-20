package com.example.offline.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long add(Comment comment);

    @Update
    void update(Comment comment);

    @Query("SELECT * FROM comment WHERE photo_id = :photoId ORDER BY timestamp DESC")
    List<Comment> getComments(long photoId);
}
