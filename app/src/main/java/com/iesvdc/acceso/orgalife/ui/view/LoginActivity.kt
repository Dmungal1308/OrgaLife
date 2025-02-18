package com.iesvdc.acceso.orgalife.ui.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.ui.modelview.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextUsuario: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonRegistrar: Button
    private lateinit var buttonTogglePassword: ImageButton
    private lateinit var textViewForgotPassword: TextView

    private var passwordVisible = false

    /**
     * Usamos la forma 'by viewModels()' para obtener el ViewModel con
     * el ViewModelProvider. (Necesita la dependencia de KTX).
     */
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cambiar color de la StatusBar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.supeficie)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        // Verificar si la sesión ya está iniciada (a través del ViewModel).
        if (loginViewModel.isLoggedIn()) {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
            return
        }

        // Inflamos el layout
        setContentView(R.layout.activity_login)

        // Inicializar vistas
        initViews()

        // Configurar Listeners
        setupListeners()

        // Observadores del ViewModel
        setupObservers()
    }

    private fun initViews() {
        editTextUsuario = findViewById(R.id.editTextUsuario)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonRegistrar = findViewById(R.id.buttonRegistrar)
        buttonTogglePassword = findViewById(R.id.buttonTogglePassword)
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword)
    }

    private fun setupListeners() {
        // Botón para mostrar/ocultar contraseña
        buttonTogglePassword.setOnClickListener {
            passwordVisible = !passwordVisible
            togglePasswordVisibility()
        }

        // Botón de inicio de sesión
        buttonLogin.setOnClickListener {
            val email = editTextUsuario.text.toString().trim()
            val password = editTextPassword.text.toString()

            // Comprobación rápida para credenciales hard-coded
            if (email == "admin" && password == "1234") {
                loginViewModel.saveSession()
                startActivity(Intent(this, MenuActivity::class.java))
                finish()
            } else {
                // Llamamos al ViewModel para hacer el login con Firebase
                loginViewModel.loginUser(email, password)
            }
        }

        // Botón de registro
        buttonRegistrar.setOnClickListener {
            startActivity(Intent(this, RegistrarActivity::class.java))
        }

        // Olvidé mi contraseña
        textViewForgotPassword.setOnClickListener {
            val email = editTextUsuario.text.toString().trim()
            loginViewModel.resetPassword(email)
        }
    }

    /**
     * Observamos los LiveData expuestos por el ViewModel
     * para actualizar la UI o navegar en consecuencia.
     */
    private fun setupObservers() {
        // Observa si el login ha tenido éxito
        loginViewModel.loginSuccess.observe(this, Observer { success ->
            // success puede ser true o false.
            if (success == true) {
                // Guardamos la sesión y navegamos
                loginViewModel.saveSession()
                startActivity(Intent(this, MenuActivity::class.java))
                finish()
            }
        })

        // Observa si el correo no está verificado
        loginViewModel.emailNotVerified.observe(this, Observer { notVerified ->
            if (notVerified == true) {
                showToast("Por favor, verifica tu correo electrónico. Se ha reenviado el mail.")
                // Aquí podrías re-enviar el correo si quieres
            }
        })

        // Observa el mensaje de error (o info) que tengamos
        loginViewModel.loginErrorMessage.observe(this, Observer { message ->
            if (!message.isNullOrEmpty()) {
                showToast(message)
            }
        })
    }

    /**
     * Muestra u oculta la contraseña de acuerdo al valor booleano 'passwordVisible'.
     */
    private fun togglePasswordVisibility() {
        if (passwordVisible) {
            editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            buttonTogglePassword.setImageResource(R.mipmap.ic_ojo_contrasenna_foreground)
        } else {
            editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_PASSWORD
            buttonTogglePassword.setImageResource(R.mipmap.ic_ojo_cerrado_foreground)
        }
        editTextPassword.setSelection(editTextPassword.text.length)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
