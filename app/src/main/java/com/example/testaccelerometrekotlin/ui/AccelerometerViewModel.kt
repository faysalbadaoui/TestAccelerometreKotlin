package com.example.testaccelerometrekotlin.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AccelerometerViewModel : ViewModel() {

    private val _midBoxText = MutableStateFlow<String>("")
    val midBoxText : StateFlow<String> = _midBoxText.asStateFlow()

    private val _lowText = MutableStateFlow<String>("")
    val lowText : StateFlow<String> = _lowText.asStateFlow()

    private val _lastValue = MutableStateFlow<Float>(0f)
    val lastValue : StateFlow<Float> = _lastValue.asStateFlow()

    fun changeLastValue(newValue: Float){
        _lastValue.value = newValue
    }
    fun changeLowText(newValue: String){
        _lowText.value = _lowText.value + "\n" + newValue
    }
    fun changeMidText(newValue: String){
        _midBoxText.value = newValue
    }

    private val _boxColor = MutableStateFlow<Color>(Color.Green)
    val boxColor : StateFlow<Color> = _boxColor.asStateFlow()

    fun setBoxColor(newValue: Color){
        _boxColor.value = newValue
    }
}