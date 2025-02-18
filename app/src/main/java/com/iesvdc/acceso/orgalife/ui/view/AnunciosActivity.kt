package com.iesvdc.acceso.orgalife.ui.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.ui.adapter.AnuncioAdapter
import com.iesvdc.acceso.orgalife.databinding.ActivityAnunciosBinding
import com.iesvdc.acceso.orgalife.ui.modelview.AnunciosViewModel

class AnunciosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnunciosBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: AnuncioAdapter

    // Obtenemos el ViewModel de anuncios
    private val anunciosViewModel: AnunciosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnunciosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout

        // Ajustes de la barra de estado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.supeficie)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        // Ajustes para la barra de sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configuramos el RecyclerView con LayoutManager
        binding.recyclerViewAnuncios.layoutManager = LinearLayoutManager(this)

        // Instanciamos el adapter con una lista vacía inicial
        adapter = AnuncioAdapter(emptyList())
        binding.recyclerViewAnuncios.adapter = adapter

        // Observamos el LiveData de anuncios
        anunciosViewModel.anuncios.observe(this, Observer { anunciosList ->
            adapter.setAnuncios(anunciosList)
        })

        // Botón hamburguesa
        binding.imageButton.setOnClickListener {
            toggleDrawer()
        }

        // Enlace a EjercicioActivity
        val ejercicio = binding.root.findViewById<TextView>(R.id.ejercicio2)
        ejercicio.setOnClickListener {
            startActivity(Intent(this, EjercicioActivity::class.java))
        }

        // Enlace a MenuActivity
        val inicio = binding.root.findViewById<TextView>(R.id.textView3)
        inicio.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        // Flecha en menú lateral
        val botonFlecha = binding.root.findViewById<ImageButton>(R.id.botonFlecha)
        botonFlecha.setOnClickListener {
            toggleDrawer()
        }

        // Cerrar Sesión
        val cerrarSesion = binding.root.findViewById<TextView>(R.id.cerrarSesion)
        cerrarSesion.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Barra inferior
        val btnAnuncios = findViewById<ImageButton>(R.id.btnAnuncios)
        btnAnuncios.setOnClickListener {
            // Si quisieras recargar anuncios, o quedarte en esta pantalla
        }
        val btnInicio = findViewById<ImageButton>(R.id.btnInicio)
        btnInicio.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
        val btnCerrarSesion = findViewById<ImageButton>(R.id.btnCerrarSesion)
        btnCerrarSesion.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun showLogoutConfirmationDialog() {
        // En un enfoque "full MVVM",
        // aquí podrías llamar a un metodo del ViewModel, o
        // usar un LogoutConfirmationDialog que llame a logoutEvent.
        LogoutConfirmationDialogFragment().show(supportFragmentManager, "LogoutConfirmationDialog")
    }
}
