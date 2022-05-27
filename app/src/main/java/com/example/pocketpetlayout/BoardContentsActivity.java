package com.example.pocketpetlayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BoardContentsActivity extends AppCompatActivity {
    public final static String TAG = "BoardContentsActivity";

    private Toolbar toolbar;

    DBHelper dbHelper;

    int boardId;
    String imgName;
    String writer;
    ImageView imgView;
    TextView titleView;
    TextView nickname;
    TextView reg_dateView;
    TextView contentView;
    TextView heartView;
    TextView commView;
    TextView heartBtn;

    EditText comentText;
    Button sendComm;
    Button delBoard;
    Button upBoard;

    //하단 버튼 없애기
    private View decorView;
    private int	uiOption;

    int comm_cnt;
    int heart;

    ArrayList<CommentItem> commentItems;

    @Override
    protected void onResume() {
        setContentView(R.layout.activity_board_contents);
        super.onResume();
        setSupportActionBar(toolbar);

        //툴바만들기
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        //----------

        // 툴바에 홈버튼을 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //DB 가져오기
        dbHelper = new DBHelper(getApplicationContext());

        //boardId 받기
        boardId = getIntent().getIntExtra("BoardId", 0);
        Log.i(TAG, " boardId" + boardId);

        imgView = findViewById(R.id.imgView);
        titleView = findViewById(R.id.titleView);
        nickname = findViewById(R.id.nickname);
        reg_dateView = findViewById(R.id.reg_date);
        contentView = findViewById(R.id.contentView);
        heartView = findViewById(R.id.heart);
        commView = findViewById(R.id.comment);
        comentText = findViewById(R.id.commentText);
        sendComm = findViewById(R.id.sendComment);
        heartBtn = findViewById(R.id.heartBtn);
        delBoard = findViewById(R.id.delBtn);
        upBoard = findViewById(R.id.upBtn);

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

        //하트(좋아요) 버튼
        heartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean oK = dbHelper.updateBoardHeart(boardId, heart);

                if(oK) {
                    Log.i(TAG, "하트 수 업데이트 완료");
                }
                else{
                    Log.i(TAG, "하트 수 업데이트 실패");
                }

                //현재 화면 새로고침
                finish();//인텐트 종료
                overridePendingTransition(0, 0);//인텐트 효과 없애기
                Intent intent = getIntent(); //인텐트
                startActivity(intent); //액티비티 열기
                overridePendingTransition(0, 0);//인텐트 효과 없애기
            }
        });
        //------------------------------

        //댓글 작성
        sendComm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comentText.getText().toString() == null){
                    Toast.makeText(BoardContentsActivity.this, "댓글을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    String comment = comentText.getText().toString();
                    //현재 시간 가져오기
                    long now = System.currentTimeMillis();
                    java.sql.Date date;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    now = System.currentTimeMillis();
                    date = new Date(now);
                    String reg_date = sdf.format(date);

                    ContentValues comm = new ContentValues();

                    comm.put(Comment.COLUMN_BOARD_ID, boardId);
                    comm.put(Comment.COLUMN_CONTENT, comment);
                    comm.put(Comment.COLUMN_CREATE_DATE, reg_date);
                    comm.put(Comment.COLUMN_NICKNAME, "User");

                    Log.i(TAG, " 내용 : " + comment + " , 보드아이디 : " + boardId
                            + ", 등록 시간 : " + reg_date);

                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    long newRowId = db.insert(Comment.TABLE_NAME, null, comm);
                    Log.i(TAG, "new row id: " + newRowId);

                    boolean oK = dbHelper.updateBoardComment(boardId, comm_cnt);

                    if(oK){
                        Log.i(TAG, " 댓글 수 업데이트 성공;");
                    }
                    else{
                        Log.i(TAG, " 댓글 수 업데이트 실패");
                    }

                    //현재 화면 새로고침
                    finish();//인텐트 종료
                    overridePendingTransition(0, 0);//인텐트 효과 없애기
                    Intent intent = getIntent(); //인텐트
                    startActivity(intent); //액티비티 열기
                    overridePendingTransition(0, 0);//인텐트 효과 없애기

                }
            }
        });
        //----------------------

        // 글 삭제 버튼
        delBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                     boolean Ok =  dbHelper.deleteBoardToPK(boardId);

                     if(Ok){
                         Log.i(TAG, " 삭제 완료 ");
                         finish();
                     }
                     else{
                         Log.i(TAG, "삭제 실패");
                     }

            }
        });
        //-------------------

        //글 수정 버튼
        upBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String titleStr = titleView.getText().toString();
                String contentStr = contentView.getText().toString();

                Intent intent = new Intent(getApplicationContext(),BoardUpdateActivity.class);
                intent.putExtra("boardId", boardId);
                intent.putExtra("title", titleStr);
                intent.putExtra("content", contentStr);
                startActivity(intent);

            }
        });
        //-------------------


        //게시글 내용 가져옴
        loadContents();


        //ListView에 댓글을 나열하기위한 데이터 초기화
        InitializeQnABoardData();

        if(!commentItems.isEmpty()) { //댓글 리스트가 비어있으면 아래 실행하지 않음

            //Listview 지정
            ListView commListView = this.findViewById(R.id.comment_list);

            // ListView Adpater 지정
            final CommentAdapter commentAdapter = new CommentAdapter(this, commentItems);

            // ListView의 어뎁터를 셋한다.
            commListView.setAdapter(commentAdapter);

            //ListView 내부 아이템이 클릭 되었을 경우?
        /*    commListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Toast.makeText(getApplicationContext(),
                            "선택한 board의 boardID :" + Id,
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), BoardContentsActivity.class);
                    intent.putExtra("BoardId", Id); //게시글 아이디를 전송
                    startActivity(intent);
                }
            });*/
        }

    }
    //ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }
    //--------------

    // 게시글 콘텐츠를 불러온 것
    public void loadContents(){
        Log.i(TAG, "loagContents");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Board.TABLE_NAME +
                                    " WHERE " + Board.COLUMN_BOARD_ID + " = " + boardId + " ; ", null);
        if(c.moveToFirst()){
            do{
                String title = c.getString(1);
                writer = c.getString(2);
                imgName = c.getString(3);
                String contents = c.getString(4);
                comm_cnt = c.getInt(5);
                heart = c.getInt(6);
                String reg_date = c.getString(8);

                Log.i(TAG, "READ title :" + title + "writer :" + writer + "writer :" + writer + "imgName" + imgName + "contents: " + contents + "com : " + comm_cnt
                + "like : " + heart + " reg_date: " + reg_date );

                titleView.setText(title);
                nickname.setText(writer);
                contentView.setText(contents);
                commView.setText(String.valueOf(comm_cnt));
                heartView.setText(String.valueOf(heart));
                reg_dateView.setText(reg_date);

                String path = getCacheDir() + "/" + imgName;
                Bitmap bitmap = BitmapFactory.decodeFile(path);

                imgView.setImageBitmap(bitmap);

            }while(c.moveToNext());
        }
        c.close();
        db.close();
    }


    //ListView에 넣을 데이터 초기화 (내부저장소에서 가져온다) 댓글을 가져오는 것!
    public void InitializeQnABoardData(){
        Log.i(TAG, "InitializeQnABoardData!!!");
        commentItems = new ArrayList<CommentItem>();

        // board ID를 통해 댓글을 가져옴!!

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Comment.TABLE_NAME +
                " WHERE " + Comment.COLUMN_BOARD_ID + " = " + boardId + " ; ", null);


        if (c.moveToFirst()) {

            do{
                int id = c.getInt(0);
                String writer = c.getString(1);  //작성자
                String comment = c.getString(2); //댓글내용
                String regDate = c.getString(3); //작성일자

                commentItems.add(new CommentItem(writer,comment,regDate)); // ITEMS에 삽입

                Log.i(TAG, "READ id :" + id +  "writer :" + writer +  "comment" + comment +"regDate" + regDate);
            }while (c.moveToNext());
        }
        c.close();
        db.close();
    }


}