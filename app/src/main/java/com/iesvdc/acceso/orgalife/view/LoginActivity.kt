package com.iesvdc.acceso.orgalife.view

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.iesvdc.acceso.orgalife.R

class LoginActivity : AppCompatActivity() {
    private lateinit var editTextUsuario: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonRegistrar: Button
    private lateinit var buttonTogglePassword: ImageButton
    private lateinit var textViewForgotPassword: TextView
    private lateinit var auth: FirebaseAuth
    private var passwordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        initViews()
        setupListeners()
    }

    private fun initViews() {
        try {
            editTextUsuario = findViewById(R.id.editTextUsuario) 
                ?: throw NullPointerException("EditText Usuario no encontrado")
            editTextPassword = findViewById(R.id.editTextPassword)
                ?: throw NullPointerException("EditText Password no encontrado")
            buttonLogin = findViewById(R.id.buttonLogin)
                ?: throw NullPointerException("Button Login no encontrado")
            buttonRegistrar = findViewById(R.id.buttonRegistrar)
                ?: throw NullPointerException("Button Registrar no encontrado")
            buttonTogglePassword = findViewById(R.id.buttonTogglePassword)
                ?: throw NullPointerException("Button Toggle Password no encontrado")
            textViewForgotPassword = findViewById(R.id.textViewForgotPassword)
                ?: throw NullPointerException("TextView Forgot Password no encontrado")
        } catch (e: Exception) {
            showToast("Error al inicializar vistas: ${e.message}")
            finish()
        }
    }

    private fun setupListeners() {
        buttonTogglePassword.setOnClickListener {
            passwordVisible = !passwordVisible
            togglePasswordVisibility()
        }

        buttonLogin.setOnClickListener {
            val email = editTextUsuario.text.toString().trim()
            val password = editTextPassword.text.toString()

            // Verificar si es el usuario admin
            if (email == "admin" && password == "1234") {
                startActivity(Intent(this, MenuActivity::class.java))
                finish()
            } else {
                loginUser(email, password)
            }
        }

        buttonRegistrar.setOnClickListener {
            startActivity(Intent(this, RegistrarActivity::class.java))
        }

        textViewForgotPassword.setOnClickListener {
            val email = editTextUsuario.text.toString().trim()
            if (email.isEmpty()) {
                showToast("Por favor, ingresa tu correo electrónico")
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast("Por favor, ingresa un correo válido")
                return@setOnClickListener
            }

            // Deshabilitar el botón mientras se procesa
            textViewForgotPassword.isEnabled = false

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    // Volver a habilitar el botón
                    textViewForgotPassword.isEnabled = true
                    
                    if (task.isSuccessful) {
                        showToast("Se ha enviado un correo para restablecer tu contraseña")
                    } else {
                        val errorMessage = when (task.exception) {
                            is FirebaseAuthInvalidUserException -> 
                                "No existe una cuenta con este correo"
                            else -> "Error al enviar el correo: ${task.exception?.message}"
                        }
                        showToast(errorMessage)
                    }
                }
        }
    }

    private fun togglePasswordVisibility() {
        if (passwordVisible) {
            // Mostrar contraseña
            editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            buttonTogglePassword.setImageResource(R.mipmap.ic_ojo_contrasenna_foreground)
        } else {
            // Ocultar contraseña
            editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            buttonTogglePassword.setImageResource(R.mipmap.ic_ojo_cerrado_foreground)
        }
        // Mantener el cursor al final del texto
        editTextPassword.setSelection(editTextPassword.text.length)
    }

    private fun loginUser(email: String, password: String) {
        if (validateFields()) {
            // Deshabilitar el botón de login mientras se procesa
            buttonLogin.isEnabled = false

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    // Volver a habilitar el botón
                    buttonLogin.isEnabled = true
                    
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null && user.isEmailVerified) {
                            // Email verificado, permitir acceso
                            startActivity(Intent(this, MenuActivity::class.java))
                            finish()
                        } else {
                            showToast("Por favor, verifica tu correo electrónico")
                            // Opcionalmente, reenviar email de verificación
                            user?.sendEmailVerification()
                        }
                    } else {
                        // Mostrar error específico
                        val errorMessage = when (task.exception) {
                            is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos"
                            is FirebaseAuthInvalidUserException -> "No existe una cuenta con este correo"
                            else -> "Error en el inicio de sesión: ${task.exception?.message}"
                        }
                        showToast(errorMessage)
                    }
                }
        }
    }

    private fun validateFields(): Boolean {
        val email = editTextUsuario.text.toString().trim()
        val password = editTextPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            showToast("Por favor, completa todos los campos")
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Por favor, ingresa un correo válido")
            return false
        }

        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
