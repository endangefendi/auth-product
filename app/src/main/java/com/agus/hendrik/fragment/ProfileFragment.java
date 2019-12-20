package com.agus.hendrik.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.agus.hendrik.activity.LoginActivity;
import com.agus.hendrik.activity.ViewFotoActivity;
import com.agus.hendrik.myapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private GoogleSignInClient googleSignInClient;

    private static final String TAG = "Profile Fragment";
    private TextView namaDepan,tittle;
    private TextView namaLengkap;
    private TextView namaBelakang;
    private TextView email;
    private CircleImageView foto;
    private ImageView greetImg;
    private TextView greetText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view   = inflater.inflate(R.layout.fragment_profile, container, false);

        namaDepan = view.findViewById(R.id.et_namaDepan);
        namaLengkap = view.findViewById(R.id.et_namaLengkap);
        foto = view.findViewById(R.id.ivFoto);
        email = view.findViewById(R.id.et_email);
        namaBelakang = view.findViewById(R.id.et_namaBelakang);
        tittle = view.findViewById(R.id.tv_title);
        TextView versi = view.findViewById(R.id.tvVersi);
        Button logout = view.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                konfir();
            }
        });
        try {
            String versionName = getContext().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            versi.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        greetImg = view.findViewById(R.id.greeting_img);
        greetText = view.findViewById(R.id.greeting_text);
        greeting();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.TOKEN_ID))
                    .requestEmail()
                    .build();
            googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        if (getArguments() != null) {
            String strnama_depan = getArguments().getString("nama_depan");
            String strnama_belakang = getArguments().getString("nama_belakang");
            String strnama_lengkap = getArguments().getString("nama_lengkap");
            String strfoto = getArguments().getString("foto");
            String stremail = getArguments().getString("email");

            namaDepan.setText(strnama_depan);
            namaBelakang.setText(strnama_belakang);
            namaLengkap.setText(strnama_lengkap);
            email.setText(stremail);

            Picasso.get()
                    .load(strfoto)
                    .fit()
                    .into(foto);
        }

        assert getArguments() != null;
        final String strfoto = getArguments().getString("foto");
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bun= new Bundle();
                Intent i = new Intent(getActivity(), ViewFotoActivity.class);
                bun.putString("foto", strfoto);
                i.putExtras(bun); startActivity(i);
            }
        });


    }

    private void konfir() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
    googleSignInClient.signOut().addOnCompleteListener(getActivity(),
        new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
    }

    private void greeting() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        String strnama_depan = null;
        if (getArguments() != null) {
             strnama_depan = getArguments().getString("nama_depan");
        }
        if (timeOfDay >= 4 && timeOfDay < 11) {
            greetText.setText("Selamat Pagi: "+strnama_depan);
            greetImg.setImageResource(R.drawable.img_default_half_morning);
        } else if (timeOfDay >= 11 && timeOfDay < 15) {
            greetText.setText("Selamat Siang: "+strnama_depan);
            greetImg.setImageResource(R.drawable.img_default_half_afternoon);
        } else if (timeOfDay >= 15 && timeOfDay < 18) {
            greetText.setText("Selamat Sore: "+strnama_depan);
            greetImg.setImageResource(R.drawable.img_default_half_without_sun);
        } else if (timeOfDay >= 18 && timeOfDay < 24) {
            greetText.setText("Selamat Malam: "+strnama_depan);
            greetText.setTextColor(Color.WHITE);
            greetImg.setImageResource(R.drawable.img_default_half_night);
        }else {
            greetText.setText("Hallo: "+strnama_depan);
            greetText.setTextColor(Color.WHITE);
            tittle.setTextColor(Color.WHITE);
            greetImg.setImageResource(R.drawable.img_default_half_night);
        }
    }
}
