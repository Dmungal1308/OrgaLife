package com.iesvdc.acceso.orgalife.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.adapter.AnuncioAdapter
import com.iesvdc.acceso.orgalife.data.Anuncio
import com.iesvdc.acceso.orgalife.databinding.ActivityAnunciosBinding
import com.iesvdc.acceso.orgalife.databinding.ActivityMenuBinding

class AnunciosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnunciosBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnuncioAdapter
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) Inflas el binding
        binding = ActivityAnunciosBinding.inflate(layoutInflater)

        // 2) Estableces la vista raíz del binding como contenido
        setContentView(binding.root)

        // 3) Ya puedes usar binding.* sin problema
        drawerLayout = binding.drawerLayout

        // Ajustes de la barra de estado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.supeficie)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        // RecyclerView
        binding.recyclerViewAnuncios.layoutManager = LinearLayoutManager(this)

        // Ajustes para la barra de sistema (ya que binding.main existe)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val anuncios = listOf(
            Anuncio("Oferta especial en productos ecológicos"),
            Anuncio("Nuevo evento de bienestar el próximo sábado"),
            Anuncio("Descuento del 20% en suscripción premium")
        )

        adapter = AnuncioAdapter(anuncios)
        binding.recyclerViewAnuncios.adapter = adapter

        // Botón hamburguesa
        binding.imageButton.setOnClickListener {
            toggleDrawer()
        }

        val ejercicio = binding.root.findViewById<TextView>(R.id.ejercicio2)
        ejercicio.setOnClickListener {
            val intent = Intent(this, EjercicioActivity::class.java) // Crear intención para iniciar EjercicioActivity
            startActivity(intent) // Iniciar la nueva actividad
        }
        val inicio = binding.root.findViewById<TextView>(R.id.textView3)
        inicio.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java) // Crear intención para iniciar MenuActivity
            startActivity(intent) // Iniciar la nueva actividad
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

        // (Si tu layout tuviese ejercicio2, aquí lo configuras)
        // val ejercicio = binding.root.findViewById<TextView>(R.id.ejercicio2)
        // ejercicio.setOnClickListener { ... }

        // Botón de Anuncios (barra inferior)
        val btnAnuncios = findViewById<ImageButton>(R.id.btnAnuncios)
        btnAnuncios.setOnClickListener {
            // volver a Anuncios o recargar, etc.
            // startActivity(Intent(this, AnunciosActivity::class.java))
        }
        val btnInicio = findViewById<ImageButton>(R.id.btnInicio)
        btnInicio.setOnClickListener {
            // Navegar a AnunciosActivity
            startActivity(Intent(this, MenuActivity::class.java))
        }

        // Botón de Cerrar Sesión (barra inferior)
        val btnCerrarSesion = findViewById<ImageButton>(R.id.btnCerrarSesion)
        btnCerrarSesion.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START) // Cerrar el menú
        } else {
            drawerLayout.openDrawer(GravityCompat.START) // Abrir el menú
        }
    }

    // Metodo para mostrar el diálogo de confirmación de cierre de sesión
    private fun showLogoutConfirmationDialog() {
        val dialog = LogoutConfirmationDialogFragment { confirmed ->
            if (confirmed) {
                // Limpiar SharedPreferences para terminar la sesión
                val sharedPreferences = getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    remove("isLoggedIn") // Eliminar la marca de sesión iniciada
                    apply() // Aplicar cambios
                }

                // Redirigir al login
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent) // Iniciar la actividad de inicio de sesión
                finish() // Cerrar la actividad actual
            }
        }
        dialog.show(supportFragmentManager, "LogoutConfirmationDialog") // Mostrar el diálogo
    }
}
