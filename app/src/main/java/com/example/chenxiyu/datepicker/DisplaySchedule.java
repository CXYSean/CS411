package com.example.chenxiyu.datepicker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class DisplaySchedule extends AppCompatActivity {
    private MyDB mydb ;

    TextView day;
    TextView start_time;
    TextView event;

    private Context context = null;

    TableLayout tableLayout;

    public static final String EXTRA_MESSAGE = "com.example.chenxiyu.datepicker.DisplaySchedule";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_schedule);
        mydb = new MyDB(this);

        tableLayout = (TableLayout)findViewById(R.id.table_layout);
        context = getApplicationContext();
    }

    public void showSchedule(View view){
        TextView name = (TextView) findViewById(R.id.text_name);
        String search_name = name.getText().toString();
        Cursor rs = mydb.getData();
        rs.moveToFirst();

        String dat = rs.getString(rs.getColumnIndex(MyDB.CONTACTS_COLUMN_DATE));
        String tim = rs.getString(rs.getColumnIndex(MyDB.CONTACTS_COLUMN_TIME));
        String eve = rs.getString(rs.getColumnIndex(MyDB.CONTACTS_COLUMN_EVENT));

        while(rs.moveToNext()) {
            dat = rs.getString(rs.getColumnIndex(MyDB.CONTACTS_COLUMN_DATE));
            tim = rs.getString(rs.getColumnIndex(MyDB.CONTACTS_COLUMN_TIME));
            eve = rs.getString(rs.getColumnIndex(MyDB.CONTACTS_COLUMN_EVENT));
            final Integer _id = rs.getInt(rs.getColumnIndex(MyDB.CONTACTS_COLUMN_ID));

            final TableRow tableRow = new TableRow(context);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            tableRow.setLayoutParams(layoutParams);

            TextView textView = new TextView(context);
            textView.setText((CharSequence) dat+" ");
            tableRow.addView(textView, 0);


            TextView textView2 = new TextView(context);
            textView2.setText((CharSequence) tim+" ");
            tableRow.addView(textView2, 1);

            TextView textView3 = new TextView(context);
            textView3.setText((CharSequence) eve);
            tableRow.addView(textView3, 2);

            CheckBox checkBox = new CheckBox(context);
            checkBox.setText("Check it");
            tableRow.addView(checkBox, 3);

            Button button = new Button(context);
            button.setText("update");
            tableRow.addView(button, 4);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tableLayout.removeAllViewsInLayout();
                    updateEvent(_id.toString());
                }
            });

            Button button1 = new Button(context);
            button1.setText("delete");
            tableRow.addView(button1, 5);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mydb.deleteEvent(_id);
                    tableLayout.removeView(tableRow);

                }
            });
            tableLayout.addView(tableRow);
        }

    }

    public void updateEvent(String id){
        Intent intent = new Intent(this, UpdateEvent.class);
        String _id = id;
        intent.putExtra(EXTRA_MESSAGE, _id);
        startActivity(intent);
    }



}
