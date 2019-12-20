package com.agus.hendrik.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agus.hendrik.activity.DetailBarangActivity;
import com.agus.hendrik.model.Barang;
import com.agus.hendrik.myapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Barang> barangs;

    public BarangAdapter(Context c , ArrayList<Barang> p){
        context = c;
        barangs = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_barang,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.nama.setText(barangs.get(position).getNama());
        holder.merk.setText(barangs.get(position).getMerk());
        holder.ukuran.setText(barangs.get(position).getUkuran());
        Picasso.get().load(barangs.get(position).getFoto()).into(holder.foto);
        holder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView close;
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                @SuppressLint("InflateParams")
                View myView = LayoutInflater.from(context).inflate(R.layout.view_foto_barang, null);
                dialogBuilder.setView(myView);

                String foto = barangs.get(holder.getAdapterPosition()).getFoto();
                ImageView imageViewFoto = myView.findViewById(R.id.ivFoto);
                Picasso.get().load(foto).into(imageViewFoto);

                final AlertDialog b = dialogBuilder.create();
                b.setCanceledOnTouchOutside(true);
                b.setCancelable(false);
                b.show();
                close = myView.findViewById(R.id.btn_close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        b.dismiss();
                    }
                });
            }
        });
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = barangs.get(holder.getAdapterPosition()).getNama();
                int no = barangs.get(holder.getAdapterPosition()).getNo();
                String merk = barangs.get(holder.getAdapterPosition()).getMerk();
                String code_barang = barangs.get(holder.getAdapterPosition()).getCode_barang();
                String satuan = barangs.get(holder.getAdapterPosition()).getSatuan();
                String keterangan = barangs.get(holder.getAdapterPosition()).getKeterangan();
                String ukuran = barangs.get(holder.getAdapterPosition()).getUkuran();
                String foto = barangs.get(holder.getAdapterPosition()).getFoto();
                String kategori = barangs.get(holder.getAdapterPosition()).getKategori();
                double harga = barangs.get(holder.getAdapterPosition()).getHarga();

                Bundle bun= new Bundle();
                Intent intent= new Intent(holder.itemView.getContext(), DetailBarangActivity.class);
                bun.putString("nama",nama);
                bun.putInt("no",no);
                bun.putString("merk",merk);
                bun.putString("code_barang", code_barang);
                bun.putString("satuan", satuan);
                bun.putString("keterangan",keterangan);
                bun.putString("kategori", kategori);
                bun.putString("ukuran", ukuran);
                bun.putString("foto", foto);
                bun.putDouble("harga", harga);
                intent.putExtras(bun);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return barangs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama,  merk, ukuran;
        Button btn;
        CircleImageView foto;

        private MyViewHolder(View itemView) {
            super(itemView);
            nama =  itemView.findViewById(R.id.namaBarang);
            merk =  itemView.findViewById(R.id.merkBarang);
            ukuran =  itemView.findViewById(R.id.ukuranBarang);
            foto = itemView.findViewById(R.id.fotoBarang);
            btn =  itemView.findViewById(R.id.btn_open);
        }
    }
}