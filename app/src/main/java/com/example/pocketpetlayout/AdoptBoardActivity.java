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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AdoptBoardActivity extends AppCompatActivity {

    public final static String TAG = "AdoptBoardActivity";
    private Toolbar toolbar;

    //하단 버튼 없애기
    private View decorView;
    private int uiOption;

    Button searchBtn;
    EditText searchWord;

    //DB
    DBHelper dbHelper;
    //플로팅버튼
    FloatingActionButton fab;

    //게시글 리스트
    ArrayList<BoardItem> Adopt_BoardItems;

    ArrayList<BoardItem> Copy_Adopt_BoardItems;

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_adopt_board);

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

        //ListView에 QnA 게시글 나열하기위한 데이터 초기화
        InitializeQnABoardData();

        if(!Adopt_BoardItems.isEmpty()) {


            //Listview 지정
            ListView adoptListView = this.findViewById(R.id.adoptListView);

            // ListView Adpater 지정
            final BoardAdapter boardAdapter = new BoardAdapter(this, Adopt_BoardItems);

            // ListView의 어뎁터를 셋한다.
            adoptListView.setAdapter(boardAdapter);

            //ListView 내부 아이템이 클릭 되었을 경우?
            adoptListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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


            Copy_Adopt_BoardItems = new ArrayList<BoardItem>();
            Copy_Adopt_BoardItems.addAll(Adopt_BoardItems);

            searchBtn = findViewById(R.id.searchBtn);
            searchWord = findViewById(R.id.searchWord);

            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = searchWord.getText().toString();
                    SearchDb(text);
                    Log.i(TAG,"text: " +text);
                    // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
                    boardAdapter.notifyDataSetChanged();
                }
            });

        }

        // 플로팅 버튼
        fab = (FloatingActionButton)findViewById(R.id.fab);

        //플로팅 버튼
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), WriteActivity.class); //글쓰기 화면으로 연결
                startActivity(intent); //액티비티 열기
            }
        });
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
                break;
            default:
                return false;
        }
        return true;
    }
    //--------------

    //ListView에 넣읗 데이터 초기화 (내부저장소에서 가져온다)
    public void InitializeQnABoardData(){
        Log.i(TAG, "InitializeQnABoardData!!!");
        Adopt_BoardItems = new ArrayList<BoardItem>();

        // board 테이블에서  qna 카테고리인 튜플의 id, title, writer, reg_date, like_cnt, com_cnt 를 가져온다.

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " +
                Board.COLUMN_BOARD_ID + " ," +
                Board.COLUMN_TITLE + " ," +
                Board.COLUMN_WRITER + " ," +
                Board.COLUMN_REGISTER_DATE + " ," +
                Board.COLUMN_LIKE_CNT + " ," +
                Board.COLUMN_COMMENT_CNT + " FROM " +
                Board.TABLE_NAME + " WHERE " +
                Board.COLUMN_CATEGORY + " = 'Adopt'" +
                " ORDER BY " + Board.COLUMN_BOARD_ID + " DESC; ", null );


        if (c.moveToFirst()) {

            do{
                int id = c.getInt(0);
                String title = c.getString(1);
                String writer = c.getString(2);
                String regDate = c.getString(3);
                int heart = c.getInt(4);
                int com = c.getInt(5);
                Adopt_BoardItems.add(new BoardItem(id,title,writer,regDate,heart,com));
                Log.i(TAG, "READ id :" + id + "title :" + title + "writer :" + writer + "regDate" + regDate + "heart: " + heart + "com : " + com);
            }while (c.moveToNext());
        }
        c.close();
        db.close();
    }


    public void SearchDb(String charText){
        //등록된 데이터 리스트 지우고
        Adopt_BoardItems.clear();
        int id = 11;
        String title = "입양안내!!";
        String writer = "피카츄";
        String regDate = "2022-05-29";
        int heart = 3;
        int com = 5;

        int id2 = 12;
        String title2 = "햄스터 입양할 건데";
        String writer2 = "로켓단";
        String regDate2 = "2022-05-13";
        int heart2 = 1;
        int com2 = 2;

        int id3 = 13;
        String title3 = "입양 질문이요 ㅠㅠ";
        String writer3 = "난냐옹이다옹";
        String regDate3 = "2022-05-27";
        int heart3 = 10;
        int com3 = 2;

        Adopt_BoardItems.add(new BoardItem(id,title,writer,regDate,heart,com));
        Adopt_BoardItems.add(new BoardItem(id2,title2,writer2,regDate2,heart2,com2));
        Adopt_BoardItems.add(new BoardItem(id3,title3,writer3,regDate3,heart3,com3));
    }


}