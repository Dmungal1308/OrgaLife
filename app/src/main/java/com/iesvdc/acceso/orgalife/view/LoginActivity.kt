package com.iesvdc.acceso.orgalife.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.iesvdc.acceso.orgalife.R

// Clase principal de la actividad de inicio de sesión
class LoginActivity : AppCompatActivity() {
    // Declaración de variables para los elementos de la interfaz
    private lateinit var editTextUsuario: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonRegistrar: Button
    private lateinit var buttonTogglePassword: ImageButton
    private lateinit var textViewForgotPassword: TextView
    private lateinit var auth: FirebaseAuth
    private var passwordVisible = false // Controla la visibilidad de la contraseña

    // Metodo que se llama al crear la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configuración de la barra de estado para dispositivos con Android M o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.supeficie)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        // Verificar si la sesión ya está iniciada
        val sharedPreferences = getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            // Si ya está logueado, redirigir a la actividad del menú
            startActivity(Intent(this, MenuActivity::class.java))
            finish() // Finaliza la actividad actual
            return
        }

        setContentView(R.layout.activity_login) // Establece el diseño de la actividad

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        initViews() // Inicializa las vistas
        setupListeners() // Configura los listeners para los botones
    }

    // Metodo para inicializar las vistas
    private fun initViews() {
        editTextUsuario = findViewById(R.id.editTextUsuario)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonRegistrar = findViewById(R.id.buttonRegistrar)
        buttonTogglePassword = findViewById(R.id.buttonTogglePassword)
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword)
    }

    // Metodo para configurar los listeners de los botones
    private fun setupListeners() {
        // Listener para el botón de mostrar/ocultar contraseña
        buttonTogglePassword.setOnClickListener {
            passwordVisible = !passwordVisible
            togglePasswordVisibility() // Cambia la visibilidad de la contraseña
        }

        // Listener para el botón de inicio de sesión
        buttonLogin.setOnClickListener {
            val email = editTextUsuario.text.toString().trim()
            val password = editTextPassword.text.toString()

            // Verifica si las credenciales son las predeterminadas
            if (email == "admin" && password == "1234") {
                saveSession() // Guarda la sesión
                startActivity(Intent(this, MenuActivity::class.java)) // Redirige al menú
                finish() // Finaliza la actividad actual
            } else {
                loginUser(email, password) // Intenta iniciar sesión con Firebase
            }
        }

        // Listener para el botón de registro
        buttonRegistrar.setOnClickListener {
            startActivity(Intent(this, RegistrarActivity::class.java)) // Redirige a la actividad de registro
        }

        // Listener para el enlace de "Olvidé mi contraseña"
        textViewForgotPassword.setOnClickListener {
            val email = editTextUsuario.text.toString().trim()
            if (email.isEmpty()) {
                showToast("Por favor, ingresa tu correo electrónico") // Mensaje de error si el campo está vacío
                return@setOnClickListener
            }

            // Verifica si el correo tiene un formato válido
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast("Por favor, ingresa un correo válido") // Mensaje de error si el formato es incorrecto
                return@setOnClickListener
            }

            // Envía un correo para restablecer la contraseña
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Se ha enviado un correo para restablecer tu contraseña") // Mensaje de éxito
                } else {
                    // Manejo de errores al enviar el correo
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "No existe una cuenta con este correo"
                        else -> "Error al enviar el correo: ${task.exception?.message}"
                    }
                    showToast(errorMessage) // Muestra el mensaje de error
                }
            }
        }
    }

    // Metodo para alternar la visibilidad de la contraseña
    private fun togglePasswordVisibility() {
        if (passwordVisible) {
            editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            buttonTogglePassword.setImageResource(R.mipmap.ic_ojo_contrasenna_foreground) // Cambia al ícono de ojo abierto
        } else {
            editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            buttonTogglePassword.setImageResource(R.mipmap.ic_ojo_cerrado_foreground) // Cambia al ícono de ojo cerrado
        }
        editTextPassword.setSelection(editTextPassword.text.length) // Mantiene el cursor al final del texto
    }

    // Metodo para iniciar sesión con Firebase
    private fun loginUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        saveSession() // Guarda la sesión
                        startActivity(Intent(this, MenuActivity::class.java)) // Redirige al menú
                        finish() // Finaliza la actividad actual
                    } else {
                        showToast("Por favor, verifica tu correo electrónico") // Mensaje si el correo no está verificado
                        user?.sendEmailVerification() // Envía un correo de verificación
                    }
                } else {
                    showToast("Correo o contraseña incorrectos") // Mensaje de error si las credenciales son incorrectas
                }
            }
        } else {
            showToast("Por favor, completa todos los campos") // Mensaje si los campos están vacíos
        }
    }

    // Metodo para guardar la sesión del usuario
    private fun saveSession() {
        val sharedPreferences = getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", true) // Marca la sesión como iniciada
            apply() // Aplica los cambios
        }
    }

    // Metodo para mostrar mensajes emergentes (Toast)
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show() // Muestra el mensaje
    }
}