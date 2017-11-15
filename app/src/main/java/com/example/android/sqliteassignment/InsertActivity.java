package com.example.android.sqliteassignment;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InsertActivity extends AppCompatActivity {

    private EditText taskName;
    private DatePicker taskDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        taskName = (EditText) findViewById(R.id.taskName);
        taskDate = (DatePicker) findViewById(R.id.taskDate);

    }

    public void goBack(View v){
        this.finish();
    }

    public void insert(View v){
        Task task = new Task();
        task.setTaskName(taskName.getText().toString() );
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, taskDate.getYear());
        calendar.set(Calendar.MONTH, taskDate.getMonth());
        calendar.set(Calendar.DATE, taskDate.getDayOfMonth());
        Date date = calendar.getTime();
        task.setDateDue(date);
        new AsyncDbWorker().execute(task);

    }

    public class AsyncDbWorker extends AsyncTask<Task, Void, Task > {

        @Override
        protected Task doInBackground(Task... params){
            return DatabaseWorker.insertTask(AppDatabase.getAppDatabase(InsertActivity.this), params[0]);
        }

        @Override
        protected void onPostExecute(Task task){
            if( task != null){
                Toast.makeText(InsertActivity.this, "Task '"+task.getTaskName()+"' has been added", Toast.LENGTH_LONG).show();
                InsertActivity.this.finish();
            }
        }
    }
}
