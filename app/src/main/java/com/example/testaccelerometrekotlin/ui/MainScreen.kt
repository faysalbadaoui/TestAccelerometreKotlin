package com.example.testaccelerometrekotlin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MainScreen(viewModel: AccelerometerViewModel) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth()
                    .background(
                        viewModel.boxColor.collectAsState().value
                    ), contentAlignment = Alignment.Center
            ) {
                Text(text = "1")
            }
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth()
                    .background(
                        Color.Red
                    ), contentAlignment = Alignment.Center
            ) {
                Text(text = viewModel.midBoxText.collectAsState().value)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(
                        Color.Yellow
                    ), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = viewModel.lowText.collectAsState().value,
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
            }
        }
    }
}