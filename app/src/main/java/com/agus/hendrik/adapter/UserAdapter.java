package com.agus.hendrik.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agus.hendrik.model.UserProfile;
import com.agus.hendrik.myapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<UserProfile> users;

    public UserAdapter(Context c , ArrayList<UserProfile> p){
        context = c;
        users = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_user,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.email.setText(users.get(position).getEmail());
        holder.jadikanAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uid = users.get(holder.getAdapterPosition()).getUid();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Jadikan sebagai admin?")
                        .setMessage("Tekan (Yes) untuk melanjutkan")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(uid);
                                try {
                                    ref.child("status").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            ref.child("level").setValue(1);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            ref.child("level").setValue(0);
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }                    }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView email;
        ImageView jadikanAdmin;

        private MyViewHolder(View itemView) {
            super(itemView);
            email =  itemView.findViewById(R.id.et_email);
            jadikanAdmin = itemView.findViewById(R.id.jadikan_admin);
        }
    }
}