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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.yu.zehnit.tools.SMS;

public class LoginActivity extends BaseActivity {

    public static final String REGEX_MOBILE = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";

    private EditText editPhoneNumber;
    private EditText editCode;
    private Button btnLogin;
    private Button btnGetCode;
    private CheckBox checkBox;
    private Intent intent;
    private TimeCount timeCount;

    private String telPhone;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intent = new Intent(LoginActivity.this, MainActivity.class);


        editPhoneNumber = findViewById(R.id.editText_phone);
        editCode = findViewById(R.id.edit_number_enter_checknum);
        editPhoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        editCode.setInputType(InputType.TYPE_CLASS_NUMBER);

        btnLogin = findViewById(R.id.button_login);
        btnGetCode = findViewById(R.id.button_get_checknum);
        btnLogin.setEnabled(false);
        btnGetCode.setEnabled(false);

        checkBox = findViewById(R.id.checkBox_textpolicy);

        editPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                telPhone = editPhoneNumber.getText().toString();
                if (telPhone.matches(REGEX_MOBILE)) {
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
                code = editCode.getText().toString();
                if (code.length() == 6 && btnGetCode.isEnabled()) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Runnable runnable = new Runnable() {
            //String telPhone = mPhone.getText().toString();
            @Override
            public void run() {
                try{
                    SMS.SMSTest(telPhone);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeCount = new TimeCount(60000,1000);
                timeCount.start();
                // new Thread(runnable).start();
            }
        });




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    try {
                        if (code.equals("123456")) {
                        //if (SMS.checkCode(code)) {
                            SharedPreferences.Editor editor = getSharedPreferences("loginStatus", MODE_PRIVATE).edit();
                            editor.putBoolean("isLogin", true);
                            editor.apply();
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "验证码输入错误，请重试", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "请阅读并勾选隐私协议后再登录", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityController.finishAll();
    }
}