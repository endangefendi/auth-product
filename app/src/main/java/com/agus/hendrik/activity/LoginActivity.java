package com.agus.hendrik.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.agus.hendrik.model.UserProfile;
import com.agus.hendrik.myapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;

    ImageView buttonGoogle;
    TextView textWith;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String deviceId;
    GoogleSignInAccount account;
    public static final String TAG = "LoginActivity";
    private ProgressBar progressBar;
    TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        textWith = findViewById(R.id.with);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        textWith.setVisibility(View.VISIBLE);

        buttonGoogle = findViewById(R.id.sign_in_google);

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        checkAndRequestPermissions();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.TOKEN_ID))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });
    }

    private void logout_google() {
        googleSignInClient.signOut().addOnCompleteListener(LoginActivity.this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            if (requestCode == 101) {
                progressBar.setVisibility(View.VISIBLE);
                buttonGoogle.setVisibility(View.GONE);
                textWith.setVisibility(View.GONE);
                try {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    account = task.getResult(ApiException.class);
                    assert account != null;
                    String email = account.getEmail();
                    firebaseAuthWithGoogle(account);
                    Log.d(TAG, "emailResult: " + email);
                } catch (ApiException e) {
                    // The ApiException status code indicates the detailed failure reason.
                    Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                }
            }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        progressBar.setVisibility(View.VISIBLE);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            String Id = user.getUid();
                            tambah_user(account, Id);
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.content_frame), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(final FirebaseUser user) {
        if (user != null) {
            //cek Level
            Query databaseUser = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("email").equalTo(String.valueOf(user.getEmail()));
            databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        UserProfile userProfile = dataSnapshot1.getValue(UserProfile.class);

                        assert userProfile != null;
                        int level = userProfile.getLevel();
                        if (level == 1) {
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(LoginActivity.this, DashboadAdmin.class));
                            finish();
                        } else if (level == 0) {
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    logout_google();
                }


            });
        } else {
            Log.d(TAG, "User dan ID = null");
        }
    }

    private void tambah_user(final GoogleSignInAccount acct, final String auId) {
        DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference("Users").child(auId);
        databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d(TAG, "Email Sudah terdaftar");
                } else {
                    UserProfile insert = new UserProfile(deviceId, 0, acct.getEmail(), auId);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(auId).setValue(insert);
                    Log.d(TAG, "Berhasil Tambah Data");
                }
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @SuppressLint("HardwareIds")
    private void checkAndRequestPermissions() {
        int cam = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int phone_state = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (cam != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (phone_state != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[0]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return;
        }

        deviceId = telephonyManager.getDeviceId();
    }
}
