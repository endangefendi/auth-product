package com.agus.hendrik.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.agus.hendrik.fragment.BarangFragmentAdmin;
import com.agus.hendrik.fragment.PengaturanFragmentAdmin;
import com.agus.hendrik.myapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class DashboadAdmin extends AppCompatActivity {


    private String[] mTabsText = {"Barang", "Pengaturan"};
    private GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboad_admin);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        String stremail = currentUser.getEmail();
        assert stremail != null;
        stremail = currentUser.getEmail();

        TextView email= findViewById(R.id.tv_user_email);
        email.setText(stremail);

        FloatingActionButton tambah_barang = findViewById(R.id.btn_tambah);
        tambah_barang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboadAdmin.this, CreateActivity.class));
            }
        });

        ViewPager viewPager = findViewById(R.id.view_pager);
        DashboadAdmin.MyPagerAdapter pagerAdapter = new DashboadAdmin.MyPagerAdapter(getSupportFragmentManager());
        if (viewPager != null)
            viewPager.setAdapter(pagerAdapter);

        TabLayout mTabLayout = findViewById(R.id.tab_layout);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(viewPager);
            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(pagerAdapter.getTabView(i));
            }
            Objects.requireNonNull(Objects.requireNonNull(
                    mTabLayout.getTabAt(0)).getCustomView()).setSelected(true);
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.TOKEN_ID))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(DashboadAdmin.this, gso);
        Button logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                konfir();
            }
        });
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        View getTabView(int position) {
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(DashboadAdmin.this).inflate(R.layout.toolbar, null);
            TextView textView = view.findViewById(R.id.judulPagger);
            textView.setTextColor(Color.WHITE);
            textView.setText(mTabsText[position]);
            return view;
        }

        @NonNull
        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    return BarangFragmentAdmin.newInstance(1);
                case 1:
                    return PengaturanFragmentAdmin.newInstance(2);
            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabsText[position];
        }
    }
    private void konfir() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboadAdmin.this);
        builder.setCancelable(false);
        builder.setTitle("Warning !!!");
        builder.setMessage("Do you want to Logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                if (googleSignInClient!=null){
                    logout_google();
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void logout_google(){
        googleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                });
    }
}
