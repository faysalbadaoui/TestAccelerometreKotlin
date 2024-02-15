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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.testaccelerometrekotlin.databinding.MainBinding

class TestAccelerometreActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var color = false
    private lateinit var view: TextView
    private var lastUpdate: Long = 0
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

        lastUpdate = System.currentTimeMillis()
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
                            androidx.compose.ui.graphics.Color.Red
                        ), contentAlignment = Alignment.Center) {
                        Text(text = viewModel.midBoxText.collectAsState().value)
                    }
                    Box(modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(
                            androidx.compose.ui.graphics.Color.Blue
                        ), contentAlignment = Alignment.Center) {
                        Text(text ="3")
                    }
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        getAccelerometer(event)
    }

    private fun getAccelerometer(event: SensorEvent) {
        val values = event.values
        // Movement
        val x = values[0]
        val y = values[1]
        val z = values[2]
        val accelerationSquareRoot = (x * x + y * y + z * z
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH))
        val actualTime = System.currentTimeMillis()
        if (accelerationSquareRoot >= 100) {
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
