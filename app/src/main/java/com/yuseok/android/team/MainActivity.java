package com.yuseok.android.team;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;

import static android.widget.Toast.makeText;
import static com.yuseok.android.team.R.id.editEmail;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // 자동로그인소스
    private CallbackManager callbackManager;
    private final String TAG = "MainActivity";

    public static SharedPreferences sf;


    // Facebook Login Button
//    LoginButton faceLogin;
    Button faceLogin;
    // 페이스북 결과값을 넣는다

    String faceResult;

    // 이메일
    EditText editId, editPw;
    Button btnLogin;

    // 로그인 값 저장
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String sfValue;
    String str, str2, str3;


    // 다이얼로그 test
    EditText testEmail, testPw, testPwCon, regisId;
    Button testButton;
            //testLogin;

    // 로고 이미지
    ImageView logoImage;
    Animation animation;

    // 아이디 패스워드 일치확인
    String chId;
    String chPw;


    // 메인에 들어갈것들을 묶어놓은 레이아웃
    FrameLayout groupView;

    // 페이스북 데이터들
    String faceBooks, id, name, email, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        final Animation trans = AnimationUtils.loadAnimation(this,R.anim.animation);


        editId = (EditText) findViewById(editEmail);
        editPw = (EditText) findViewById(R.id.editPw);

        btnLogin = (Button) findViewById(R.id.btnLogin);
//        btnSignup = (Button) findViewById(R.id.btnSignup);


        // 1. 애니메이션 부분 시작 ==================================================================
        logoImage = (ImageView) findViewById(R.id.logoImage);
//        imageView = (FrameLayout) findViewById(R.id.imageView);
        // 이미지를 천천히 띄워준다
        animation = new AlphaAnimation(0, 1);

        animation.setDuration(1000);
        logoImage.setVisibility(View.VISIBLE);
        logoImage.setAnimation(animation);
        // ===============


        logoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoImage.startAnimation(trans);

                groupView = (FrameLayout) findViewById(R.id.groupView);
                Animation animation = new AlphaAnimation(0, 1);

                animation.setDuration(1500);
                groupView.setVisibility(View.VISIBLE);
                groupView.setAnimation(animation);
                animation.getFillAfter();

                logoImage.setClickable(false);
            }

        });
        // 1. 애니메이션 부분 끝 ==================================================================

        // 엔터키 설정 =======================================
        editPw.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    onClick(btnLogin);
                }
                return false;
            }
        });
        // ======================================================

        // testButton
        testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                final View mview = getLayoutInflater().inflate(R.layout.dialog_regis,null);
                // 테스트 버튼
                Button testLogin = (Button) mview.findViewById(R.id.testLogin);
//                Button testCancel = (Button) findViewById(R.id.testCancel);
                testEmail = (EditText) mview.findViewById(R.id.testEmail);
                testPw = (EditText) mview.findViewById(R.id.testPw);
                testPwCon = (EditText) mview.findViewById(R.id.testPwCon);
                regisId = (EditText) mview.findViewById(R.id.regisId);
                Log.i("회원가입창에서의 정보" , testEmail + "," + testPw + ", " + testPwCon+ ", " + regisId);

                // 비밀번호 일치 검사
                testPwCon.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String password = testPw.getText().toString();
                        String confirm = testPwCon.getText().toString();

                        if (password.equals(confirm)) {
                            testPw.setBackgroundColor(Color.GREEN);
                            testPwCon.setBackgroundColor(Color.GREEN);
                        } else {
                            testPw.setBackgroundColor(Color.RED);
                            testPwCon.setBackgroundColor(Color.RED);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });



                // 2. 회원가입 다이얼로그 시작 =====================================================
                testLogin.setOnClickListener(new View.OnClickListener() {

                    int checkCount = 0;
                    @Override
                    public void onClick(View v) {

                        String email = testEmail.getText().toString();
                        String password = testPw.getText().toString();

                        // 이메일 입력, gudtlr 확인
                        if (testEmail.getText().toString().length() == 0) {
                            if (!SignUtil.validateEmail(email)) {
//                        checkEmail.setVisibility(view.VISIBLE);
                                makeText(MainActivity.this, "Email 형식이 틀립니다.!", Toast.LENGTH_SHORT).show();
                                checkCount++;
                            }
                            makeText(MainActivity.this, "Email을 입력하세요!", Toast.LENGTH_SHORT).show();
                            testPw.requestFocus();
                            return;
                        }

                        // 서버에서 중복되는 아이디값이 있는지 확인하는 로직 추가
                        if(regisId.getText().toString().length() == 0) {
                            makeText(MainActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                            checkCount++;

                        }

                        // 비밀번호 입력 형식 확인
                        if (testPw.getText().toString().length() == 0) {
                            if (!SignUtil.validatePassword(password)) {
                                makeText(MainActivity.this, "Password 형식이 틀립니다.!", Toast.LENGTH_SHORT).show();

                            }
                            makeText(MainActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                            testPw.requestFocus();
                            return;
                        }

                        // 비밀번호 확인 입력 확인
                        if (testPwCon.getText().toString().length() == 0) {
                            makeText(MainActivity.this, "비밀번호 확인을 입력하세요!", Toast.LENGTH_SHORT).show();
                            testPwCon.requestFocus();
                            return;
                        }

                        // 비밀번호 일치 확인
                        if (!testPw.getText().toString().equals(testPwCon.getText().toString())) {
                            makeText(MainActivity.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                            testPw.setText("");
                            testPwCon.setText("");
                            testPw.requestFocus();
                            return;
                        }

                    }
                });
                mBuilder.setView(mview);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }

        });

        // 2. 회원가입 다이얼로그 끝 =====================================================



        // 3. 로그인 버튼 작동 시작 ==========================================================
        btnLogin.setOnClickListener(this);
        // 3. 로그인 버튼 작동 끝 ==========================================================

        // 자동 로그인부분
        pref = getSharedPreferences(sfValue, 0);
        str = pref.getString("Id", ""); // 키값으로 꺼냄
        str2 = pref.getString("Pw", "");
        str3 = pref.getString("Face", "");
        editId.setText(str);// editId에 반영함
        editPw.setText(str2);

