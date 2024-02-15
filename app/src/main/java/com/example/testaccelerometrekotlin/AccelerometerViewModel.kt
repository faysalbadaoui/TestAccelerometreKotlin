package com.example.testaccelerometrekotlin

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
    var state : MutableLiveData<String> = MutableLiveData("")
    fun changeValue(newValue: String){
        state.value = newValue
    }

    private val _midBoxText = MutableStateFlow<String>("")
    val midBoxText : StateFlow<String> = _midBoxText.asStateFlow()

    fun changeMidText(newValue: String){
        _midBoxText.value = newValue
    }

    private val _boxColor = MutableStateFlow<Color>(Color.Green)
    val boxColor : StateFlow<Color> = _boxColor.asStateFlow()

    fun setBoxColor(newValue: Color){
        _boxColor.value = newValue
    }
}