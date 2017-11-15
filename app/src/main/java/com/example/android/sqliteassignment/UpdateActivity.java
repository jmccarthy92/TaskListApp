package com.example.android.sqliteassignment;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Update;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UpdateActivity extends AppCompatActivity {

    DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        updateView();
    }

    public void updateView(){
        new AsyncDbWorker().execute();
    }

    public void updateViewWithTasks(List<Task> taskList) {
            RelativeLayout layout = new RelativeLayout(this);
            ScrollView scrollView = new ScrollView( this );
            GridLayout grid = new GridLayout( this );
            grid.setRowCount( taskList.size());
            grid.setColumnCount( 4 );

            TextView[] ids = new TextView[taskList.size()];
            EditText[] nameEdit = new EditText[taskList.size()];
            EditText[] datePicker = new EditText[taskList.size()];
            Button [] buttons = new Button[taskList.size()];
            ButtonHandler bh = new ButtonHandler();

            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize( size );
            int width = size.x;

            int i = 0;

            for( Task task : taskList){
                ids[i] = new TextView( this );
                ids[i].setGravity( Gravity.CENTER );
                ids[i].setText("" + task.getTaskID());

                nameEdit[i] = new EditText(this);
                nameEdit[i].setText(task.getTaskName());
                nameEdit[i].setId( 10 * task.getTaskID());

                datePicker[i] = new EditText(this);
                datePicker[i].setText(dFormat.format(task.getDateDue()));
                datePicker[i].setId( 10 * task.getTaskID() + 1);
                datePicker[i].setInputType(InputType.TYPE_DATETIME_VARIATION_DATE | InputType.TYPE_CLASS_DATETIME);


                buttons[i] = new Button( this );
                buttons[i].setText("Update");
                buttons[i].setId( task.getTaskID());
                buttons[i].setOnClickListener(bh);

                grid.addView( ids[i], width / 10, ViewGroup.LayoutParams.WRAP_CONTENT);
                grid.addView( nameEdit[i], (int) (width * .3), ViewGroup.LayoutParams.WRAP_CONTENT);
                grid.addView( datePicker[i], (int) (width * .4), ViewGroup.LayoutParams.WRAP_CONTENT);
                grid.addView( buttons[i], (int)(width * .2), ViewGroup.LayoutParams.WRAP_CONTENT );

                i++;
            }

            scrollView.addView( grid );
            layout.addView(scrollView);
            RelativeLayout.LayoutParams params
                    = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT );
            params.addRule( RelativeLayout.ALIGN_PARENT_BOTTOM );
            params.addRule( RelativeLayout.CENTER_HORIZONTAL );
            params.setMargins( 0, 0, 0, 50 );
            Button backButton = new Button( this );
            backButton.setText(" Go Back ");
            backButton.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    UpdateActivity.this.finish();
                }
            });
            layout.addView(backButton, params);
            setContentView(layout);
       // }
    }

    private class ButtonHandler implements View.OnClickListener {

        @Override
        public void onClick(View v){
            Date date;
            int taskId = v.getId();
            EditText nameET = (EditText) findViewById( 10 * taskId);
            EditText dateET = (EditText) findViewById(10 * taskId + 1);
            String name = nameET.getText().toString();
            try {
                String dateStr = dateET.getText().toString() ;
                Calendar calendar = Calendar.getInstance();
                String [] dateStrSplit = dateStr.split("-");
                calendar.set(Calendar.YEAR, Integer.parseInt(dateStrSplit[0]));
                calendar.set(Calendar.MONTH, Integer.parseInt(dateStrSplit[1]) - 1);
                calendar.set(Calendar.DATE, Integer.parseInt(dateStrSplit[2]));
                date = calendar.getTime();
                Task task = new Task();
                task.setTaskID(taskId);
                task.setTaskName(name);
                task.setDateDue(date);
                Log.d("JAMES2", task.getDateDue().toString());
                new AsyncDbUpdateWorker().execute(task);
                updateView();
            } catch( Exception e){
                Log.d("DEBUG", e.toString());
                Toast.makeText(UpdateActivity.this, "Error Ocurred updating", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AsyncDbUpdateWorker extends AsyncTask<Task, Void, Boolean > {

        @Override
        protected Boolean doInBackground(Task... params){
            return DatabaseWorker.updateTask(AppDatabase.getAppDatabase(UpdateActivity.this), params[0]);
        }

        @Override
        protected void onPostExecute(Boolean bool){
            if( bool){
                Toast.makeText( UpdateActivity.this, "Task Updated" , Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText( UpdateActivity.this, "Error Occurred in Updating Make Sure to Input date as MM/dd/yyyy", Toast.LENGTH_SHORT).show();
            }
            updateView();
        }
    }

    private class AsyncDbWorker extends AsyncTask<Void, Void, List<Task> > {

        @Override
        protected List<Task> doInBackground(final Void... params){
            return DatabaseWorker.getAll(AppDatabase.getAppDatabase(UpdateActivity.this));
        }

        @Override
        protected void onPostExecute(List<Task> taskList){
                updateViewWithTasks(taskList);
        }
    }



}
