package com.example.pocketpetlayout;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    Button ProfileFixBtn;
    Button CheckPetBtn;
    TextView text1;
    ImageView imageView;
    Intent intent;
    String imagePath;

    //하단 버튼 없애기
    private View decorView;
    private int	uiOption;

    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override  // Inflate the layout for this fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //하단 버튼을 없애는 기능
        decorView = getActivity().getWindow().getDecorView();
        uiOption = getActivity().getWindow().getDecorView().getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOption);
        //---------------------

        DBInfo(view);


        // 프로필 편집 , 반려동물 편집 버튼
        ProfileFixBtn = view.findViewById(R.id.profileButton1);
        ProfileFixBtn.setOnClickListener(this::onClick);
        CheckPetBtn = view.findViewById(R.id.profileButton2);
        CheckPetBtn.setOnClickListener(this::onClick);

        imageView = view.findViewById(R.id.profileImageView1);
        imagePath = getActivity().getIntent().getStringExtra("path");
        if (imagePath != null) { // 이미지 경로가 있을 경우
            Glide.with(this).load(imagePath).into(imageView);
        }


        /*
         * 그리드뷰에 이미지 띄우기
         */
        //화면크기 얻기
        Display display = ((WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int displayWidth = display.getWidth();
        int displayHeight = display.getHeight();

        GridView gridView = (GridView) view.findViewById(R.id.profileGrid1);
        ProfileAdapter adapter = new ProfileAdapter(getActivity(),displayWidth); //가로크기의 정보를 같이 넘긴다.
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), position+"클릭함", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    //버튼 클릭 이벤트
    public void onClick(View view){
        switch (view.getId()){
            case R.id.profileButton1:
                Intent intent = new Intent(getActivity().getApplicationContext(), ProfileFixActivity.class);
                startActivity(intent);
                break;
            case R.id.profileButton2:
                Intent intent2 = new Intent(getActivity().getApplicationContext(), PetProfileCheckActivity.class);
                startActivity(intent2);
                break;
        }
    }

    public void DBInfo(View view) {
        MyDbHelper dbHelper = new MyDbHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + Member.TABLE_NAME, null);

        if (c.moveToFirst()) {
            String member_name = c.getString(2);
            Log.i(TAG, "name :" + member_name);
            text1 = (TextView) view.findViewById(R.id.profileText7);
            text1.setText(member_name);
        }
        c.close();
        db.close();
    }
}