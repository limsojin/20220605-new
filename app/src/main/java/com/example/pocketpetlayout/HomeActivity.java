package com.example.pocketpetlayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public class HomeActivity<ResultProfileBinding> extends AppCompatActivity {
    private static final String TAG = "Home_Activity";


    //하단 버튼 없애기
    private View decorView;
    private int	uiOption;

    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    private ResultProfileBinding binding;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment;
    private BoardFragment boardFragment;
    private FeedFragment feedFragment;
    private CamFragment camFragment;
    private ProfileFragment profileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final String TAG = "MainActivity";

        homeFragment = new HomeFragment();
        boardFragment = new BoardFragment();
        feedFragment = new FeedFragment();
        camFragment = new CamFragment();
        profileFragment = new ProfileFragment();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, homeFragment).commitAllowingStateLoss();


        //하단 버튼을 없애는 기능
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility( uiOption );
        //---------------------

        // bottom navigation View
        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.item_home:
                                Log.d(TAG, "onNavigationItemSelected: home button clicked");
                                fragmentManager.beginTransaction().replace(R.id.fragment_frame, homeFragment).commit();
                                return true;
                            case R.id.item_board:
                                Log.d(TAG, "onNavigationItemSelected: about button clicked");
                                fragmentManager.beginTransaction().replace(R.id.fragment_frame, boardFragment).commit();
                                return true;
                           case R.id.item_feed:
                                Log.d(TAG, "onNavigationItemSelected: profile button clicked");
                                fragmentManager.beginTransaction().replace(R.id.fragment_frame, feedFragment).commit();
                                return true;
                            case R.id.item_bookmark:
                                Log.d(TAG, "onNavigationItemSelected: profile button clicked");
                                fragmentManager.beginTransaction().replace(R.id.fragment_frame, camFragment).commit();
                                return true;
                            case R.id.item_profile:
                                Log.d(TAG, "onNavigationItemSelected: profile button clicked");
                                fragmentManager.beginTransaction().replace(R.id.fragment_frame, profileFragment).commit();
                                return true;
                            default:
                                return false;
                        }
                    }
                }
        );
    }
}
