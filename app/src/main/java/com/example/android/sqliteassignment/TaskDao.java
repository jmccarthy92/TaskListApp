package com.example.android.sqliteassignment;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by globe_000 on 11/13/2017.
 */
@Dao
public interface TaskDao {

    @Query("SELECT * FROM task ORDER BY datetime(date_due) LIMIT 20")
    List<Task> getAll();

    @Insert
    void insertAll(Task... tasks);

    @Delete
    int delete(Task task);

    @Update
    int update(Task task);
}
