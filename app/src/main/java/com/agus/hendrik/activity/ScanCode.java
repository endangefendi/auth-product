package com.agus.hendrik.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agus.hendrik.model.Barang;
import com.agus.hendrik.model.WaktuCek;
import com.agus.hendrik.myapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


import de.hdodenhof.circleimageview.CircleImageView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCode extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final String TAG = "Scan Code";
    private ZXingScannerView mQRScanner;
    String hasilScan;
    DatabaseReference storageBarang;
    private int idcek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
        mQRScanner = new ZXingScannerView(this);
        jumalahcek();
        ViewGroup contentFrame = findViewById(R.id.content_frame);
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        mQRScanner.setResultHandler(this);
        if (isCameraAllowed()) {
            contentFrame.addView(mQRScanner);
            mQRScanner.startCamera();
        }


    }

    private void mapbvj() {
        Uri navigationIntentUri = Uri.parse("google.navigation:q=" + -7.780904 + "," + 110.391409);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }


    @Override
    public void onResume() {
        super.onResume();
        mQRScanner.setResultHandler(this);
        mQRScanner.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mQRScanner.stopCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private boolean isCameraAllowed() {
        boolean flag = false;
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            flag = true;
        }
        return flag;
    }

    @Override
    public void handleResult(Result rawQRData) {
        String dataURL_QR = rawQRData.getText();

        Log.d(TAG, "Hasil scan: "+dataURL_QR );
        Log.d(TAG, rawQRData.getBarcodeFormat().toString());

        hasilScan = rawQRData.getText();
        storageBarang = FirebaseDatabase.getInstance().getReference();
        final Query queryNew =  FirebaseDatabase.getInstance().getReference()
                .child("Barang").orderByChild("code_barang").equalTo(hasilScan);
        queryNew.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Barang barangScan = null;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        barangScan = dataSnapshot1.getValue(Barang.class);
                    }
                    result_scan(barangScan);
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ScanCode.this);
                    builder.setCancelable(false);
                    builder.setTitle("Barang tidak terdata");
                    builder.setMessage("Pastikan barang yang anda beli asli dari BVJ");
                    builder.setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //if user select "No", just cancel this dialog and continue with app
                            dialog.cancel();
                            onResume();
                        }
                    });
                    AlertDialog alert1 = builder.create();
                    alert1.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void result_scan(final Barang barangScan) {
        mQRScanner.stopCamera();
        final String nama = barangScan.getNama();
        final String code_barang = barangScan.getCode_barang();
        final String foto = barangScan.getFoto();
        final int no = barangScan.getNo();
        final String merk = barangScan.getMerk();
        final String satuan = barangScan.getSatuan();
        final String keterangan = barangScan.getKeterangan();
        final String status = barangScan.getStatus();
        final String ukuran = barangScan.getUkuran();
        final String kategori = barangScan.getKategori();
        final double harga = barangScan.getHarga();
        add_waktu_cek(code_barang);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams")
        final View dialogView = inflater.inflate(R.layout.result_scan, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(true);
        b.setCancelable(false);
        b.show();

        TextView tvcode = dialogView.findViewById(R.id.et_code_barang);
        tvcode.setText(code_barang);

        TextView tvstatus = dialogView.findViewById(R.id.et_status);
        tvstatus.setText(status);


        CircleImageView ImageFoto = dialogView.findViewById(R.id.iv_foto_barang);

        Glide.with(ScanCode.this).load(foto)
                .placeholder(R.drawable.ic_profil)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .thumbnail(0.5f)
                .into(ImageFoto);

        ImageView close = dialogView.findViewById(R.id.btn_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
                onResume();
                mQRScanner.startCamera();
            }
        });

        ImageView nav = dialogView.findViewById(R.id.ic_navigasi);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapbvj();
            }
        });
        TextView detail = dialogView.findViewById(R.id.btn_detail);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bun = new Bundle();
                Intent intent = new Intent(ScanCode.this, DetailBarangActivity.class);
                bun.putString("nama", nama);
                bun.putInt("no", no);
                bun.putString("merk", merk);
                bun.putString("code_barang", code_barang);
                bun.putString("satuan", satuan);
                bun.putString("keterangan", keterangan);
                bun.putString("ukuran", ukuran);
                bun.putString("kategori", kategori);
                bun.putString("foto", foto);
                bun.putDouble("harga", harga);
                intent.putExtras(bun);
                startActivity(intent);
            }
        });

    }

    private void add_waktu_cek(String code_barang){
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.getDefault());
        String date = sdf.format(now.getTime());
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        String uid = currentUser.getUid();

        Log.d(TAG,"ADD WAKTU CEK : "+idcek+" - "+uid+" - "+code_barang+" - "+date);
        WaktuCek save = new WaktuCek(idcek,uid,code_barang,date);
        FirebaseDatabase.getInstance().getReference("Waktu_Cek")
                .child(String.valueOf(idcek)).setValue(save);
    }

    private void jumalahcek() {
        Query queryNew =  FirebaseDatabase.getInstance().getReference().child("Waktu_Cek").orderByChild("idcek");
        queryNew.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int size = (int) dataSnapshot.getChildrenCount();
                idcek = size + 1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ScanCode.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}