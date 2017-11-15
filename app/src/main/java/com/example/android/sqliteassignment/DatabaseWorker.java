package com.example.android.sqliteassignment;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.List;

/**
 * Created by globe_000 on 11/13/2017.
 */

public class DatabaseWorker {

    private static final String TAG = DatabaseWorker.class.getName();

    public static Task insertTask(final AppDatabase db, Task task){
        db.taskDao().insertAll(task);
        return task;
    }

    public static boolean updateTask(final AppDatabase db, Task task){
        int count = db.taskDao().update(task);
        if(count < 1)
            return false;
        return true;
    }

    public static boolean deleteTask(final AppDatabase db, Task task){
        int count =db.taskDao().delete(task);
        if(count < 1)
            return false;
        return true;
    }

    public static List<Task> getAll(AppDatabase db){
        List<Task> taskList = db.taskDao().getAll();
        //Log.d("JAMES", taskList.toString());
        return taskList;
    }



}
