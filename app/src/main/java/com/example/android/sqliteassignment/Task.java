package com.example.android.sqliteassignment;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.provider.SyncStateContract;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by globe_000 on 11/13/2017.
 */
@Entity(tableName="task")
public class Task implements Comparable<Task>{

    @PrimaryKey(autoGenerate = true)
    private int taskID;

    @ColumnInfo(name = "task_name")
    private String taskName;

    @ColumnInfo(name = "date_due")
    @TypeConverters({Task.DateConverter.class})
    private Date dateDue;

    public int getTaskID(){ return taskID; }

    public String getTaskName() { return taskName; }

    public Date getDateDue() { return dateDue;}

    public void setTaskID(int id) { this.taskID = id; }

    public void setTaskName(String name){ this.taskName = name;}

    public void setDateDue(Date date){ this.dateDue = date;}

    @Override
    public String toString(){
        return getTaskID() + " , " + getTaskName() + " , " + getDateDue();
    }

    public static class DateConverter {
        private String timePattern = "yyyy-MM-dd HH:mm:ss";
        private DateFormat df = new SimpleDateFormat(timePattern, Locale.US);

        @TypeConverter
        public Date fromDate(String value){
            if( value != null){
               try {
                   Log.d("JAMES3", value);
                   return new Date(value);
               } catch(Exception pe){
                   pe.printStackTrace();
               }
               return null;
            } else {
                return null;
            }
        }

        @TypeConverter
        public String toDate(Date date){
            if(date == null)
                return null;
            Log.d("JAMES4", date.toString());
            return date.toString();
        }

    }

    @Override
    public int compareTo(Task o){
        if( getDateDue() == null || o.getDateDue() == null)
            return 0;
        return getDateDue().compareTo(o.getDateDue());
    }


}
