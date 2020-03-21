package com.pes.fibness.ui.entrenamientos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EntrenamientosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EntrenamientosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}