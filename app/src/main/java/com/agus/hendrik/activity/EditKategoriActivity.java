package com.agus.hendrik.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agus.hendrik.model.Barang;
import com.agus.hendrik.myapp.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditKategoriActivity extends AppCompatActivity {

    private String[] kkategori = {
            "",
            "Old",
            "Best"};

    private String[] arr = {
            "Pcs",
            "Box",
            "Pasang",
            "Lusin"};

    String kat,ss;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kategori);
        final EditText nama, code_barang, merk, satuan, keterangan, ukuran, harga;
        final TextView no;
        final CircleImageView foto;

        nama =  findViewById(R.id.et_namaBarang);
        no =  findViewById(R.id.et_no);
        code_barang =  findViewById(R.id.et_codeBarang);
        merk =  findViewById(R.id.et_merkBarang);
        satuan =  findViewById(R.id.et_satuanBarang);
        keterangan =  findViewById(R.id.et_keterangan);
        ukuran =  findViewById(R.id.et_ukuranBarang);
        harga = findViewById(R.id.et_harga);
        foto = findViewById(R.id.fotoBarang);

        final Bundle bun = this.getIntent().getExtras();
        progressDialog = new ProgressDialog(this);

        if (bun != null) {
            nama.setText(bun.getString("nama"));
            no.setText(String.valueOf(bun.getInt("no")));
            code_barang.setText(bun.getString("code_barang"));
            satuan.setText(bun.getString("satuan"));
            keterangan.setText(bun.getString("keterangan"));
            ukuran.setText(bun.getString("ukuran"));

            String Ssatuan = bun.getString("satuan");
            int i = Ssatuan.indexOf(" ");
             String ukus = Ssatuan.substring(0,i);
            satuan.setText(ukus);

            final Spinner spinner = findViewById(R.id.satuanSpiner);
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_checked, arr);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    ss = spinner.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            merk.setText(bun.getString("merk"));
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Barang")
                    .child(no.getText().toString()).child("harga");

            try {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String a = String.valueOf(dataSnapshot.getValue());
                        harga.setText(a);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        harga.setText("");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditKategoriActivity.this);
                    @SuppressLint("InflateParams")
                    View myView = LayoutInflater.from(EditKategoriActivity.this).inflate(R.layout.view_foto_barang, null);
                    dialogBuilder.setView(myView);

                    String foto = bun.getString("foto");
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

        final Spinner spin = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_checked, kkategori);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kat = spin.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                kat="";
            }
        });

        ImageView ivSave = findViewById(R.id.iv_save);
        ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Saving Data");
                progressDialog.show();

                String final_nama = nama.getText().toString();
                int final_no = Integer.parseInt(no.getText().toString());
                String final_code_barang = code_barang.getText().toString();
                String final_merk = merk.getText().toString();
                String final_satuan = satuan.getText().toString()+" "+ss;
                String final_keterangan = keterangan.getText().toString();
                String final_ukuran =  ukuran.getText().toString();
                assert bun != null;
                String final_foto = bun.getString("foto");
                double final_harga = bun.getDouble("harga");

                Barang upload = new Barang(final_nama, final_no, final_code_barang,
                        final_foto, final_merk, final_satuan, final_keterangan, final_ukuran,
                        kat, final_harga, "Tersedia");
                FirebaseDatabase.getInstance().getReference("Barang")
                        .child(String.valueOf(final_no)).setValue(upload);
                progressDialog.dismiss();
                Toast.makeText(EditKategoriActivity.this,"" +
                        "Edit Product successfully...",Toast.LENGTH_SHORT).show();
                kembali();
            }
        });

    }
    private void kembali() {
        Intent back = new Intent();
        setResult(RESULT_OK, back);
        finish();
    }


}
