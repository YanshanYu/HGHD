package com.yu.zehnit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;

import com.gyf.immersionbar.ImmersionBar;

public class AddEquipmentActivity extends BaseActivity {

    private Toolbar toolbar;
    private Button btnAdd;
    private EditText textSn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_equipment);

        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true)
                .fitsSystemWindows(true).init();

        toolbar = findViewById(R.id.toolbar_add);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAdd = findViewById(R.id.button_add);
        textSn = findViewById(R.id.editText_sn);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textSn.getText().toString().equals("123456")) {
                    progressDialog = new ProgressDialog(AddEquipmentActivity.this);
                    progressDialog.setTitle("提示");
                    progressDialog.setMessage("正在添加设备...");
                    progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog.dismiss();
                        }
                    });
                    progressDialog.show();
//                    Thread t = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Thread.sleep(3000);//让他显示10秒后，取消ProgressDialog
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            progressDialog.dismiss();
//
//
//                        }
//                    });
//                    t.start();
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            //do...

                            try {
                                Thread.sleep(2000);//显示3秒后，取消ProgressDialog
                                SharedPreferences pref = getSharedPreferences("eqpNum", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                int count = pref.getInt("num", 0);
                                editor.putInt("num", count + 1);
                                editor.apply();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Message message = new Message();
                            message.what = 0;
                            mHandler.sendMessage(message);
                        }
                    });
                    thread.start();

                }
            }
        });

    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            finish();
            if (msg.what == 0) {
                progressDialog.dismiss();
            }
        }
    };

}