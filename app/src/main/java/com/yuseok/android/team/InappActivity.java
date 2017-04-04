package com.yuseok.android.team;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class InappActivity extends AppCompatActivity {

    final int DIALOG_LIST = 1; // 리스트 형식의 다이얼로그 ID


    TextView textUser;
    ImageView imageView;

    Button painter, btnInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inapp);

        imageView = (ImageView) findViewById(R.id.imageView);

        textUser = (TextView) findViewById(R.id.textUser);

        btnInfo = (Button) findViewById(R.id.btnInfo);

        Intent intent = getIntent();

        String data = intent.getStringExtra("user");


        textUser.setText(data + "");

        try {
            // String으로 받아온 json값에서 id값만 뽑아내기
            String[] first = data.split(":");
            String temp = first[1];
            String[] sec = temp.split(",");
            String id = sec[0];
            String id2 = id.substring(1, id.length() - 1);
            //===========================================

            Glide.with(this).load("https://graph.facebook.com/" + id2 + "/picture?type=large").into(imageView);

        } catch (Exception e) {
            }

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InappActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });
    }
}

