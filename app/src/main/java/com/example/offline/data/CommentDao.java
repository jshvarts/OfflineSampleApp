package com.example.offline.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.offline.model.Comment;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long add(Comment comment);

    @Update
    void update(Comment comment);

    @Delete
    void delete(Comment comment);

    @Query("SELECT * FROM comment WHERE photo_id = :photoId ORDER BY timestamp DESC")
    Flowable<List<Comment>> getComments(long photoId);
}
