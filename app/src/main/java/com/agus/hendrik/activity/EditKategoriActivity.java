package com.agus.hendrik.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agus.hendrik.model.Barang;
import com.agus.hendrik.myapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditKategoriActivity extends AppCompatActivity {

    private String[] kkategori = {
            "New",
            "Old",
            "Best"};

    private String[] tringSatuan = {
            "Pcs",
            "Box",
            "Pasang",
            "Lusin"};

    String kat,ss;
    private ProgressDialog progressDialog;
    private Spinner spinner_kategori,spinner_satuan;
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
        ImageView backButton = findViewById(R.id.iv_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Bundle bun = this.getIntent().getExtras();
        progressDialog = new ProgressDialog(this);

        if (bun != null) {
            nama.setText(bun.getString("nama"));
            no.setText(String.valueOf(bun.getInt("no")));
            code_barang.setText(bun.getString("code_barang"));
            satuan.setText(bun.getString("satuan"));
            keterangan.setText(bun.getString("keterangan"));
            ukuran.setText(bun.getString("ukuran"));
            final String kategori = bun.getString("kategori");

            String Ssatuan = bun.getString("satuan");
            int i = Ssatuan.indexOf(" ");
            String noSatuan = Ssatuan.substring(0,i);
            satuan.setText(noSatuan);
            final String sat = Ssatuan.substring(i,Ssatuan.length());

            spinner_kategori = findViewById(R.id.spinner_kategori);
            spinner_satuan = findViewById(R.id.spinner_satuan);
            final ArrayAdapter<String> adapterArr = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, tringSatuan);
            spinner_satuan.setAdapter(adapterArr);

            spinner_satuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    ss = spinner_satuan.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    switch (sat) {
                        case "Lusin":
                            spinner_satuan.setSelection(3);
                            break;
                        case "Box":
                            spinner_satuan.setSelection(1);
                            break;
                        case "Pasang":
                            spinner_satuan.setSelection(2);
                            break;
                        case "Pcs":
                            spinner_satuan.setSelection(0);
                            break;
                    }
                }
            });

            switch (sat) {
                case "Lusin":
                    spinner_satuan.setSelection(3);
                    break;
                case "Box":
                    spinner_satuan.setSelection(1);
                    break;
                case "Pasang":
                    spinner_satuan.setSelection(2);
                    break;
                case "Pcs":
                    spinner_satuan.setSelection(0);
                    break;
            }
            ArrayAdapter<String> adapterKategori = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, kkategori);
            spinner_kategori.setAdapter(adapterKategori);
            spinner_kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    kat = spinner_kategori.getSelectedItem().toString();
//                    Toast.makeText(EditKategoriActivity.this,"" +
//                            spin.getSelectedItemId(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    if (kategori != null) {
                        switch (kategori) {
                            case "Best":
                                spinner_kategori.setSelection(2);
                                break;
                            case "Old":
                                spinner_kategori.setSelection(1);
                                break;
                            case "New":
                                spinner_kategori.setSelection(0);
                                break;
                        }
                    }
                }
            });

            if (kategori != null) {
                switch (kategori) {
                    case "Best":
                        spinner_kategori.setSelection(2);
                        break;
                    case "Old":
                        spinner_kategori.setSelection(1);
                        break;
                    case "New":
                        spinner_kategori.setSelection(0);
                        break;
                }
            }
            merk.setText(bun.getString("merk"));
            double h = bun.getDouble("harga");
            harga.setText(String.valueOf(Math.round(h)));

            Glide.with(this)
                    .load(bun.getString("foto"))
                    .into(foto);

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

                    Glide.with(EditKategoriActivity.this).load(foto)
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
                double final_harga = Double.parseDouble(harga.getText().toString());

                Barang upload = new Barang(final_nama, final_no, final_foto, final_code_barang,
                        final_merk, final_satuan, final_keterangan, final_ukuran,
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
