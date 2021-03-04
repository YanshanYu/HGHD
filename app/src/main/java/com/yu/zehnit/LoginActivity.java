package com.yu.zehnit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

    private EditText editPhoneNumber;
    private EditText editNumber;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intent = new Intent(LoginActivity.this, MainActivity.class);

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        Boolean isLogin = pref.getBoolean("isLogin", false);
        if (isLogin) startActivity(intent);

        editPhoneNumber = findViewById(R.id.edit_phone_number);
        editNumber = findViewById(R.id.edit__number);
        Button btnLogin = findViewById(R.id.button11);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editNumber.getText().toString().equals("123456")) {
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
}