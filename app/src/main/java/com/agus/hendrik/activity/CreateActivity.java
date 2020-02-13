package com.agus.hendrik.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agus.hendrik.model.Barang;
import com.agus.hendrik.myapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CreateActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ProgressDialog progressDialog;
    private DatabaseReference databaseBarang;

    private static final String TAG = "Create Activity";
    private ZXingScannerView mQRScanner;
    EditText nama, code_barang, merk, satuan, keterangan, ukuran, harga;
    TextView no;
    String strnama, strno, strcode_barang, strmerk, strsatuan, strketerangan, strukuran, strharga, strfoto;
    String code_scan, ss;
    ImageView imageView;
    ViewGroup contentFrame;
    Bitmap bitmap;
    LinearLayout framForm;

    private String[] Ssatuan = {
            "Pcs",
            "Box",
            "Pasang",
            "Lusin"};
    private Uri resultUri;


    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseBarang = FirebaseDatabase.getInstance().getReference("Barang");

        cekjumlahbarang();
        setContentView(R.layout.activity_create);
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mQRScanner = new ZXingScannerView(this);

        progressDialog = new ProgressDialog(this);

        framForm =findViewById(R.id.framForm);
        imageView =findViewById(R.id.image);
        nama =  findViewById(R.id.et_namaBarang);
        no =  findViewById(R.id.et_no);
        code_barang =  findViewById(R.id.et_codeBarang);
        merk =  findViewById(R.id.et_merkBarang);
        satuan =  findViewById(R.id.et_satuanBarang);
        keterangan =  findViewById(R.id.et_keterangan);
        ukuran =  findViewById(R.id.et_ukuranBarang);
        harga = findViewById(R.id.et_harga);

        final Spinner spinner = findViewById(R.id.satuanSpiner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_checked, Ssatuan);
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

        ImageView ivSave = findViewById(R.id.iv_save);
        ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekInputan();
            }
        });

        ImageView ivClear = findViewById(R.id.iv_clear);
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama.setText("");
                no.setText("");
                code_barang.setText("");
                merk.setText("");
                satuan.setText("");
                keterangan.setText("");
                ukuran.setText("");
                harga.setText("");
                onResume();
                contentFrame.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
            }
        });

        contentFrame = findViewById(R.id.framFoto);
        mQRScanner.setResultHandler(this);
        if (isCameraAllowed()) {
            contentFrame.addView(mQRScanner);
            mQRScanner.startCamera();
        }
        Button choose_photo = findViewById(R.id.bt_foto);
        choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                }
                break;

            case REQUEST_CODE_GALLERY:
                if (resultCode == RESULT_OK) {
                    resultUri = data.getData();
                    imageView.setImageURI(resultUri);
                }
                break;
        }

    }

    private void getImage(){
        //Method ini digunakan untuk mengambil gambar dari Kamera
        CharSequence[] menu = {"Kamera", "Galeri"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Upload Image")
                .setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                //Mengambil gambar dari Kemara ponsel
                                Intent imageIntentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(imageIntentCamera, REQUEST_CODE_CAMERA);
                                break;

                            case 1:
                                //Mengambil gambar dari galeri
                                Intent imageIntentGallery = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(imageIntentGallery, REQUEST_CODE_GALLERY);
                                break;
                        }
                    }
                });
        dialog.create();
        dialog.show();
    }

    private String getFileExtension(Uri filePath) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(filePath));
    }

    private void saving(){
        if (resultUri != null || bitmap != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Saving Data");
            progressDialog.show();
            final StorageReference storageReferences = storageReference.child("foto/"+ System.currentTimeMillis()+ "." +
                    getFileExtension(resultUri));
            storageReferences.putFile(resultUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReferences.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    strfoto = String.valueOf(uri);

                                    progressDialog.dismiss();
                                    Toast.makeText(CreateActivity.this,
                                            "Saving Data successfully", Toast.LENGTH_SHORT).show();
                                    String final_nama = strnama ;
                                    int final_no = Integer.parseInt(strno);
                                    String final_code_barang = strcode_barang;
                                    String final_foto = strfoto;
                                    String final_merk = strmerk;
                                    String final_satuan = strsatuan+" "+ss;
                                    String final_keterangan = strketerangan;
                                    String final_ukuran = strukuran;
                                    double final_harga = Double.parseDouble(strharga);

                                    Barang upload = new Barang(final_nama, final_no,
                                            final_foto, final_code_barang, final_merk, final_satuan , final_keterangan, final_ukuran,
                                            "New", final_harga, "Tersedia");
                                    FirebaseDatabase.getInstance().getReference("Barang")
                                            .child(String.valueOf(final_no)).setValue(upload);
                                    konfirTambahLagi();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(CreateActivity.this, e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) /
                            taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                }
            });
        } else {
            Toast.makeText(this,"Make sure all data is correct",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void konfirTambahLagi() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Success !!!");
        builder.setMessage("Do you want to save data again?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                nama.setText("");
                no.setText("");
                code_barang.setText("");
                merk.setText("");
                satuan.setText("");
                keterangan.setText("");
                ukuran.setText("");
                harga.setText("");
                onResume();
                contentFrame.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
                kembali();
            }
        });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private void kembali() {
        Intent back = new Intent();
        setResult(RESULT_OK, back);
        finish();
    }
    private void cekInputan() {
        strnama= nama.getText().toString();
        strno = no.getText().toString();
        strcode_barang = code_barang.getText().toString();
        strmerk = merk.getText().toString();
        strsatuan = satuan.getText().toString();
        strketerangan = keterangan.getText().toString();
        strukuran = ukuran.getText().toString();
        strharga = harga.getText().toString();
        if (strcode_barang.isEmpty()){
            tampil_pop();
            return;
        }
        if (strnama.isEmpty()){
            nama.setError("Insert data");
            nama.requestFocus();
            return;
        }
        if (strmerk.isEmpty()){
            merk.setError("Insert data");
            merk.requestFocus();
            return;
        }
        if (strsatuan.isEmpty()){
            satuan.setError("Insert data");
            satuan.requestFocus();
            return;
        }
        if (strketerangan.isEmpty()){
            keterangan.setError("Insert data");
            keterangan.requestFocus();
            return;
        }
        if (strukuran.isEmpty()){
            ukuran.setError("Insert data");
            ukuran.requestFocus();
            return;
        }
        if (strharga.isEmpty()){
            harga.setError("Insert data");
            harga.requestFocus();
        }else saving();
    }

    private void tampil_pop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Gagal");
        builder.setMessage("Silahkan scan barang terlabih dahulu...\nTekan ya untuk melanjutkan");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert1 = builder.create();
        alert1.show();
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
        code_scan = rawQRData.getText();
        //cek ke data base

        cekCodeExis(code_scan);

    }


    private void cekCodeExis(final String code) {
        databaseBarang = FirebaseDatabase.getInstance().getReference().child("Barang");
        databaseBarang.orderByChild("code_barang").equalTo(code_scan)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //apakah barang code sudah digunakan mesan error?
                        //jika belum ada melanjutkan
                        if (dataSnapshot.exists()) {
                            pop_up(code);
                            framForm.setVisibility(View.GONE);
                        } else {
                            berhasilScan();
                        }
                    }
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        code_barang.setError(code_scan+" already exists");
                        mQRScanner.setResultHandler(CreateActivity.this);
                        mQRScanner.startCamera();
                    }
                });
    }

    private void pop_up(String code_scan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Warning");
        builder.setMessage(code_scan+" already exists");
        builder.setPositiveButton("Ulangi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
                onResume();
                framForm.setVisibility(View.VISIBLE);
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
                onStop();
                finish();
            }
        });
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

    private void berhasilScan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Berahsil scan");
        builder.setMessage("Gunakan code ini ?\n"+code_scan);
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
                code_barang.setText(code_scan);
                mQRScanner.stopCamera();
                contentFrame.setVisibility(View.GONE);
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
                contentFrame.setVisibility(View.VISIBLE);
                onResume();
            }
        });
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

    private void cekjumlahbarang() {
        Query queryNew =  FirebaseDatabase.getInstance().getReference().child("Barang").orderByChild("no");
        queryNew.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int size = (int) dataSnapshot.getChildrenCount();
                no.setText( String.valueOf(size+1));
                strno=no.toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CreateActivity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //ketika tombol back di tekan maka akan menutup activity
        finish();
    }
}
