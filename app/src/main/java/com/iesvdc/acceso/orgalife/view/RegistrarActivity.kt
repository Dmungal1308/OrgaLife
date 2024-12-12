package com.iesvdc.acceso.orgalife.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.iesvdc.acceso.orgalife.R

class RegistrarActivity : AppCompatActivity() {
    private lateinit var editTextUsuario: EditText
    private lateinit var editTextNombreApellidos: EditText
    private lateinit var editTextCorreo: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextRepeatPassword: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var buttonRegistrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)
        
        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        initViews()

        findViewById<Button>(R.id.buttonRegistrar).setOnClickListener {
            if (validateFields()) {
                registerUser()
            }
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

    private fun validateFields(): Boolean {
        val usuario = editTextUsuario.text.toString().trim()
        val nombreApellidos = editTextNombreApellidos.text.toString().trim()
        val correo = editTextCorreo.text.toString().trim()
        val password = editTextPassword.text.toString()
        val repeatPassword = editTextRepeatPassword.text.toString()

        if (usuario.isEmpty() || nombreApellidos.isEmpty() || 
            correo.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            showToast("Todos los campos son obligatorios")
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            showToast("Formato de correo inválido")
            return false
        }

        if (password != repeatPassword) {
            showToast("Las contraseñas no coinciden")
            return false
        }

        if (password.length < 6) {
            showToast("La contraseña debe tener al menos 6 caracteres")
            return false
        }

        return true
    }

    private fun registerUser() {
        if (validateFields()) {
            val email = editTextCorreo.text.toString().trim()
            val password = editTextPassword.text.toString()

            // Deshabilitar el botón de registro para evitar múltiples clicks
            buttonRegistrar.isEnabled = false

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Enviar email de verificación
                        auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener { verificationTask ->
                                if (verificationTask.isSuccessful) {
                                    // Guardar datos del usuario
                                    saveUserData()
                                    
                                    showToast("Se ha enviado un correo de verificación a $email")
                                    
                                    // Redirigir al login
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                } else {
                                    showToast("Error al enviar el correo de verificación: ${verificationTask.exception?.message}")
                                }
                                // Volver a habilitar el botón
                                buttonRegistrar.isEnabled = true
                            }
                    } else {
                        // Volver a habilitar el botón
                        buttonRegistrar.isEnabled = true
                        
                        // Mostrar error específico
                        val errorMessage = when (task.exception) {
                            is FirebaseAuthWeakPasswordException -> "La contraseña debe tener al menos 6 caracteres"
                            is FirebaseAuthInvalidCredentialsException -> "El correo electrónico no es válido"
                            is FirebaseAuthUserCollisionException -> "Ya existe una cuenta con este correo electrónico"
                            else -> "Error en el registro: ${task.exception?.message}"
                        }
                        showToast(errorMessage)
                    }
                }
        }
    }

    private fun saveUserData() {
        val user = auth.currentUser
        user?.let { firebaseUser ->
            val userData = hashMapOf(
                "uid" to firebaseUser.uid,
                "usuario" to editTextUsuario.text.toString().trim(),
                "nombreCompleto" to editTextNombreApellidos.text.toString().trim(),
                "email" to editTextCorreo.text.toString().trim()
            )

            // Guardar en Firestore
            val db = FirebaseFirestore.getInstance()
            db.collection("usuarios")
                .document(firebaseUser.uid)
                .set(userData)
                .addOnSuccessListener {
                    showToast("Datos guardados correctamente")
                }
                .addOnFailureListener { e ->
                    showToast("Error al guardar datos: ${e.message}")
                }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}