package com.example.chenxiyu.datepicker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UpdateEvent extends AppCompatActivity {

    TextView day;
    TextView user;
    TextView time_start;
    TextView event;
    TextView duration;

    private String new_name;
    private String new_time;
    private String new_date;
    private Integer new_dur;
    private String new_event;

    private Button update;

    private MyDB mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);

        mydb = new MyDB(this);
        update = (Button) findViewById(R.id.update);

        Intent intent = getIntent();
        String s_id = intent.getStringExtra(DisplaySchedule.EXTRA_MESSAGE);
        System.out.print(s_id);
        if(s_id == null || s_id == ""){

            finish();
        }


        final Integer _id = Integer.parseInt(s_id);

        Cursor rs = mydb.getById(_id);
        rs.moveToFirst();

        String nam = rs.getString(rs.getColumnIndex(MyDB.CONTACTS_COLUMN_NAME));
        String dat = rs.getString(rs.getColumnIndex(MyDB.CONTACTS_COLUMN_DATE));
        String tim = rs.getString(rs.getColumnIndex(MyDB.CONTACTS_COLUMN_TIME));
        String eve = rs.getString(rs.getColumnIndex(MyDB.CONTACTS_COLUMN_EVENT));
        Integer dur = rs.getInt(rs.getColumnIndex(MyDB.CONTACTS_COLUMN_DURATION));

        new_name = nam;
        new_time = tim;
        new_date = dat;
        new_dur = dur;
        new_event = eve;

        day = (TextView)findViewById(R.id.textView3);
        day.setText(dat);

        user = (TextView)findViewById(R.id.et_name);
        user.setText(nam);

        time_start = (TextView)findViewById(R.id.et_time);
        time_start.setText(tim);

        event = (TextView)findViewById(R.id.et_event);
        event.setText(eve);

        duration = (TextView)findViewById(R.id.et_length);
        duration.setText(String.valueOf(dur));


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText e_name = (EditText)findViewById(R.id.et_name);
                new_name = e_name.getText().toString();

                EditText e_time = (EditText)findViewById(R.id.et_time);
                new_time = e_time.getText().toString();

                EditText e_dur = (EditText)findViewById(R.id.et_length);
                String duration = e_dur.getText().toString();
                new_dur = Integer.parseInt(duration);
                EditText e_event = (EditText)findViewById(R.id.et_event);
                new_event = e_event.getText().toString();

                mydb.updateEvent(_id, new_name, new_date, new_time, new_dur, new_event);
            }
        });
    }
}
