package com.example.pocketpetlayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class ProfileFix extends AppCompatActivity {

    EditText etext1;
    EditText etext2;
    EditText etext3;

    Bitmap bitmap;
    ImageView imageView;
    private static final int REQUEST_CODE = 0;
    private static final String TAG = "ProfileFix";

    //하단 버튼 없애기
    private View decorView;
    private int	uiOption;

    // POPUPMENU 부분
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        getMenuInflater().inflate(R.menu.toolbar_checkmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            case R.id.toolbar_check: // 체크 버튼을 통해 반려동물 프로필로 이동
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_fix);

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

        DBInfo();

        // 상단 툴바
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        // 이미지 클릭 하여 메뉴 보이기
        imageView = findViewById(R.id.img_user);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu pop = new PopupMenu(getApplicationContext(), view);
                getMenuInflater().inflate(R.menu.profile_menu, pop.getMenu());

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.one: //  카메라를 이용하여 프로필 변경
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                activityResultPicture.launch(intent);
                                break;
                            case R.id.two: // 갤러리를 이용하여 프로필 변경
                                Intent intent2 = new Intent();
                                intent2.setType("image/*");
                                intent2.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent2, REQUEST_CODE);
                                break;
                            case R.id.three: // 기본 이미지를 이용하여 프로필 변경
                                imageView.setImageResource(R.mipmap.ic_launcher);
                        }
                        return true;
                    }
                });
                pop.show();
            }
        });
    }
    // 카메라 사진
    ActivityResultLauncher<Intent> activityResultPicture = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null){
                        Bundle extras = result.getData().getExtras();

                        bitmap = (Bitmap) extras.get("data");

                        imageView.setImageBitmap(bitmap);
                    }
                }
            });

    @Override // 갤러리
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    imageView.setImageBitmap(img);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void DBInfo() {
        MyDbHelper dbHelper = new MyDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + Member.TABLE_NAME, null);

        if (c.moveToFirst()) {
            String member_name = c.getString(2);
            String sex = c.getString(3);
            String birthday = c.getString(4);

            Log.i(TAG, "name :" + member_name);
            etext1 = findViewById(R.id.profileFixEditText1);
            etext2 = findViewById(R.id.profileFixEditText2);
            etext3 = findViewById(R.id.profileFixEditText3);
            etext1.setText(member_name);
            etext2.setText(sex);
            etext3.setText(birthday);
        }
        c.close();
        db.close();
    }
}