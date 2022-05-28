package com.example.pocketpetlayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PetProfileAddActivity extends AppCompatActivity {

    //하단 버튼 없애기
    private View decorView;
    private int	uiOption;

    String petname;
    String petbirthday;
    String petgender;

    TextView petTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile_add);

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

        //상단 툴바
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        petTxt = findViewById(R.id.PetTextView2);

        petname = getIntent().getStringExtra("petname");
        int a = getIntent().getIntExtra("petbirthday",0);
        petbirthday = String.valueOf(a);
        petgender = getIntent().getStringExtra("petgender");

        petTxt.setText(petname);

    }

    //체크 메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_checkmenu, menu);
        return true;
    }
    @Override // 각 메뉴 클릭 이벤트
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            case R.id.toolbar_check: // 체크 버튼을 통해 반려동물 프로필로 이동
                Intent intent = new Intent(getApplicationContext(), PetProfileActivity2.class);
                intent.putExtra("petname", petname);
                intent.putExtra("petbirthday", petbirthday);
                intent.putExtra("petgender", petgender);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
