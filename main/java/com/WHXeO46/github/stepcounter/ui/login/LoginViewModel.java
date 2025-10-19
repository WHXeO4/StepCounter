package com.WHXeO46.github.stepcounter.ui.login;

import android.view.ViewGroup;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<String> _text;
    public final LiveData<String> text;

    public LoginViewModel() {
        _text = new MutableLiveData<>();
        _text.setValue("Welcome");
        text = _text;
    }

    public LiveData<String> getText() {
        return text;
    }

    public MutableLiveData<String> getTextMutable() {
        return _text;
    }

    public void setText(String s) {
        _text.setValue(s);
    }
}
