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

    private val anunciosViewModel: AnunciosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnunciosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.supeficie)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.recyclerViewAnuncios.layoutManager = LinearLayoutManager(this)

        adapter = AnuncioAdapter(emptyList())
        binding.recyclerViewAnuncios.adapter = adapter

        anunciosViewModel.anuncios.observe(this, Observer { anunciosList ->
            adapter.setAnuncios(anunciosList)
        })

        binding.imageButton.setOnClickListener {
            toggleDrawer()
        }

        binding.root.findViewById<TextView>(R.id.ejercicio2).setOnClickListener {
            startActivity(Intent(this, com.iesvdc.acceso.orgalife.ui.view.EjercicioActivity::class.java))
        }

        binding.root.findViewById<TextView>(R.id.textView3).setOnClickListener {
            startActivity(Intent(this, com.iesvdc.acceso.orgalife.ui.view.MenuActivity::class.java))
        }

        binding.root.findViewById<ImageButton>(R.id.botonFlecha).setOnClickListener {
            toggleDrawer()
        }

        binding.root.findViewById<TextView>(R.id.cerrarSesion).setOnClickListener {
            showLogoutConfirmationDialog()
        }

        findViewById<ImageButton>(R.id.btnInicio).setOnClickListener {
            startActivity(Intent(this, com.iesvdc.acceso.orgalife.ui.view.MenuActivity::class.java))
        }
        findViewById<ImageButton>(R.id.btnAnuncios).setOnClickListener {
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
        val logoutDialog = LogoutConfirmationDialogFragment()
        logoutDialog.onLogoutConfirmed = {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        logoutDialog.show(supportFragmentManager, "LogoutConfirmationDialog")
    }

}
