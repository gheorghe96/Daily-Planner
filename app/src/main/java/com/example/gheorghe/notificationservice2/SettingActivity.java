package com.example.gheorghe.notificationservice2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class SettingActivity extends AppCompatActivity {
    ImageButton imageButton_delete;
    SQLiteDatabase sqLiteDatabase;
    myHelper MyHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        imageButton_delete = findViewById(R.id.imageButton);

        MyHelper = new myHelper(getApplicationContext(),"database",null,1);
        sqLiteDatabase = MyHelper.getWritableDatabase();

        imageButton_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqLiteDatabase.delete("mytable",null,null);
                Toast.makeText(getApplicationContext(),"All elements has beed removed",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}
