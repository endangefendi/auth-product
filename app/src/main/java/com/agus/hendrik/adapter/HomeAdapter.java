package com.agus.hendrik.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.agus.hendrik.model.Home;
import com.agus.hendrik.myapp.R;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends BaseAdapter {

    private Context context;
    private int status;
    private List<Home> list;
    private     OnItemClickListener listener;

    public HomeAdapter(Context context, int status, OnItemClickListener listener) {
        this.context = context;
        this.status = status;
        this.list = createList(status);
        this.listener = listener;
    }

    private List<Home> createList(int status){
        List<Home> homes = new ArrayList<>();
        homes.add(new Home(R.drawable.ic_barang, "Product", status));
        homes.add(new Home(R.drawable.ic_scan_dongker, "Scan", status));
        homes.add(new Home(R.drawable.ic_tentang, "About App", status));
        homes.add(new Home(R.mipmap.ic_logo_bvj, "Profile BVJ", status));

        return homes;
    }

    @Override
    public int getCount() {
        if (list == null)return 0;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list == null)return null;
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.item_gridview, parent, false);
        final Home item = list.get(position);
        ImageView imgView = (ImageView)view.findViewById(R.id.iv_icon);
        TextView txtTitle = (TextView) view.findViewById(R.id.tv_title);
        imgView.setImageResource(item.image);
        txtTitle.setText(item.title);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) listener.onItemClicked(position, item);
            }
        });
        return view;
    }

    public void setList(List<Home> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


    public interface OnItemClickListener{
        void onItemClicked(int position, Home item);
    }
}
