package com.example.gheorghe.notificationservice2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    TimePicker timePicker;
    EditText editText_Task;
    Button btn_add;
    myHelper MyHelper;
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    long alertTime;
    int requestCode;
    SimpleDateFormat dateFormat;
    Date date;
    String DBdate;
    String newEventDate;
    String insertDate;
    String timePickerTime;
    Intent intent;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        timePicker = findViewById(R.id.timePicker2);
        editText_Task = findViewById(R.id.editText_Task);
        btn_add = findViewById(R.id.btn_add);

        dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        //Conect to DataBase
        MyHelper = new myHelper(AddActivity.this,"database",null,1);
        sqLiteDatabase = MyHelper.getWritableDatabase();

        newEventDate = getIntent().getStringExtra("calendarDate");

        timePickerTime = timePicker.getCurrentHour().toString() + ":" + timePicker.getCurrentMinute().toString();
        insertDate = newEventDate + " " + timePickerTime;

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                timePickerTime = Integer.toString(hour) + ":" + Integer.toString(minute);
                insertDate = newEventDate + " " + timePickerTime;
                Log.d("Time_Picker",insertDate);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_Task.getText().toString().trim().length() == 0) {
                    editText_Task.setError("Field is empty");
                } else {
                    contentValues = new ContentValues();
                    contentValues.put("date",insertDate);
                    contentValues.put("task",editText_Task.getText().toString());
                    contentValues.put("notify","false");
                    sqLiteDatabase.insert("mytable",null,contentValues);

                    cursor = sqLiteDatabase.rawQuery("select * from mytable",null);
                    if (cursor.moveToFirst()){
                        int id = cursor.getColumnIndex("id");
                        int idDate = cursor.getColumnIndex("date");
                        int idTask = cursor.getColumnIndex("task");
                        int idNotify = cursor.getColumnIndex("notify");
                        do {
                            Log.d("DatabaseFromAdd",cursor.getString(id) + " | " + cursor.getString(idDate) + " | " + cursor.getString(idTask) + " | " + cursor.getString(idNotify));
                        }while (cursor.moveToNext());
                    }

                    if (cursor.moveToFirst()) {
                        if (cursor.moveToLast()) {
                            int id = cursor.getColumnIndex("id");
                            int idDate = cursor.getColumnIndex("date");
                            do {
                                requestCode = cursor.getInt(id);
                                DBdate = cursor.getString(idDate);
                                Log.d("DatabaseFromAdd","-------------------------------------------------------------");
                                Log.d("DatabaseFromAdd",Integer.toString(requestCode) + " " + DBdate);
                            }while (cursor.moveToNext());
                        }
                    }
                    try
                    {
                        date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(DBdate);
                        alertTime = date.getTime();
                    }
                    catch (Exception ex ){

                    }
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    String currentTime = sdf.format(calendar.getTime());
                    if (DBdate.compareTo(currentTime) > 0) {
                        intent = new Intent(getApplicationContext(), AlarmReceiver.class); //ALARM IS SET
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), requestCode, intent, 0);
                        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, pendingIntent);
                    }
                    Toast.makeText(getApplicationContext(),"Event added successfully",Toast.LENGTH_SHORT).show();
                    intent = new Intent(AddActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}
