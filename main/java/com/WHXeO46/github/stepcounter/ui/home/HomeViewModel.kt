package com.WHXeO46.github.stepcounter.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.WHXeO46.github.stepcounter.data.StepService

class HomeViewModel : ViewModel() {

    private var step: Int = 0
    private val _text = MutableLiveData<String>().apply {
        value = StepService.getStep().toString()
    }
    val text: LiveData<String> = _text

    public fun updateValue(value: Int) {
        step = value
        _text.value = step.toString()
    }
}