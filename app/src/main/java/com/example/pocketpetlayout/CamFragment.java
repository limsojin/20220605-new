package com.example.pocketpetlayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CamFragment extends Fragment implements View.OnClickListener{
    Button webcam;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_cam, container, false);

        webcam =(Button) v.findViewById(R.id.webcam);
        webcam.setOnClickListener(this::onClick);
        return v;
    }

    @Override
    public void onClick(View v) {
        Intent bj=new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.45.57:8080/greet.html"));
        startActivity(bj);
    }
}