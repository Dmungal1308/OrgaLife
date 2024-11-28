package com.iesvdc.acceso.orgalife.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.iesvdc.acceso.orgalife.R

class PantallaCargaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_carga)

        // Navegar a la actividad principal después de un pequeño retraso
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@PantallaCargaActivity, LoginActivity::class.java)
            startActivity(intent)
            finish() // Finaliza la actividad Splash
        }, 20) // Retraso de 2 segundos
    }
}