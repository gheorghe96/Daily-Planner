package com.example.gheorghe.notificationservice2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText editText_Search;
    Button btn_search;
    CalendarView calendarView;
    ListView listView;
    FloatingActionButton floatingBtnOption, floatingBtnAdd, floatingBtnSetting;
    myHelper MyHelper;
    SQLiteDatabase sqLiteDatabase;
    String stringDate;
    Calendar calendarDate;
    ArrayAdapter<String> adapter;
    Cursor cursor;
    String selectedItemListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Define Elements
        editText_Search = findViewById(R.id.editText_Search);
        btn_search = findViewById(R.id.btn_search);
        calendarView = findViewById(R.id.calendarView2);
        listView = findViewById(R.id.listView);
        floatingBtnAdd = findViewById(R.id.floatingBtnAdd);
        floatingBtnOption = findViewById(R.id.floatingBtnOption);
        floatingBtnSetting = findViewById(R.id.floatingBtnSetting);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);

        //Connect to database
        MyHelper = new myHelper(MainActivity.this,"database",null,1);
        sqLiteDatabase = MyHelper.getWritableDatabase();

        calendarDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyy");
        stringDate = sdf.format(calendarDate.getTime());

        cursor = sqLiteDatabase.rawQuery("select * from mytable where date like '%"+stringDate+"%'", null);
        adapter.clear();
        if (cursor.moveToFirst()) {
            int idDate = cursor.getColumnIndex("date");
            int idTask = cursor.getColumnIndex("task");
            do {
                String substringDate= cursor.getString(idDate).substring(cursor.getString(idDate).indexOf(" ") + 1);
                adapter.add("(" + substringDate + ")" + " - " + cursor.getString(idTask));
            }while (cursor.moveToNext());
        }
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
            selectedItemListView = adapter.getItemAtPosition(position).toString().substring(adapter.getItemAtPosition(position).toString().indexOf("- ") + 2);
            Intent intent = new Intent(MainActivity.this,UpdateActivity.class);
            intent.putExtra("updateString",selectedItemListView);
            startActivity(intent);
            finish();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_Search.getText().toString().trim().length() == 0) {
                    editText_Search.setError("Field is Empty");
                } else {
                    adapter.clear();
                    cursor = sqLiteDatabase.rawQuery("select * from mytable where task like '%"+editText_Search.getText().toString()+"%'",null);
                    if (cursor.moveToNext()) {
                        int idDate = cursor.getColumnIndex("date");
                        int idTask = cursor.getColumnIndex("task");
                        do {
                            adapter.add("(" + cursor.getString(idDate) + ")" + " - " + cursor.getString(idTask));
                        }while(cursor.moveToNext());
                    }
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    InputMethodManager inputManager = (InputMethodManager)MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                month += 1;
                stringDate = Integer.toString(day) + "." + Integer.toString(month) + "." + Integer.toString(year);
                cursor = sqLiteDatabase.rawQuery("select * from mytable where date like '%"+stringDate+"%'", null);
                adapter.clear();
                if (cursor.moveToFirst()) {
                    int idDate = cursor.getColumnIndex("date");
                    int idTask = cursor.getColumnIndex("task");
                    do {
                        String substringDate= cursor.getString(idDate).substring(cursor.getString(idDate).indexOf(" ") + 1);
                        adapter.add("(" + substringDate + ")" + " - " + cursor.getString(idTask));
                    }while (cursor.moveToNext());
                }
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        floatingBtnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (floatingBtnAdd.getVisibility() == View.VISIBLE) {
                    floatingBtnAdd.setVisibility(View.INVISIBLE);
                    floatingBtnSetting.setVisibility(View.INVISIBLE);
                } else {
                    floatingBtnAdd.setVisibility(View.VISIBLE);
                    floatingBtnSetting.setVisibility(View.VISIBLE);
                }
            }
        });

        floatingBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                intent.putExtra("calendarDate",stringDate);
                startActivity(intent);
                finish();
            }
        });

        floatingBtnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
                finish();
                /*sqLiteDatabase.delete("mytable",null,null);
                adapter.clear();
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"Data Base was cleared",Toast.LENGTH_SHORT).show();*/
            }
        });
    }
}
