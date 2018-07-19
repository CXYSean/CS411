package com.example.chenxiyu.datepicker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private Button show_data;

    private MyDB mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydb = new MyDB(this);

        dateView = (TextView) findViewById(R.id.textView3);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        show_data = (Button)findViewById(R.id.go_display);

        show_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showData();
            }
        });
    }

    private void showData(){
        Intent intent = new Intent(this, DisplaySchedule.class);
        startActivity(intent);
    }

    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    year = arg1;
                    month = arg2+1;
                    day = arg3;
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    public void addEvent(View view){
        //TODO ADD the data to sqlite

        EditText e_name = (EditText)findViewById(R.id.et_name);
        String name = e_name.getText().toString();

        EditText e_time = (EditText)findViewById(R.id.et_time);
        String time = e_time.getText().toString();

        EditText e_dur = (EditText)findViewById(R.id.et_length);
        String duration = e_dur.getText().toString();
        int dur = Integer.parseInt(duration);
        EditText e_event = (EditText)findViewById(R.id.et_event);
        String event = e_event.getText().toString();

        String date = day+"/"+month+"/"+year;

        mydb.insertEvent(name, date, time,dur,event);

    }
}
