package com.yuseok.android.team;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import static com.yuseok.android.team.MainActivity.sf;

public class InfoActivity extends AppCompatActivity {

    ImageView iPicture;
    EditText editText;
    Button btnCon;
    InputMethodManager imm;

    // 로그아웃 테스트
    Button btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // 로그아웃 테스트
        btnLogout = (Button) findViewById(R.id.btnLogout);

        iPicture = (ImageView) findViewById(R.id.iPicture);

        editText = (EditText) findViewById(R.id.editText);

        // 수정하기위한 editText를 띄워주는 버튼
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        btnCon = (Button)findViewById(R.id.btnCon);


        btnCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnCon.getText().equals("수정")) {
                    editText.setFocusableInTouchMode(true);
                    editText.requestFocus();
                    imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
                    btnCon.setText("완료");
                } else if(btnCon.getText().equals("완료")) {
                    editText.setFocusable(false);
                    btnCon.setText("수정");
                    imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
                    editText.setImeOptions(EditorInfo.IME_ACTION_DONE);

                }
                editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if(actionId == EditorInfo.IME_ACTION_DONE) {
                            onClick(btnCon);
                        }
                        return false;
                    }
                });

            }

            // TODO: 서버에 자기소개를 보내주는 로직 추가
        });

        btnLogout.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sf.edit();
                editor.clear();
                editor.commit();
            }
        });


        // id자리에 페이스북에서 받은 아이디 값 넣기
        Glide.with(this).load("https://graph.facebook.com/" + "1313470802075087" + "/picture?type=large").into(iPicture);
        // Glide.with(this).load("https://graph.facebook.com/" + id2 + "/picture?type=large").into(imageView);


    }

}
