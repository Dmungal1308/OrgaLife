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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnunciosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnunciosBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: AnuncioAdapter

    // Inyectamos el ViewModel con Hilt
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
        binding.root.findViewById<TextView>(R.id.ejercicio2).setOnClickListener {
            startActivity(Intent(this, com.iesvdc.acceso.orgalife.ui.view.EjercicioActivity::class.java))
        }

        // Enlace a MenuActivity
        binding.root.findViewById<TextView>(R.id.textView3).setOnClickListener {
            startActivity(Intent(this, com.iesvdc.acceso.orgalife.ui.view.MenuActivity::class.java))
        }

        // Flecha en menú lateral
        binding.root.findViewById<ImageButton>(R.id.botonFlecha).setOnClickListener {
            toggleDrawer()
        }

        // Cerrar sesión
        binding.root.findViewById<TextView>(R.id.cerrarSesion).setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Barra inferior (puedes personalizar estas acciones según necesites)
        findViewById<ImageButton>(R.id.btnInicio).setOnClickListener {
            startActivity(Intent(this, com.iesvdc.acceso.orgalife.ui.view.MenuActivity::class.java))
        }
        findViewById<ImageButton>(R.id.btnAnuncios).setOnClickListener {
            // Por ejemplo, recargar anuncios o permanecer en esta Activity.
        }
        findViewById<ImageButton>(R.id.btnCerrarSesion).setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun showLogoutConfirmationDialog() {
        val dialog = LogoutConfirmationDialogFragment()
        dialog.onLogoutConfirmed = {
            // Aquí navegas al LoginActivity y cierras la Activity actual
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        dialog.show(supportFragmentManager, "LogoutConfirmationDialog")
    }
}
