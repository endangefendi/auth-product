package com.agus.hendrik.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.agus.hendrik.myapp.R;
import com.squareup.picasso.Picasso;

public class ViewFotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_foto);
        ImageView imageView = findViewById(R.id.ivFoto);
        Bundle bun=this.getIntent().getExtras();
        assert bun != null;
        String url = bun.getString("foto");
        assert url != null;
        Log.d("ViewFotoActivity : ", url);

        Picasso.get()
                .load(url)
                .fit()
                .into(imageView);

        ImageView back = findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
