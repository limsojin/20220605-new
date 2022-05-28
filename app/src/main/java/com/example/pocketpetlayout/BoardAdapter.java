package com.example.pocketpetlayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class BoardAdapter extends BaseAdapter implements Filterable {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<BoardItem> boardItems;
    Filter listFilter;

    // 필터링된 결과 데이터를 저장하기 위한 ArrayList. 최초에는 전체 리스트 보유.
    ArrayList<BoardItem> filteredItemList = boardItems;

    public BoardAdapter(Context context, ArrayList<BoardItem> data){
        this.context = context;
        boardItems = data;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return boardItems.size();
    }

    @Override
    public BoardItem getItem(int position) {
        return boardItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View converview, ViewGroup viewGroup) {

        final int pos = position;
        final Context context = viewGroup.getContext();

        View view = layoutInflater.inflate(R.layout.activity_list_item, null);

        TextView title = view.findViewById(R.id.txt_title);
        TextView writer = view.findViewById(R.id.txt_writer);
        TextView reg_date = view.findViewById(R.id.txt_regDate);
        TextView heart = view.findViewById(R.id.txt_heart);
        TextView comment = view.findViewById(R.id.txt_comm);

        title.setText(boardItems.get(position).getTitle());
        writer.setText(boardItems.get(position).getWriter());
        reg_date.setText(boardItems.get(position).getReg_date());
        heart.setText(String.valueOf(boardItems.get(position).getHeart()));
        comment.setText(String.valueOf(boardItems.get(position).getComment()));

        return view;
    }
    @Override
    public Filter getFilter() {
        if (listFilter == null) {
            listFilter = new ListFilter();
        }
        return listFilter;
    }

    private class ListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = boardItems;
                results.count = boardItems.size();
            } else {
                ArrayList<BoardItem> itemList = new ArrayList<BoardItem>();

                for (BoardItem item : boardItems) {
                    if (item.getTitle().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        itemList.add(item);
                    }
                }
                results.values = itemList;
                results.count = itemList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItemList = (ArrayList<BoardItem>) results.values;

            // notify
            if (results.count > 0) {
                notifyDataSetChanged() ;
            } else {
                notifyDataSetInvalidated() ;
            }
        }
    }
}

