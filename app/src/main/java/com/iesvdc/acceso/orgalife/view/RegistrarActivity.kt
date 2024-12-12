package com.iesvdc.acceso.orgalife.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.iesvdc.acceso.orgalife.R
import java.text.SimpleDateFormat
import java.util.*

class RegistrarActivity : AppCompatActivity() {
    private lateinit var editTextNombre: EditText
    private lateinit var editTextApellidos: EditText
    private lateinit var editTextFechaNacimiento: EditText
    private lateinit var editTextCorreo: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextRepeatPassword: EditText
    private lateinit var auth: FirebaseAuth
    private var fechaNacimiento: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)
        
        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        initViews()
        setupDatePicker()

        findViewById<Button>(R.id.buttonRegistrar).setOnClickListener {
            if (validateFields()) {
                registerUser()
            }
        }
    }

    private fun initViews() {
        editTextNombre = findViewById(R.id.editTextNombre)
        editTextApellidos = findViewById(R.id.editTextApellidos)
        editTextFechaNacimiento = findViewById(R.id.editTextFechaNacimiento)
        editTextCorreo = findViewById(R.id.editTextCorreo)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword)
    }

    private fun setupDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            fechaNacimiento.set(Calendar.YEAR, year)
            fechaNacimiento.set(Calendar.MONTH, month)
            fechaNacimiento.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        editTextFechaNacimiento.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                fechaNacimiento.get(Calendar.YEAR),
                fechaNacimiento.get(Calendar.MONTH),
                fechaNacimiento.get(Calendar.DAY_OF_MONTH)
            ).apply {
                val maxDate = Calendar.getInstance()
                maxDate.add(Calendar.YEAR, -18)
                datePicker.maxDate = maxDate.timeInMillis
                
                val minDate = Calendar.getInstance()
                minDate.add(Calendar.YEAR, -100)
                datePicker.minDate = minDate.timeInMillis
            }.show()
        }
    }

    private fun updateDateInView() {
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        editTextFechaNacimiento.setText(sdf.format(fechaNacimiento.time))
    }

    private fun validateFields(): Boolean {
        val nombre = editTextNombre.text.toString().trim()
        val apellidos = editTextApellidos.text.toString().trim()
        val fechaNac = editTextFechaNacimiento.text.toString().trim()
        val correo = editTextCorreo.text.toString().trim()
        val password = editTextPassword.text.toString()
        val repeatPassword = editTextRepeatPassword.text.toString()

        if (nombre.isEmpty() || apellidos.isEmpty() || fechaNac.isEmpty() || 
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
        val email = editTextCorreo.text.toString().trim()
        val password = editTextPassword.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Enviar email de verificación
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                showToast("Se ha enviado un correo de verificación a $email")
                                // Guardar datos adicionales del usuario en Firestore si lo necesitas
                                saveUserData()
                                // Redirigir al login
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            } else {
                                showToast("Error al enviar el correo de verificación")
                            }
                        }
                } else {
                    showToast("Error en el registro: ${task.exception?.message}")
                }
            }
    }

    private fun saveUserData() {
        val user = auth.currentUser
        user?.let { firebaseUser ->
            val userData = hashMapOf(
                "uid" to firebaseUser.uid,
                "nombre" to editTextNombre.text.toString().trim(),
                "apellidos" to editTextApellidos.text.toString().trim(),
                "fechaNacimiento" to editTextFechaNacimiento.text.toString().trim(),
                "email" to editTextCorreo.text.toString().trim()
            )

            // Aquí puedes guardar los datos adicionales en Firestore si lo necesitas
            // db.collection("users").document(firebaseUser.uid).set(userData)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}