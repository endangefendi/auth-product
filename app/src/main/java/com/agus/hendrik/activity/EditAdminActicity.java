package com.agus.hendrik.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.agus.hendrik.adapter.AdminAdapter;
import com.agus.hendrik.adapter.UserAdapter;
import com.agus.hendrik.model.UserProfile;
import com.agus.hendrik.myapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditAdminActicity extends AppCompatActivity {

    RecyclerView recyclerViewAdmin,recyclerViewUser;
    ArrayList<UserProfile> listAdmin,listUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_admin_acticity);

        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerViewAdmin = findViewById(R.id.list_admin);
        recyclerViewUser = findViewById(R.id.list_user);
        listAdmin = new ArrayList<>();
        listUser = new ArrayList<>();

        loadAdmin();
        loadUser();
    }


    private void loadAdmin() {
        final AdminAdapter adminAdapter = new AdminAdapter(this,listAdmin);
        LinearLayoutManager llmBest = new LinearLayoutManager(this);
        llmBest.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewAdmin.setLayoutManager(llmBest);
        recyclerViewAdmin.setAdapter(adminAdapter);

        Query query =  FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("level").equalTo(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listAdmin.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        UserProfile pojo = issue.getValue(UserProfile.class);
                            listAdmin.add(pojo);

                    }
                }
                adminAdapter.notifyDataSetChanged();
                recyclerViewAdmin.setVisibility(View.VISIBLE);
                recyclerViewAdmin.setAdapter(adminAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditAdminActicity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUser() {
        final UserAdapter userAdapter = new UserAdapter(this,listUser);
        LinearLayoutManager llmBest = new LinearLayoutManager(this);
        llmBest.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewUser.setLayoutManager(llmBest);
        recyclerViewUser.setAdapter(userAdapter);

        Query query =  FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("level").equalTo(0);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listUser.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        UserProfile pojo = issue.getValue(UserProfile.class);
                        listUser.add(pojo);
                    }
                }
                userAdapter.notifyDataSetChanged();
                recyclerViewUser.setVisibility(View.VISIBLE);
                recyclerViewUser.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditAdminActicity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}