package com.lans.composeresult

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lans.composeresult.hilt.simple.HiltMainActivity
import com.lans.composeresult.simple.SimpleActivity
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    startActivity(Intent(this@MainActivity, SimpleActivity::class.java))
                }) {
                    Text("compose")
                }

                Button(onClick = {
                    startActivity(Intent(this@MainActivity, HiltMainActivity::class.java))
                }) {
                    Text("hilt-viemodel")
                }
            }
        }
    }
}