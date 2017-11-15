package com.example.android.sqliteassignment;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.List;

import static android.R.attr.button;

public class DeleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        updateView();
    }

    public void updateView() {;
        new AsyncDbWorker().execute();
    }

    public void updateViewWithTasks(List<Task> taskList){
        RelativeLayout layout = new RelativeLayout( this );
        ScrollView scrollView = new ScrollView(this);
        RadioGroup group = new RadioGroup( this );
        for( Task task : taskList){
            RadioButton rb = new RadioButton(this);
            rb.setId( task.getTaskID());
            rb.setText( task.getTaskID() + " , " + task.getTaskName() + " , " + task.getDateDue());
            group.addView( rb );
        }

        RadioButtonHandler rbh = new RadioButtonHandler();
        group.setOnCheckedChangeListener(rbh);

        Button backButton = new Button( this );
        backButton.setText(" Go Back ");
        backButton.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                DeleteActivity.this.finish();
            }
        });

        scrollView.addView(group);
        layout.addView( scrollView );
        RelativeLayout.LayoutParams params
                = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT );
        params.addRule( RelativeLayout.ALIGN_PARENT_BOTTOM );
        params.addRule( RelativeLayout.CENTER_HORIZONTAL );
        params.setMargins( 0, 0, 0, 50 );
        layout.addView( backButton, params );
        setContentView( layout );
    }

    private class RadioButtonHandler implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            Task task = new Task();
            task.setTaskID(checkedId);
            new AsyncDbDeleteWorker().execute(task);
        }
    }

    private class AsyncDbDeleteWorker extends AsyncTask<Task, Void, Boolean > {

        @Override
        protected Boolean doInBackground(Task... params){
            return DatabaseWorker.deleteTask(AppDatabase.getAppDatabase(DeleteActivity.this), params[0]);
        }

        @Override
        protected void onPostExecute(Boolean bool){
            if( bool){
                Toast.makeText( DeleteActivity.this, "Task Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText( DeleteActivity.this, "Error Occurred in Deleting", Toast.LENGTH_SHORT).show();
            }
            updateView();
        }
    }

    private class AsyncDbWorker extends AsyncTask<Void, Void, List<Task> > {

        @Override
        protected List<Task> doInBackground(final Void... params){
            return DatabaseWorker.getAll(AppDatabase.getAppDatabase(DeleteActivity.this));
        }

        @Override
        protected void onPostExecute(List<Task> taskList){
                updateViewWithTasks(taskList);
        }
    }


}
