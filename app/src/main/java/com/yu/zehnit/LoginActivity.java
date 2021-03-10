package com.yu.zehnit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

    public static final String REGEX_MOBILE = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";

    private EditText editPhoneNumber;
    private EditText editCode;
    private Button btnLogin;
    private Button btnGetCode;
    private Intent intent;
    private TimeCount timeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intent = new Intent(LoginActivity.this, MainActivity.class);

        // 若已登录，直接进入主页
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        Boolean isLogin = pref.getBoolean("isLogin", false);
        if (isLogin) startActivity(intent);

        editPhoneNumber = findViewById(R.id.editText_phone);
        editCode = findViewById(R.id.edit_number_enter_checknum);
        editPhoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        editCode.setInputType(InputType.TYPE_CLASS_NUMBER);

        btnLogin = findViewById(R.id.button_login);
        btnGetCode = findViewById(R.id.button_get_checknum);
        btnLogin.setEnabled(false);
        btnGetCode.setEnabled(false);

        editPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phoneNumber = editPhoneNumber.getText().toString();
                if (phoneNumber.matches(REGEX_MOBILE)) {
                    btnGetCode.setEnabled(true);
                } else {
                    btnGetCode.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editCode.getText().length() == 6) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeCount = new TimeCount(60000,1000);
                timeCount.start();
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editCode.getText().toString().equals("123456")) {
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.putBoolean("isLogin", true);
                    editor.apply();
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "验证码输入错误，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnGetCode.setBackgroundColor(Color.parseColor("#B6B6D8"));
            btnGetCode.setClickable(false);
            btnGetCode.setText("(" + millisUntilFinished / 1000 + ") 秒后可重新发送");
        }

        @Override
        public void onFinish() {
            btnGetCode.setText("重新获取验证码");
            btnGetCode.setClickable(true);
            btnGetCode.setBackgroundColor(Color.parseColor("#008577"));

        }
    }
}