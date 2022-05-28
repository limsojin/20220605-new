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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MyBoardActivity extends AppCompatActivity {
    public final static String TAG = "MyBoardActivity";

    private Toolbar toolbar;
    //하단 버튼 없애기
    private View decorView;
    private int uiOption;

    //DB
    DBHelper dbHelper;
    MyDbHelper myDbHelper;

    //게시글 리스트
    ArrayList<BoardItem> My_BoardItems;

    //유저 이름!!!! 지정해줄 필요가 있습니다.
    String username;

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_my_board);


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

        //DBHelper
        dbHelper = new DBHelper(getApplicationContext());
        myDbHelper = new MyDbHelper(getApplicationContext());

        //ListView에 QnA 게시글 나열하기위한 데이터 초기화
        InitializeQnABoardData();

        if(!My_BoardItems.isEmpty()) {
            //Listview 지정
            ListView myListView = this.findViewById(R.id.myListView);

            // ListView Adpater 지정
            final BoardAdapter boardAdapter = new BoardAdapter(this, My_BoardItems);

            // ListView의 어뎁터를 셋한다.
            myListView.setAdapter(boardAdapter);

            //ListView 내부 아이템이 클릭 되었을 경우?
            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    int Id = boardAdapter.getItem(position).getId();

/*
                    Toast.makeText(getApplicationContext(),
                            "선택한 board의 boardID :" + Id,
                            Toast.LENGTH_LONG).show();
*/

                    Intent intent = new Intent(getApplicationContext(), BoardContentsActivity.class);
                    intent.putExtra("BoardId", Id); //게시글 아이디를 전송
                    startActivity(intent);


                }
            });
        }

    }

    //ToolBar에 toolbar_menu.xml 을 인플레이트
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_freeboardmenu, menu);
        return true;
    }
    //---------------

    //ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.toolbar_write:
                Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    //ListView에 넣읗 데이터 초기화 (내부저장소에서 가져온다)
    public void InitializeQnABoardData(){
        Log.i(TAG, "InitializeQnABoardData!!!");
        My_BoardItems = new ArrayList<BoardItem>();

        // board 테이블에서 kyj 유저가 writer인 튜플의 id, title, writer, reg_date, like_cnt, com_cnt 를 가져온다.
        SQLiteDatabase db2 = myDbHelper.getReadableDatabase();
        Cursor c1 = db2.rawQuery( "SELECT " +
                Member.NICKNAME + " , " +
                Member.MEMBER_ID + " FROM " + Member.TABLE_NAME + " ;" , null);

        if (c1.moveToFirst()) {

            do{
                username = c1.getString(0);
                Log.i(TAG, "글쓴이 : " + username);

            }while (c1.moveToNext());
        }
        c1.close();
        db2.close();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " +
                Board.COLUMN_BOARD_ID + " ," +
                Board.COLUMN_TITLE + " ," +
                Board.COLUMN_WRITER + " ," +
                Board.COLUMN_REGISTER_DATE + " ," +
                Board.COLUMN_LIKE_CNT + " ," +
                Board.COLUMN_COMMENT_CNT + " FROM " +
                Board.TABLE_NAME + " WHERE " +
                Board.COLUMN_WRITER + " == '" + username +
                "' ORDER BY " + Board.COLUMN_BOARD_ID + " DESC; ", null );


        if (c.moveToFirst()) {

            do{
                int id = c.getInt(0);
                String title = c.getString(1);
                String writer = c.getString(2);
                String regDate = c.getString(3);
                int heart = c.getInt(4);
                int com = c.getInt(5);
                My_BoardItems.add(new BoardItem(id,title,writer,regDate,heart,com));
                Log.i(TAG, "READ id :" + id + "title :" + title + "writer :" + writer + "regDate" + regDate + "heart: " + heart + "com : " + com);
            }while (c.moveToNext());
        }
        c.close();
        db.close();
    }
}