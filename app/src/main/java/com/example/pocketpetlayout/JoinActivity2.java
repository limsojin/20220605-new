package com.example.pocketpetlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JoinActivity2 extends AppCompatActivity {

    String emailSt;
    String passwordSt2;
    String passwordSt3;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join2);

        MyDbHelper myDbHelper = new MyDbHelper(getApplicationContext());
        database = myDbHelper.getWritableDatabase();

        Button nextbtn = (Button) findViewById(R.id.nextbtn3);
        EditText email1 = (EditText) findViewById(R.id.email1);
        EditText password2 = (EditText) findViewById(R.id.password2);
        EditText password3 = (EditText) findViewById(R.id.password3);

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity3.class);

                emailSt= email1.getText().toString();
                passwordSt2 = password2.getText().toString();
                passwordSt3 = password2.getText().toString();

                if(emailSt.length() == 0 || passwordSt2.length() == 0){
                    Toast toast = Toast.makeText(JoinActivity2.this, "아이디와 비밀번호는 필수 입력사항입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                if (password2 == password3){
                    Toast toast = Toast.makeText(JoinActivity2.this, "비밀번호 재입력이 틀렸습니다", Toast.LENGTH_SHORT);
                }

                intent.putExtra("MEMBER_ID",emailSt );
                intent.putExtra("PASSWORD",passwordSt2);
                intent.putExtra("PASSWORDST",passwordSt3);
                startActivity(intent);
            }
        });
    }

}