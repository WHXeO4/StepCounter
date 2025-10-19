package com.WHXeO46.github.stepcounter.ui.welcome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WelcomeViewModel extends ViewModel {
    private MutableLiveData<String> _text;
    private LiveData<String> text;

    public WelcomeViewModel() {
        _text = new MutableLiveData<>();
        _text.setValue("Have fun with this step counter, Ciallo~");
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
