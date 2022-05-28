package com.example.pocketpetlayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


//*****________ 주의 !!!! ________*****
// 홈 프레그 먼트 화면에서 보여주는 피드리사이클러 입니다!!!!!!
// 피드 프래그먼트에서 사용하는거 아님!!!!!!!!!!!!!
public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.ViewHolder> {
    Context context;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView feed_image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            feed_image = (ImageView) itemView.findViewById(R.id.Feed_image);
        }
    }

    private ArrayList<FeedItem> mFeedList = null;

    public HomeFeedAdapter(ArrayList<FeedItem> mFeedList) {
        this.mFeedList = mFeedList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_feed_item, parent, false);
        HomeFeedAdapter.ViewHolder vh = new HomeFeedAdapter.ViewHolder(view);
        return vh;
    }

    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull HomeFeedAdapter.ViewHolder holder, int position) {
        FeedItem item = mFeedList.get(position);

        String path = context.getCacheDir() + "/" +item.getImgName();
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Bitmap bmp = Bitmap.createScaledBitmap(bitmap, 500, 500, false);

        holder.feed_image.setImageBitmap(bmp); // 사진 없어서 기본 파일로 이미지 띄움
    }

    @Override
    public int getItemCount() {
        if(mFeedList.size() >= 5) {
            return 5;
        }
        else{
            return mFeedList.size();

        }
    }
}