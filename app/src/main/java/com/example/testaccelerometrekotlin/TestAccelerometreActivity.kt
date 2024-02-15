package com.example.testaccelerometrekotlin

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.testaccelerometrekotlin.databinding.MainBinding
import kotlin.math.abs

class TestAccelerometreActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var color = false
    private lateinit var view: TextView
    private var lastUpdate: Long = 0
    private var lastUpdate2: Long = 0
    private val viewModel : AccelerometerViewModel by viewModels()
    private lateinit var binding: MainBinding

    @SuppressLint("CoroutineCreationDuringComposition")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val hasAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null
        if(hasAccelerometer){
            viewModel.changeMidText("Shake to get a toast and to switch color")
            sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }else{
            viewModel.changeMidText("Sorry there Is no Accelerometer in your device")
        }
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        if(lightSensor != null){
            viewModel.changeLowText("Max Range: ${lightSensor.maximumRange}")
            sensorManager.registerListener(
                this,
                lightSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }else{
            viewModel.changeLowText("Sorry there Is no Light sensor in your device")
        }

        lastUpdate = System.currentTimeMillis()
        lastUpdate2 = System.currentTimeMillis()
        setContent {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth()
                        .background(
                            viewModel.boxColor.collectAsState().value
                        ), contentAlignment = Alignment.Center) {
                        Text(text ="1")
                    }
                    Box(modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth()
                        .background(
                            Color.Red
                        ), contentAlignment = Alignment.Center) {
                        Text(text = viewModel.midBoxText.collectAsState().value)
                    }
                    Box(modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(
                            Color.Yellow
                        ), contentAlignment = Alignment.Center) {

                        Text(text =viewModel.lowText.collectAsState().value, modifier = Modifier.verticalScroll(rememberScrollState()))
                    }
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if(event.sensor.type == Sensor.TYPE_LIGHT) {
            getLight(event)
        }else{
            getAccelerometer(event)
        }
    }
    private fun getLight(event: SensorEvent) {
        val maxLight = event.sensor.maximumRange
        val low = maxLight / 3
        val high = maxLight * (2 / 3)
        val actualTime = System.currentTimeMillis()

        if (actualTime - lastUpdate2 < 1000) {
            return
        }
        if ( abs(event.values[0] - viewModel.lastValue.value) > 200) {
            lastUpdate2 = actualTime
            viewModel.changeLowText("New Value Light Sensor = ${event.values[0]}")
            if (event.values[0] < low) {
                viewModel.changeLowText("Low Intensity")
            } else if (event.values[0] > high) {
                viewModel.changeLowText("High Intensity")
            } else {
                viewModel.changeLowText("Normal Intensity")
            }
            viewModel.changeLastValue(event.values[0])
        }
    }
    private fun getAccelerometer(event: SensorEvent) {
        val values = event.values
        // Movement
        val x = values[0]
        val y = values[1]
        val z = values[2]
        val accelerationSquareRoot = ((x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH))
        val actualTime = System.currentTimeMillis()
        if (accelerationSquareRoot >= 1.1) {
            if (actualTime - lastUpdate < 1000) {
                return
            }
            lastUpdate = actualTime
            if(viewModel.boxColor.value == Color.Green)
                viewModel.setBoxColor(Color.Cyan)
            else
                viewModel.setBoxColor(Color.Green)
            Toast.makeText(this, R.string.shuffed, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onPause() {
        // unregister listener
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}
