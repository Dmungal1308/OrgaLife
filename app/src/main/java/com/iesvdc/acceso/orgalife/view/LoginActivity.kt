package com.iesvdc.acceso.orgalife.view

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.iesvdc.acceso.orgalife.R

class LoginActivity : AppCompatActivity() {
    private var isPasswordVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referencias a los elementos del layout
        val usernameEditText = findViewById<EditText>(R.id.editTextText)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.button)
        val togglePasswordButton = findViewById<ImageButton>(R.id.buttonTogglePassword)

        // Alternar visibilidad de la contraseña
        togglePasswordButton.setOnClickListener {
            if (isPasswordVisible) {
                // Ocultar contraseña
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                togglePasswordButton.setImageResource(R.mipmap.ic_ojo_cerrado) // Cambiar ícono
            } else {
                // Mostrar contraseña
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT
                togglePasswordButton.setImageResource(R.mipmap.ic_ojo_contrasenna)
            }
            isPasswordVisible = !isPasswordVisible
            passwordEditText.setSelection(passwordEditText.text.length)
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username == "root" && password == "1234") {
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
                finish() // Finaliza el LoginActivity
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
