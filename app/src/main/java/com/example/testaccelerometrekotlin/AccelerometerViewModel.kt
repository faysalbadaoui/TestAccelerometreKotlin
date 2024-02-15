package com.example.testaccelerometrekotlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AccelerometerViewModel : ViewModel() {
    var state : MutableLiveData<String> = MutableLiveData("")
}