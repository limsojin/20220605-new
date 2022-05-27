package com.example.pocketpetlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BoardUpdateActivity extends AppCompatActivity {
    final static String TAG = "BoardUpdateActivity";

    //하단 버튼 없애기
    private View decorView;
    private int uiOption;

    //카테고리
    Spinner categorySpin;
    ArrayAdapter arrayAdapt;
    ArrayAdapter<CharSequence> categoryAdapt;

    // 등록, 수정, 삭제 버튼
    Button c_Btn;
    Button d_Btn;
    TextView getImgBtn;

    //사진 등록
    ImageView imgView;

    //
    int boardId;
    EditText title;
    String titleStr;
    EditText content;
    String contentStr;
    String category;
    String imgName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

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

        //등록 수정 삭제 버튼
        c_Btn = findViewById(R.id.c_button);
        d_Btn = findViewById(R.id.d_button);

        c_Btn.setText("수정");
        d_Btn.setText("취소");

        //
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);

        //
        boardId = getIntent().getIntExtra("boardId",0);

        String oldTitle = getIntent().getStringExtra("title");
        String oldContent = getIntent().getStringExtra("content");

        title.setText(oldTitle);
        content.setText(oldContent);


        //사진 등록
        getImgBtn = findViewById(R.id.getImgBtn);
        imgView = findViewById(R.id.imgView);

        //카테고리 설정
        categorySpin = findViewById(R.id.category_Spin);
        arrayAdapt = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item);
        categoryAdapt = ArrayAdapter.createFromResource(getBaseContext(), R.array.category,
                android.R.layout.simple_spinner_item);
        categoryAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpin.setAdapter(categoryAdapt);

        categorySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position){
                    case 0:{//입양 게시판 카테고리 선택 시
                        category = "Free";
                        break;
                    }
                    case 1:{ //질문 게시판 카테고리 선택 시
                        category = "QnA";
                        break;
                    }
                    case 2:{ //입양 게시판 카테고리 선택 시
                        category = "Adopt";
                        break;
                    }
                }
                Log.i(TAG, "선택된 카테고리 :" + category);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                category = "Free";  //기본 카테고리를 자유 게시판으로
            }
        });

        //사진 등록
        getImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 101);
            }
        });



        // 수정하기
        c_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //제목
                titleStr = title.getText().toString();
                //내용
                contentStr = content.getText().toString();

                boolean Ok = dbHelper.updateBoardContents(boardId, titleStr, imgName, category, contentStr);

                if(Ok){
                    Log.i(TAG, " 수정 완료 !");
                }else{
                    Log.i(TAG, " 수정 실패 ! ");
                }

                finish();
            }
        });


        // 수정 취소
        d_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //갤러리
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101){
            if(resultCode == RESULT_OK){
                Uri fileUri = data.getData();
                String uri = data.getDataString();
                imgName = uri.substring(61);
                Log.i(TAG, "파일 Uri : " + fileUri);
                ContentResolver resolver = getContentResolver();
                try{
                    //파일URI inputstream
                    InputStream instream = resolver.openInputStream(fileUri);
                    Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                    // 이미지 저장
                    imgView.setImageBitmap(imgBitmap);
                    instream.close();
                    //Bitmap 을 내부저장소에 저장
                    saveBitmapToJpeg(imgBitmap);
                    Toast.makeText(getApplicationContext(), "파일 불러오기 성공", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void saveBitmapToJpeg(Bitmap bitmap) { // 선택한 이미지 내부 저장소에 저장
        File tempFile = new File(getCacheDir(), imgName); // 파일경로와 이름 넣기
        try {
            tempFile.createNewFile(); //자동으로 빈 파일을 생성하기
            FileOutputStream out = new FileOutputStream(tempFile); // 파일을 쓸 수 있는 스트림을 준비하기
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); //compress 함수를 사용해 스트림에 비트맵을 저장
            out.close(); //스트림 닫아주기
            Toast.makeText(getApplicationContext(), "파일 저장 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 저장 실패", Toast.LENGTH_SHORT).show();
        }
    }

}