package com.iesvdc.acceso.orgalife.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.ui.modelview.RegistrarViewModel

class RegistrarActivity : AppCompatActivity() {

    private lateinit var editTextUsuario: EditText
    private lateinit var editTextNombreApellidos: EditText
    private lateinit var editTextCorreo: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextRepeatPassword: EditText
    private lateinit var buttonRegistrar: Button

    // Obtenemos el ViewModel con un delegado
    private val registrarViewModel: RegistrarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        initViews()
        setupObservers()

        buttonRegistrar.setOnClickListener {
            // Llamamos al ViewModel y pasamos los valores
            registrarViewModel.registerUser(
                usuario = editTextUsuario.text.toString(),
                nombreCompleto = editTextNombreApellidos.text.toString(),
                correo = editTextCorreo.text.toString(),
                password = editTextPassword.text.toString(),
                repeatPassword = editTextRepeatPassword.text.toString()
            )
        }
    }

    private fun initViews() {
        editTextUsuario = findViewById(R.id.editTextUsuario)
        editTextNombreApellidos = findViewById(R.id.editTextNombreApellidos)
        editTextCorreo = findViewById(R.id.editTextCorreo)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword)
        buttonRegistrar = findViewById(R.id.buttonRegistrar)
    }

    private fun setupObservers() {
        // Observamos si el registro se completa con éxito
        registrarViewModel.registrationSuccess.observe(this, Observer { success ->
            if (success == true) {
                // Mostrar Toast / Navegar a Login
                showToast("Registro exitoso. Se envió correo de verificación.")
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        })

        // Observamos los errores que puedan surgir
        registrarViewModel.registrationError.observe(this, Observer { error ->
            error?.let {
                showToast(it)
            }
        })

        // Observamos estado de carga
        registrarViewModel.isLoading.observe(this, Observer { loading ->
            buttonRegistrar.isEnabled = !loading
            // Podrías mostrar un ProgressBar, etc.
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
