package com.agus.hendrik.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agus.hendrik.activity.BarangActivity;
import com.agus.hendrik.activity.ScanCode;
import com.agus.hendrik.adapter.BarangAdapter;
import com.agus.hendrik.adapter.ContentAdapter;
import com.agus.hendrik.adapter.HomeAdapter;
import com.agus.hendrik.adapter.SliderAdapter;
import com.agus.hendrik.adapter.kategoriAdapter;
import com.agus.hendrik.model.Barang;
import com.agus.hendrik.model.Home;
import com.agus.hendrik.model.KategoriModel;
import com.agus.hendrik.myapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements HomeAdapter.OnItemClickListener , kategoriAdapter.OnItemClickListener{
    private GridView grdView;

    private static final String TAG = "Home Fragment";

    private ProgressBar pBar,pBarNew;
    private TextView tvUsername,tvEmail,tvTitle;
    private RecyclerView recyclerView, recyclerViewnew;
    private ArrayList<Barang> list,listNew;
    GridView listkategori;

    RelativeLayout ReBest, ReNew, Rekategori;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view   = inflater.inflate(R.layout.fragment_home, container, false);
        listkategori     = view.findViewById(R.id.listkategori);
        grdView     = view.findViewById(R.id.gv_list);
        pBar = view.findViewById(R.id.progressBar);
        pBarNew = view.findViewById(R.id.progressBar1);
        RelativeLayout frame = view.findViewById(R.id.frameTitle);
        frame.setVisibility(View.VISIBLE);
        ReBest = view.findViewById(R.id.ReBest);
        ReNew = view.findViewById(R.id.ReNew);
        Rekategori = view.findViewById(R.id.Rekategori);
        tvUsername = view.findViewById(R.id.tv_user);
        tvEmail = view.findViewById(R.id.tv_email);
        tvTitle = view.findViewById(R.id.tv_title);
        recyclerView = view.findViewById(R.id.list);
        recyclerViewnew = view.findViewById(R.id.listNew);
        recyclerView.setVisibility(View.GONE);
        recyclerViewnew.setVisibility(View.GONE);
        SliderView sliderView = view.findViewById(R.id.imageSlider);

        SliderAdapter adapter1 = new SliderAdapter(getContext());

        sliderView.setSliderAdapter(adapter1);

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(5); //set scroll delay in seconds :
        sliderView.startAutoCycle();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HomeAdapter homeAdapter = new HomeAdapter(getContext(), 1, this);
        grdView.setAdapter(homeAdapter);

        kategoriAdapter Adapter = new kategoriAdapter(getContext(),  this);
        listkategori.setAdapter(Adapter);
        pBar.setVisibility(View.VISIBLE);
        pBarNew.setVisibility(View.VISIBLE);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String stringTitle = getResources().getString(R.string.app_name);
        tvTitle.setText(stringTitle);

        if (currentUser != null) {
            String nama = currentUser.getDisplayName();
            assert nama != null;
            int i = nama.indexOf(" ");
            tvUsername.setText(nama.substring(0,i));
            String email = currentUser.getEmail();
            tvEmail.setText(email);
        }

        list = new ArrayList<>();
        listNew = new ArrayList<>();

        loadBestProduct();
    }
//
//    private void loadKategori() {
//
//        Query queryNew =  FirebaseDatabase.getInstance().getReference().child("T_Kategori").orderByChild("kategori");
//        queryNew.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                listkategori.clear();
//                if (dataSnapshot.exists()) {
//                    Rekategori.setVisibility(View.VISIBLE);
//                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
//                        Barang pojo = issue.getValue(Barang.class);
//                        if (pojo.getKategori().equalsIgnoreCase(pojo.getKategori().toString()) ) {
//                            listkategori.add(pojo);
//                        }
//                    }
//                }else {
//                    Rekategori.setVisibility(View.GONE);
//                }
//                kategoriAdapter.notifyDataSetChanged();
//                pBarKategori.setVisibility(View.GONE);
//                recyclerViewKategori.setVisibility(View.VISIBLE);
//                recyclerViewKategori.setAdapter(kategoriAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getActivity(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
//            }
//        });
//        loadBestProduct();
//    }

    private void loadNewBarang() {
        final ContentAdapter barangAdapternew = new ContentAdapter(getActivity(),listNew);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewnew.setLayoutManager(llm);
        recyclerViewnew.setAdapter(barangAdapternew);

        Query queryNew =  FirebaseDatabase.getInstance().getReference().child("Barang").orderByChild("jenis").equalTo("New");
        queryNew.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listNew.clear();
                if (dataSnapshot.exists()) {
                    ReNew.setVisibility(View.VISIBLE);
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Barang pojo = issue.getValue(Barang.class);
                        if(pojo.getStatus().equals("Tersedia")){
                            listNew.add(pojo);
                        }
                    }
                }else {
                    ReNew.setVisibility(View.GONE);
                }
                barangAdapternew.notifyDataSetChanged();
                pBarNew.setVisibility(View.GONE);
                recyclerViewnew.setVisibility(View.VISIBLE);
                recyclerViewnew.setAdapter(barangAdapternew);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBestProduct() {
        final ContentAdapter barangAdapterBest = new ContentAdapter(getActivity(),list);
        LinearLayoutManager llmBest = new LinearLayoutManager(getContext());
        llmBest.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llmBest);
        recyclerView.setAdapter(barangAdapterBest);

        Query query =  FirebaseDatabase.getInstance().getReference().child("Barang").orderByChild("jenis").equalTo("Best");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                if (dataSnapshot.exists()) {
                    ReBest.setVisibility(View.VISIBLE);
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Barang pojo = issue.getValue(Barang.class);
                        if(pojo.getStatus().equals("Tersedia")){
                            list.add(pojo);
                        }
                    }
                }else {
                 ReBest.setVisibility(View.GONE);
                }
                barangAdapterBest.notifyDataSetChanged();
                pBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(barangAdapterBest);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
        loadNewBarang();
    }

    private void loadAbout() {
        ImageView close;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams")
        final View dialogView = inflater.inflate(R.layout.about, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(true);
        b.setCancelable(false);
        b.show();
        close = dialogView.findViewById(R.id.btn_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
    }

    private void loadProfileBVJ() {
        ImageView close,web, lokasi, ig;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams")
        final View dialogView = inflater.inflate(R.layout.profile_bvj, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(true);
        b.setCancelable(false);
        b.show();
        close = dialogView.findViewById(R.id.btn_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

        web = dialogView.findViewById(R.id.ic_web);
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webbvj();
            }
        });

        lokasi = dialogView.findViewById(R.id.ic_lokasi);
        lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapbvj();
            }
        });

        ig = dialogView.findViewById(R.id.ic_ig);
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                igbvj();
            }
        });
    }

    private void mapbvj() {
        String uri = "http://maps.app.goo.gl/KJ2DsdXAZZj19xjMA";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    private void igbvj() {
        String uri = "https://www.instagram.com/bvjmodified/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    private void webbvj() {
        String uri = "http://bakulvariasijogja.blogspot.com";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    @Override
    public void onItemClicked(int position, Home item) {
            switch (position) {
                case 0: {
                    startActivity(new Intent(getActivity(), BarangActivity.class));
                } break;
                case 1: {
                    startActivity(new Intent(getActivity(), ScanCode.class));
                } break;
                case 2: {
                    loadAbout();
                } break;
                case 3: {
                    loadProfileBVJ();
                } break;
            }
    }


    @Override
    public void onItemClicked(int position, KategoriModel item) {

    }
}
