package com.example.pocketpetlayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.InputStream;

public class AddPetActivity extends AppCompatActivity {

    String petnickSt;
    String petgender;
    int petbirthDay;
    ImageView pro2;
    private static final int REQUEST_CODE = 0;
    Bitmap bitmap;

    public static final String TAG = " joinActivity4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        Button nextbtn3 = (Button) findViewById(R.id.addBtn);
        EditText petnickname = (EditText) findViewById(R.id.petnickname); // 닉네임
        Spinner spinner_year = (Spinner)findViewById(R.id.spinner_year); // 월 선택
        Spinner spinner_month = (Spinner)findViewById(R.id.spinner_month); // 년 선택
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup); // 성별 선택

        nextbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String spinnerYear = spinner_year.getSelectedItem().toString(); // 선택한 년도 spinnerYear에 넣음
                String spinnerMonth = spinner_month.getSelectedItem().toString(); // 선택한 월 spinnerMonth에 넣음
                petbirthDay = Integer.parseInt(spinnerYear + spinnerMonth); //생년월일 넣음
                petnickSt= petnickname.getText().toString(); // 닉네임 넣음
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId(); // 선택한 성별 넣음
                if (checkedRadioButtonId == -1) {
                    // No item selected
                }
                else{
                    if (checkedRadioButtonId == R.id.petwoman) {
                        petgender= "woman";
                    }else if(checkedRadioButtonId == R.id.petman){
                        petgender= "man";
                    }
                }


                Intent intent = new Intent(getApplicationContext(), PetProfileAddActivity.class);
                intent.putExtra("petname", petnickSt);
                intent.putExtra("petbirthday", petbirthDay);
                intent.putExtra("petgender", petgender);
                startActivity(intent);

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
                        RoundedBitmapDrawable bitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
                        bitmapDrawable.setCircular(true);
                        pro2.setImageDrawable(bitmapDrawable);
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

                    pro2.setImageBitmap(img);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }


}