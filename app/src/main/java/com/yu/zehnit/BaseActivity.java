package com.yu.zehnit;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.yu.zehnit.tools.MyContextWrapper;
import com.yu.zehnit.tools.SharedPreferencesUtils;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "dxx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale newLocale;

        // 系统语言
        String localLang = Locale.getDefault().getLanguage();
        Log.d(TAG, "attachBaseContext:-------------------------------------------------------------------- " + localLang);

        SharedPreferencesUtils.setFileName("info");
        String language = (String) SharedPreferencesUtils.getParam(newBase, "language", localLang);

        newLocale = new Locale(language);

        Context context = MyContextWrapper.wrap(newBase, newLocale);
        super.attachBaseContext(context);
    }

}
