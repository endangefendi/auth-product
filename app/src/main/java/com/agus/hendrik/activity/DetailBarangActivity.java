package com.agus.hendrik.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.agus.hendrik.myapp.R;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailBarangActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang);

        TextView nama,no, code_barang, merk, satuan, keterangan, ukuran, harga;
        CircleImageView foto;

        nama =  findViewById(R.id.namaBarang);
        no =  findViewById(R.id.idBArang);
        code_barang =  findViewById(R.id.codeBarang);
        merk =  findViewById(R.id.merkBarang);
        satuan =  findViewById(R.id.satuanBarang);
        keterangan =  findViewById(R.id.tipeBarang);
        ukuran =  findViewById(R.id.ukuranBarang);
        foto = findViewById(R.id.fotoBarang);
        harga = findViewById(R.id.et_harga);

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        final Bundle bun = this.getIntent().getExtras();

        if (bun != null) {
            nama.setText(bun.getString("nama"));
            no.setText(String.valueOf(bun.getInt("no")));
            code_barang.setText(bun.getString("code_barang"));
            satuan.setText(bun.getString("satuan"));
            keterangan.setText(bun.getString("keterangan"));
            ukuran.setText(bun.getString("ukuran"));
            merk.setText(bun.getString("merk"));
            //harga.setText(String.valueOf(bun.getDouble("harga")));

            harga.setText(formatRupiah.format(bun.getDouble("harga")));

            Glide.with(this)
                    .load(bun.getString("foto"))
                    .into(foto);

            ImageView backButton = findViewById(R.id.iv_back);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            foto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView close;
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DetailBarangActivity.this);
                    @SuppressLint("InflateParams")
                    View myView = LayoutInflater.from(DetailBarangActivity.this).inflate(R.layout.view_foto_barang, null);
                    dialogBuilder.setView(myView);

                    String foto = bun.getString("foto");
                    ImageView imageViewFoto = myView.findViewById(R.id.ivFoto);
                    Picasso.get().load(foto).placeholder(R.drawable.ic_profil).into(imageViewFoto);

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
        }
        else {
            nama.setText("");
            no.setText("");
            code_barang.setText("");
            satuan.setText("");
            keterangan.setText("");
            ukuran.setText("");
            merk.setText("");
        }

    }


}
