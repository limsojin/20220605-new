package com.example.pocketpetlayout;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class QnAAdapter extends RecyclerView.Adapter<QnAAdapter.ViewHolder> {

    final static String TAG = "QnAAdapter";
    Context context;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView qna_image;
        TextView txt_main;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            qna_image = (ImageView) itemView.findViewById(R.id.QnA_image);

            txt_main = (TextView) itemView.findViewById(R.id.txt_main);


        }
    }

    private ArrayList<QnAItem> mQnAList = null;

    public QnAAdapter(ArrayList<QnAItem> mList) {
        this.mQnAList = mList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_question_item, parent, false);
        QnAAdapter.ViewHolder vh = new QnAAdapter.ViewHolder(view);
        return vh;
    }

    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull QnAAdapter.ViewHolder holder, int position) {

        if(!mQnAList.isEmpty()) {
            QnAItem item = mQnAList.get(position);

            String path = context.getCacheDir() + "/" + item.getImgName();
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            Bitmap bmp = Bitmap.createScaledBitmap(bitmap, 500, 500, false);

            holder.qna_image.setImageBitmap(bmp); // raw 폴더에 저장된 이미지 띄우기
            holder.txt_main.setText(item.getMainText());
        }
        else{
            Log.i(TAG, " QNA 리스트 비어있음");
        }
    }

    @Override
    public int getItemCount() {
        if(mQnAList.size() >= 5) {
            return 5;
        }
        else{
            return mQnAList.size();
        }
    } //아이템 사이즈도 지정하기 최근 글 5개만 보여줌

}

