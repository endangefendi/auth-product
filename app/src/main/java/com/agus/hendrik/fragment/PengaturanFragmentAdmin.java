package com.agus.hendrik.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agus.hendrik.activity.EditAdminActicity;
import com.agus.hendrik.adapter.ContentAdapterBarangTidakAkrif;
import com.agus.hendrik.model.Barang;
import com.agus.hendrik.myapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PengaturanFragmentAdmin extends Fragment {

    private static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;
    private ProgressBar pBarBarang;

    private RecyclerView recyclerViewBarang;
    private ArrayList<Barang> listBarangTidakTersedia;
    ImageView setting_admin;

    public PengaturanFragmentAdmin() {
    }

    public static PengaturanFragmentAdmin newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        PengaturanFragmentAdmin fragment = new PengaturanFragmentAdmin();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view   = inflater.inflate(R.layout.pengaturan_admin_fragment, container, false);
        recyclerViewBarang = view.findViewById(R.id.list_product_tidak_aktif);
        recyclerViewBarang.setVisibility(View.GONE);
        pBarBarang = view.findViewById(R.id.progressBar);
        setting_admin = view.findViewById(R.id.setAdmin);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pBarBarang.setVisibility(View.VISIBLE);

        listBarangTidakTersedia = new ArrayList<>();
        setting_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( getContext(), EditAdminActicity.class));
            }
        });

        loadNewBarang();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void loadNewBarang() {
        final ContentAdapterBarangTidakAkrif barangAdapternew = new ContentAdapterBarangTidakAkrif(getActivity(),listBarangTidakTersedia);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewBarang.setLayoutManager(llm);
        recyclerViewBarang.setAdapter(barangAdapternew);

        Query queryNew =  FirebaseDatabase.getInstance().getReference().child("Barang").orderByChild("status").equalTo("Tidak Tersedia");
        queryNew.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listBarangTidakTersedia.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Barang pojo = issue.getValue(Barang.class);
                            listBarangTidakTersedia.add(pojo);

                    }
                }
                barangAdapternew.notifyDataSetChanged();
                pBarBarang.setVisibility(View.GONE);
                recyclerViewBarang.setVisibility(View.VISIBLE);
                recyclerViewBarang.setAdapter(barangAdapternew);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
