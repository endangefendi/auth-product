package com.agus.hendrik.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agus.hendrik.adapter.BarangAdapter;
import com.agus.hendrik.adapter.BarangAdapterAdmin;
import com.agus.hendrik.model.Barang;
import com.agus.hendrik.myapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BarangFragmentAdmin extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Barang> list;
    private ProgressBar pBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_barang, container, false);
        recyclerView = view.findViewById(R.id.list);
        pBar = view.findViewById(R.id.progressBar);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = new ArrayList<>();
        pBar.setVisibility(View.VISIBLE);

        loadAllBarang();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public BarangFragmentAdmin() {
    }

    public static BarangFragmentAdmin newInstance(int pageNo) {
        Bundle args = new Bundle();
        BarangFragmentAdmin fragment = new BarangFragmentAdmin();
        fragment.setArguments(args);
        return fragment;
    }

    private void loadAllBarang() {
        final BarangAdapterAdmin barangAdapter = new BarangAdapterAdmin(getActivity(),list);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(barangAdapter);
        Query queryNew =  FirebaseDatabase.getInstance().getReference().child("Barang").orderByChild("status").equalTo("Tersedia");
        queryNew.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    Barang newBarang = dataSnapshot1.getValue(Barang.class);
                    list.add(newBarang);
                }
                barangAdapter.notifyDataSetChanged();
                pBar.setVisibility(View.GONE);
                recyclerView.setAdapter(barangAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
