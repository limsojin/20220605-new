package com.example.pocketpetlayout;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ProfileAdapter extends BaseAdapter {
    private int displayWidth; //화면크기
    private int size; //이미지 크기
    private int pad; //패딩

    private Context mContext;
    //출력될 이미지 데이터셋(res/drawable 폴더)
    private Integer[] mThumbIds = {
            R.drawable.pro01,
            R.drawable.pro02,
            R.drawable.pro03,
            R.drawable.pro04,
            R.drawable.pro05,
            R.drawable.pro06,
            R.drawable.pro07,
            R.drawable.pro08,
            R.drawable.pro09,
            R.drawable.pro12,
            R.drawable.pro11,
            R.drawable.pro16,
            R.drawable.pro13,
            R.drawable.pro14,
            R.drawable.pro15,
    };

    public ProfileAdapter(Context c,int displayWidth){
        mContext = c;
        //추가... 넘오온 가로크기를 저장.
        this.displayWidth = displayWidth;
        size = displayWidth/3 ;  //화면크기를 / 3으로 나누어서 이미지 사이즈를 구한다.
        pad = 8;
        System.out.println("size="+size);
    }


    @Override
    public int getCount() {
        //이미지셋에 있는 아이템의 수를 반환함(그리드뷰는 아이템의 수에 해당하는 행렬을 준비함)
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //주어진 위치(position)에 출력할 이미지를 반환함
        ImageView imageView;
        if(convertView == null){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(size, size)); //85,85
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(pad, pad, pad, pad); //8,8,8,8

        }else{
            imageView = (ImageView) convertView;
        }
        //이미지뷰에 주어진 위치의 이미지를 설정함
        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

}