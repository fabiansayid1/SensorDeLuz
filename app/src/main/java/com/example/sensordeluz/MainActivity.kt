package com.example.sensordeluz

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var layoutPrincipal: ConstraintLayout
    private lateinit var tvTitulo: TextView
    private lateinit var tvValor: TextView
    private lateinit var tvEstado: TextView

    private lateinit var sensorManager: SensorManager
    private var sensorLuz: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        layoutPrincipal = findViewById(R.id.main)
        tvTitulo = findViewById(R.id.tvTitulo)
        tvValor = findViewById(R.id.tvValor)
        tvEstado = findViewById(R.id.tvEstado)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorLuz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (sensorLuz == null) {
            tvEstado.text = "No se detectó sensor de luz"
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val luz = event.values[0]
            tvValor.text = "Luz: $luz lx"

            if (luz < 50) {
                // Modo Oscuro: Fondo negro y textos blancos
                layoutPrincipal.setBackgroundColor(Color.BLACK)
                tvTitulo.setTextColor(Color.WHITE)
                tvValor.setTextColor(Color.WHITE)
                tvEstado.setTextColor(Color.WHITE)
                tvEstado.text = "Modo Oscuro"

            } else if (luz > 500) {
                layoutPrincipal.setBackgroundColor(Color.WHITE)
                tvTitulo.setTextColor(Color.BLACK)
                tvValor.setTextColor(Color.BLACK)
                tvEstado.setTextColor(Color.BLACK)
                tvEstado.text = "Modo Claro"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onResume() {
        super.onResume()
        sensorLuz?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}