//        faceLogin.setText(str3);

        chId = editId.getText().toString();
        chPw = editPw.getText().toString();

        if(chId.equals("ys920603@gmail.com") && chPw.equals("111") ){
            Intent i = new Intent(MainActivity.this, InappActivity.class);
            startActivity(i);
            finish();
        }
        else if (!str3.equals("")){
//            Toast.makeText(MainActivity.this,"불일치합니다",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent (MainActivity.this,InappActivity.class);
            intent.putExtra("user",str3) ;
            startActivityForResult(intent, RESULT_OK);
            finish();
        }
        // 자동 로그인 확인완료


    }



    // 4. 페이스북 로그인 시작 ==============================================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //패이스북 로그인 결과를 콜백에 담는다.
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.i("requestCode=========", requestCode + "");
        Log.i("resultCode=========", resultCode+"");
        Log.i("data=========", data + "");



//        faceResult = resultCode+"";
//        Log.i("다른 결과값=========", faceResult);

    }

    public void facebookLoginOnClick(View v){
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(MainActivity.this,
                Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult loginResult) {

                GraphRequest request;
                request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (response.getError() != null) {

                        } else {
                            Log.i("TAG", "user: " + object.toString());
                            Log.i("TAG", "AccessToken: " + loginResult.getAccessToken().getToken());
                            setResult(RESULT_OK);

                            faceBooks = object.toString();
                            faceLogin(object);
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("test", "Error: " + error);
                //finish();
            }

            @Override
            public void onCancel() {
                //finish();
            }
        });

    }

    // 4. 페이스북 로그인 끝 ==============================================


    // Shared로 저장하기
    @Override
    protected void onStop() {
        super.onStop();
        // SharedPreferences 에 설정값(특별히 기억해야할 사용자 값)을 저장하기
        sf = getSharedPreferences(sfValue, 0);
        SharedPreferences.Editor editor = sf.edit();//저장하려면 editor가 필요
        String str = editId.getText().toString(); // 사용자가 입력한 값
        String str2 = editPw.getText().toString();
//        String str3 = faceResult.toString();
//        Log.i("faceLogin값은====", faceResult.toString() );
        editor.putString("Id", str); // 입력
        editor.putString("Pw", str2); // 입력
        editor.putString("Face", faceBooks);
//        Log.i("str3값은====", str3);
        editor.commit(); // 파일에 최종 반영함
    }
    // Shared저장완료


    // 로그인을 눌렀을 경우
    Intent intent;
    @Override
    public void onClick(View v) {

        // InappActivity로 넘겨주기
        switch (v.getId()) {
            case R.id.btnLogin:
                chId = editId.getText().toString();
                chPw = editPw.getText().toString();


                if(chId.equals("ys920603@gmail.com") && chPw.equals("111") ){
                Intent i = new Intent(MainActivity.this, InappActivity.class);
                startActivity(i);
                finish();}
                else {
                    makeText(MainActivity.this,"불일치합니다",Toast.LENGTH_SHORT).show();
                }
                break;

        }


    }

    public void faceLogin(JSONObject object) {

        intent = new Intent(MainActivity.this, InappActivity.class);
        intent.putExtra("user", object.toString());
//                        intent.putExtra("id", resultId.toString());
        startActivityForResult(intent, RESULT_OK);
        finish();
    }
}