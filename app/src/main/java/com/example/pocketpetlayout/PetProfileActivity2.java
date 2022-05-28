package com.example.pocketpetlayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class PetProfileActivity2 extends AppCompatActivity {

    private static final String TAG = "PetProfile2";
    TextView text1;
    TextView text2;
    TextView text3;
    ImageView imageView;

    //하단 버튼 없애기
    private View decorView;
    private int	uiOption;

    String petname;
    String petbirthday;
    String petgender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile2);



        //하단 버튼을 없애는 기능
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOption);
        //---------------------



        petname = getIntent().getStringExtra("petname");
        petbirthday = getIntent().getStringExtra("petbirthday");
        petgender = getIntent().getStringExtra("petgender");


        text1 = findViewById(R.id.PetProfileText2);
        text2 = findViewById(R.id.PetProfileText4);
        text3 = findViewById(R.id.petProfileText6);
        text1.setText(petname);
        text2.setText(petbirthday);
        text3.setText(petgender);

        // 반려동물 프로필 변경 페이지 이동 버튼 이벤트
        Button button = findViewById(R.id.PetProfileButton1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), PetProfileFixActivity.class);
                startActivity(intent);
            }
        });

        // 반려동물 선택 페이지로 이동 버튼 이벤트
        Button button2 = findViewById(R.id.PetProfileButton2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), PetProfileCheckActivity.class);
                startActivity(intent);
            }
        });

        // 상단 툴바
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        imageView = findViewById(R.id.PetProfileImageView1);
        String imagePath = getIntent().getStringExtra("path");
        if (imagePath != null) { // 이미지 경로가 있을 경우
            Glide.with(this).load(imagePath).into(imageView);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void DBInfo() {
        MyDbHelper dbHelper = new MyDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + Pet.TABLE_NAME, null);

        if (c.moveToFirst()) {
            String pet_name = c.getString(0);
            String birthday = c.getString(1);
            String sex = c.getString(2);
            Log.i(TAG, "name :" + pet_name + "birthday :" + birthday + "sex :" + sex);

        }
        c.close();
        db.close();
    }

}