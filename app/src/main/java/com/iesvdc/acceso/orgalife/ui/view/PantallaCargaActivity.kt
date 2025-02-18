package com.iesvdc.acceso.orgalife.ui.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.ui.modelview.SplashViewModel

class PantallaCargaActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_carga)

        // Ajustes de la barra de estado, si quieres
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.supeficie)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        // Ajustar insets si tu layout lo necesita
        val rootView = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Observamos el evento de navegación
        splashViewModel.navigateToLoginEvent.observe(this) { shouldNavigate ->
            if (shouldNavigate == true) {
                // Ya podemos navegar a la siguiente pantalla
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}
