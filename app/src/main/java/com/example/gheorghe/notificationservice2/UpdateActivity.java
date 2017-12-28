package com.example.gheorghe.notificationservice2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabase;
    myHelper MyHelper;
    String updateString;
    EditText editText_update;
    Button btn_update,btn_delete;
    ContentValues content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        editText_update = findViewById(R.id.editText_Update);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);

        MyHelper = new myHelper(getApplicationContext(),"database",null,1);
        sqLiteDatabase = MyHelper.getWritableDatabase();

        updateString = getIntent().getStringExtra("updateString");
        Log.d("UpdateString",updateString);

        content = new ContentValues();

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_update.getText().toString().trim().length() == 0) {
                    editText_update.setError("Field is empty");
                } else {
                    content.put("task",editText_update.getText().toString());
                    Log.d("UpdateString",editText_update.getText().toString());
                    sqLiteDatabase.update("mytable",content,"task = '"+updateString+"'",null);
                }
                Intent intent = new Intent(UpdateActivity.this,MainActivity.class);
                Toast.makeText(getApplicationContext(),"The item Has beed updated",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqLiteDatabase.delete("mytable","task = '"+updateString+"'",null);
                Toast.makeText(getApplicationContext(),"The item Has beed removed",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UpdateActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}
