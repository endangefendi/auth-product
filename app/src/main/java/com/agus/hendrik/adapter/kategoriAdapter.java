package com.agus.hendrik.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agus.hendrik.model.Barang;
import com.agus.hendrik.myapp.R;
import com.agus.hendrik.activity.BarangByIdActivity;

import java.util.ArrayList;

public class kategoriAdapter extends RecyclerView.Adapter<kategoriAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Barang> barangs;

    public kategoriAdapter(Context c , ArrayList<Barang> p){
        context = c;
        barangs = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_kategori,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.nama.setText(barangs.get(position).getKategori());
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BarangByIdActivity.class);
                intent.putExtra("id_kategori",barangs.get(position).getKategori());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return barangs.size();
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
}