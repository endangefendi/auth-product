package com.agus.hendrik.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agus.hendrik.model.Barang;
import com.agus.hendrik.model.Home;
import com.agus.hendrik.model.KategoriModel;
import com.agus.hendrik.myapp.R;
import com.agus.hendrik.activity.BarangByIdActivity;

import java.util.ArrayList;
import java.util.List;

public class kategoriAdapter extends BaseAdapter {

    private Context context;
    private List<KategoriModel> barangs;
    private OnItemClickListener listener;

    public kategoriAdapter(Context c , OnItemClickListener listener){
        this.context = c;
        this.barangs = createList();
        this.listener = listener;
    }
    private List<KategoriModel> createList(){
        List<KategoriModel> homes = new ArrayList<>();
        homes.add(new KategoriModel("Oli"));
        homes.add(new KategoriModel("Body"));
        homes.add(new KategoriModel("Shockbreaker"));
        homes.add(new KategoriModel("Ban"));
        homes.add(new KategoriModel("Velg"));
        return homes;
    }

    @Override
    public int getCount() {
        if (barangs == null)return 0;
        return barangs.size();
    }

    @Override
    public Object getItem(int position) {
        if (barangs == null)return null;
        return barangs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.list_kategori, parent, false);
        final KategoriModel item = barangs.get(position);
        TextView nama = (TextView) view.findViewById(R.id.et_namakategori);
        nama.setText(item.title);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BarangByIdActivity.class);
                intent.putExtra("id_kategori",barangs.get(position).title);
                context.startActivity(intent);
            }
        });

        return view;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama;
        RelativeLayout more;

        private MyViewHolder(View itemView) {
            super(itemView);
            nama =  itemView.findViewById(R.id.et_namakategori);
            more = itemView.findViewById(R.id.more);
        }
    }

    public void addOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


    public interface OnItemClickListener{
        void onItemClicked(int position, KategoriModel item);
    }
}