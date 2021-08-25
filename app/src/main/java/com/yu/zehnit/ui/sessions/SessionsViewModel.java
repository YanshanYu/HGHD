package com.yu.zehnit.ui.sessions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SessionsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SessionsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is rehabilitation fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}