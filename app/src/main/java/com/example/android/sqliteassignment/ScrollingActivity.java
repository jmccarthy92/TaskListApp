package com.example.android.sqliteassignment;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        updateView();

    }

    @Override
    protected void onResume(){
        super.onResume();
        //updateView();
        new AsyncDbWorker().execute();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        updateView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    public void updateView() {
        new AsyncDbWorker().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.insert) {
            Intent insertIntent = new Intent(this, InsertActivity.class);
            this.startActivity(insertIntent);
            return true;
            //return true;
        } else if (id == R.id.update){
            Intent updateIntent = new Intent(this, UpdateActivity.class);
            this.startActivity(updateIntent);
            return true;
        } else if (id == R.id.delete){
            Intent deleteIntent = new Intent(this, DeleteActivity.class);
            this.startActivity(deleteIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class AsyncDbWorker extends AsyncTask<Void, Void, List<Task> > {

        @Override
        protected List<Task> doInBackground(final Void... params){;
            return DatabaseWorker.getAll(AppDatabase.getAppDatabase(ScrollingActivity.this));
        }

        @Override
        protected void onPostExecute(List<Task> taskList){
            ScrollView taskDisplay = (ScrollView) findViewById(R.id.taskDisplay);
            LinearLayout layout = new LinearLayout(ScrollingActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);

            if( taskList.size() > 0){
                String taskListStr="";
                String [] colors = new String[]{"#FF0000", "#DD0000", "#BB0000","#AA0000","#880000","#770000"};
                taskDisplay.removeAllViews();
                Collections.sort(taskList);
                TextView[] ids = new TextView[taskList.size()];
                int i = 0;
                for(Task task : taskList) {
                    ids[i] = new TextView(ScrollingActivity.this);
                    ids[i].setGravity( Gravity.CENTER );
                    taskListStr = task.getTaskID() + " , " + task.getTaskName() + " , " + task.getDateDue() + "\n";
                    ids[i].setText(taskListStr);
                    ids[i].setBackgroundColor(Color.parseColor(colors[i % colors.length]));
                    layout.addView(ids[i]);
                    i++;
                }

                taskDisplay.addView(layout);

            } else {
                TextView taskDefault = new TextView(ScrollingActivity.this);
                taskDefault.setText("No Tasks in the database.");
                taskDisplay.addView(taskDefault);

            }
        }
    }
}
