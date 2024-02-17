package com.example.testaccelerometrekotlin

import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import com.example.testaccelerometrekotlin.ui.AccelerometerViewModel
import com.example.testaccelerometrekotlin.domain.SensorConstants
import com.example.testaccelerometrekotlin.domain.SensorDelay
import com.example.testaccelerometrekotlin.ui.MainScreen
import kotlin.math.abs

class TestAccelerometreActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var lastUpdate: Long = 0
    private var lastUpdate2: Long = 0
    private val viewModel : AccelerometerViewModel by viewModels()

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
            MainScreen(viewModel)
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

        if (actualTime - lastUpdate2 < SensorDelay.DEFAULT) {
            return
        }
        if ( abs(event.values[0] - viewModel.lastValue.value) > SensorConstants.LIGHT) {
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
        if (accelerationSquareRoot >= SensorConstants.ACCELEROMETER) {
            if (actualTime - lastUpdate < SensorDelay.DEFAULT) {
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

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }
    override fun onPause() {
        // unregister listener
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }
}
