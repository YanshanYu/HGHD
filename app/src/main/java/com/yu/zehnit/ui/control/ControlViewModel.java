package com.yu.zehnit.ui.control;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ControlViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ControlViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is control fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}