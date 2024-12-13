package com.iesvdc.acceso.orgalife.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.iesvdc.acceso.orgalife.R

// Clase para el dialogo de confirmacion de cierre de sesion
class LogoutConfirmationDialogFragment(private val onConfirm: (Boolean) -> Unit) : DialogFragment() {

    // Metodo que crea el dialogo
    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Cerrar Sesion")
            .setMessage("¿Estas seguro de que deseas cerrar sesion?")
            .setPositiveButton("Si") { _, _ -> onConfirm(true) }
            .setNegativeButton("No") { _, _ -> onConfirm(false) }

        val dialog = builder.create()

        // Configurar el color de fondo del dialogo
        dialog.setOnShowListener {
            dialog.window?.setBackgroundDrawableResource(R.color.superficieContenedorAlta)

            // Cambiar el color del titulo
            val titleId = resources.getIdentifier("alertTitle", "id", "android")
            val textViewTitle = dialog.findViewById<TextView>(titleId)
            textViewTitle?.setTextColor(resources.getColor(R.color.primario))

            // Cambiar el color de los botones
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(resources.getColor(R.color.error))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(resources.getColor(R.color.primario))
        }

        return dialog
    }
}
