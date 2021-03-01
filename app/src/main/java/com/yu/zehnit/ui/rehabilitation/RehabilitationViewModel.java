package com.yu.zehnit.ui.rehabilitation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RehabilitationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RehabilitationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is rehabilitation fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}