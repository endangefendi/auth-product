package com.agus.hendrik.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agus.hendrik.activity.DetailBarangActivity;
import com.agus.hendrik.activity.EditKategoriActivity;
import com.agus.hendrik.model.Barang;
import com.agus.hendrik.myapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BarangAdapterAdmin extends RecyclerView.Adapter<BarangAdapterAdmin.MyViewHolder> {
    private String nama ;
    private int no;
    private String merk;
    private String code_barang ;
    private String satuan;
    private String keterangan;
    private String ukuran ;
    private String foto ;
    private String kategori;
    private double harga;
    private ProgressDialog progressDialog;


    private Context context;
    private ArrayList<Barang> barangs;

    public BarangAdapterAdmin(Context c , ArrayList<Barang> p){
        context = c;
        barangs = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_barang_admin,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.tvnama.setText(barangs.get(position).getNama());
        holder.tvmerk.setText(barangs.get(position).getMerk());
        holder.tvukuran.setText(barangs.get(position).getUkuran());

        Glide.with(context).load(barangs.get(position).getFoto())
                .placeholder(R.drawable.ic_profil)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .thumbnail(0.5f)
                .into(holder.imfoto);

        holder.imfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView close;
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                @SuppressLint("InflateParams")
                View myView = LayoutInflater.from(context).inflate(R.layout.view_foto_barang, null);
                dialogBuilder.setView(myView);

                String foto = barangs.get(holder.getAdapterPosition()).getFoto();
                ImageView imageViewFoto = myView.findViewById(R.id.ivFoto);
                Glide.with(context).load(foto)
                        .placeholder(R.drawable.ic_profil)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .thumbnail(0.5f)
                        .into(imageViewFoto);

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
        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDetail(holder);
            }
        });
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoEditKategori(holder);
            }
        });

        holder.btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                konfirDelete(holder);
                }
        });
    }

    private void konfirDelete(MyViewHolder holder) {
        final int no = barangs.get(holder.getAdapterPosition()).getNo();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Yakin ingin menghapus barang?")
                .setMessage("Tekan (Yes) untuk melanjutkan")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        progressDialog = ProgressDialog.show(context, "Please wait...",
                                "Processing...", true);
                        progressDialog.show();
                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Barang")
                                .child(String.valueOf(no));
                        try {
                            ref.child("status").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ref.child("status").setValue("Tidak Tersedia");
                                    ref.child("kategori").setValue("");
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    ref.setValue("Tersedia");
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public int getItemCount() {
        return barangs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvnama, tvmerk, tvukuran;
        ImageView btn_detail,btn_edit,btn_hapus;
        CircleImageView imfoto;

        private MyViewHolder(View itemView) {
            super(itemView);
            tvnama =  itemView.findViewById(R.id.namaBarang);
            tvmerk =  itemView.findViewById(R.id.merkBarang);
            tvukuran =  itemView.findViewById(R.id.ukuranBarang);
            imfoto = itemView.findViewById(R.id.fotoBarang);
            btn_edit = itemView.findViewById(R.id.btn_kat);
            btn_detail=  itemView.findViewById(R.id.btn_open);
            btn_hapus =  itemView.findViewById(R.id.btn_hapus);
        }
    }

    private void gotoEditKategori(MyViewHolder holder) {
        nama = barangs.get(holder.getAdapterPosition()).getNama();
        no = barangs.get(holder.getAdapterPosition()).getNo();
        merk = barangs.get(holder.getAdapterPosition()).getMerk();
        code_barang = barangs.get(holder.getAdapterPosition()).getCode_barang();
        satuan = barangs.get(holder.getAdapterPosition()).getSatuan();
        keterangan = barangs.get(holder.getAdapterPosition()).getKeterangan();
        ukuran = barangs.get(holder.getAdapterPosition()).getUkuran();
        foto = barangs.get(holder.getAdapterPosition()).getFoto();
        kategori = barangs.get(holder.getAdapterPosition()).getKategori();
        harga = barangs.get(holder.getAdapterPosition()).getHarga();

        Bundle bun= new Bundle();
        Intent intent= new Intent(holder.itemView.getContext(), EditKategoriActivity.class);
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

    private void gotoDetail(MyViewHolder holder) {
        nama = barangs.get(holder.getAdapterPosition()).getNama();
        no = barangs.get(holder.getAdapterPosition()).getNo();
        merk = barangs.get(holder.getAdapterPosition()).getMerk();
        code_barang = barangs.get(holder.getAdapterPosition()).getCode_barang();
        satuan = barangs.get(holder.getAdapterPosition()).getSatuan();
        keterangan = barangs.get(holder.getAdapterPosition()).getKeterangan();
        ukuran = barangs.get(holder.getAdapterPosition()).getUkuran();
        foto = barangs.get(holder.getAdapterPosition()).getFoto();
        kategori = barangs.get(holder.getAdapterPosition()).getKategori();
        harga = barangs.get(holder.getAdapterPosition()).getHarga();

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
}