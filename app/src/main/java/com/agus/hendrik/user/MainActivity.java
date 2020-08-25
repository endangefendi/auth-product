package com.agus.hendrik.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.agus.hendrik.activity.ScanCode;
import com.agus.hendrik.fragment.HomeFragment;
import com.agus.hendrik.fragment.ProfileFragment;
import com.agus.hendrik.myapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    FrameLayout layout;
    private String nama_lengkap = null, nama_depan = null, nama_belakang = null, stremail = null, strfoto = null;
    private ImageView imageViewHome, imageViewProfile;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private TextView textViewHome, textViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragmment(new HomeFragment());

        checkAndRequestPermissions();

        layout = findViewById(R.id.container);
        textViewHome = findViewById(R.id.tv_home);
        textViewProfile = findViewById(R.id.tv_profil);

        imageViewHome = findViewById(R.id.iv_home);
        imageViewProfile = findViewById(R.id.iv_profil);

        LinearLayout nav_home = findViewById(R.id.nav_home);
        nav_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewHome.setTextColor(getResources().getColor(R.color.biru));
                textViewProfile.setTextColor(getResources().getColor(R.color.biru_dongker));
                textViewHome.setTypeface(Typeface.DEFAULT_BOLD);
                textViewProfile.setTypeface(Typeface.DEFAULT);
                textViewHome.setTextSize(15);
                textViewProfile.setTextSize(12);

                imageViewHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_home));
                imageViewProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profil_dongker));
                loadFragmment(new HomeFragment());
            }
        });

        LinearLayout nav_scan = findViewById(R.id.nav_scan);
        nav_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScanCode.class));
            }
        });

        LinearLayout nav_profile = findViewById(R.id.nav_profil);
        nav_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewHome.setTextColor(getResources().getColor(R.color.biru_dongker));
                textViewProfile.setTypeface(Typeface.DEFAULT_BOLD);
                textViewHome.setTypeface(Typeface.DEFAULT);
                textViewProfile.setTextColor(getResources().getColor(R.color.biru));
                textViewProfile.setTextSize(15);
                textViewHome.setTextSize(12);

                imageViewHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_dongker));
                imageViewProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profil));
                loadAkun();
            }
        });

    }

    private void loadAkun() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            nama_lengkap = currentUser.getDisplayName();
            assert nama_lengkap != null;
            int i = nama_lengkap.indexOf(" ");
            nama_depan = nama_lengkap.substring(0, i);
            nama_belakang = nama_lengkap.substring(i + 1);
            stremail = currentUser.getEmail();
            strfoto = String.valueOf(currentUser.getPhotoUrl());

        }
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nama_depan", nama_depan);
        bundle.putString("nama_belakang", nama_belakang);
        bundle.putString("nama_lengkap", nama_lengkap);
        bundle.putString("email", stremail);
        bundle.putString("foto", strfoto);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    public void loadFragmment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit(); }

    private void checkAndRequestPermissions() {

        int cam = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int loc = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int phoneSta = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (cam != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (phoneSta != PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

}
