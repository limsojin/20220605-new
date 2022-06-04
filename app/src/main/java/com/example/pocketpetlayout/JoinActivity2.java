package com.example.pocketpetlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JoinActivity2 extends AppCompatActivity {

    private static final String TAG = ".JoinActivity2";
    String emailSt;
    String passwordSt2;
    String passwordSt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join2);

        MyDbHelper myDbHelper = new MyDbHelper(getApplicationContext());

        Button overlap = (Button) findViewById(R.id.overlap);
        Button okbtn = (Button) findViewById(R.id.okbtn);
        Button nextbtn = (Button) findViewById(R.id.nextbtn);
        EditText email1 = (EditText) findViewById(R.id.email1);
        EditText password2 = (EditText) findViewById(R.id.password2);
        EditText password3 = (EditText) findViewById(R.id.password3);


        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity3.class);

                emailSt= email1.getText().toString();
                passwordSt2 = password2.getText().toString();
                passwordSt3 = password3.getText().toString();
                if(emailSt.length() == 0 || passwordSt2.length() == 0){
                    Toast toast = Toast.makeText(JoinActivity2.this, "아이디와 비밀번호는 필수 입력사항입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                intent.putExtra("MEMBER_ID",emailSt);
                intent.putExtra("PASSWORD",passwordSt2);
                //intent.putExtra("PASSWORDST",passwordSt3);
                startActivity(intent);
            }
        });

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailSt= email1.getText().toString();
                passwordSt2 = password2.getText().toString();
                passwordSt3 = password3.getText().toString();

                if (!(passwordSt2.equals(passwordSt3))){
                    Toast toast = Toast.makeText(JoinActivity2.this, "비밀번호 재입력이 틀렸습니다", Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    Toast toast = Toast.makeText(JoinActivity2.this, "비밀번호가 일치합니다", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        overlap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailSt= email1.getText().toString();
                passwordSt2 = password2.getText().toString();
                passwordSt3 = password3.getText().toString();
                SQLiteDatabase db = myDbHelper.getReadableDatabase();
                Cursor c =db.rawQuery("SELECT * FROM " + Member.TABLE_NAME, null);
                if(c.moveToFirst()){
                    do{
                        String email = c.getString(0);
                        Log.i(TAG,"email: " + email);

                        if(email.equals(emailSt)){
                            Log.i(TAG, "emailSt: " + emailSt);
                            Toast toast = Toast.makeText(JoinActivity2.this, "이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT);
                            toast.show();
                        }else if(!(email.equals(emailSt))){
                            Log.i(TAG, "emailSt: " + emailSt);
                            Toast toast = Toast.makeText(JoinActivity2.this, "사용 가능한 이메일입니다.", Toast.LENGTH_SHORT);
                            toast.show();
                        }else if(email.length()==0){
                            Log.i(TAG, "email: " + email);
                            Toast toast = Toast.makeText(JoinActivity2.this, "사용 가능한 이메일입니다.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }while(c.moveToNext());
                }else{
                    Toast toast = Toast.makeText(JoinActivity2.this, "사용 가능한 이메일입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
    }
}