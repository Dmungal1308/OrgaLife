package com.iesvdc.acceso.orgalife.ui.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.domain.usercase.LogoutUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LogoutConfirmationDialogFragment : DialogFragment() {

    // Inyectamos el caso de uso de logout
    @Inject
    lateinit var logoutUseCase: LogoutUseCase

    // Callback para notificar a la Activity que se realizó el logout y se debe redirigir a LoginActivity.
    var onLogoutConfirmed: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que deseas cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                // Ejecuta el logout borrando el token en SharedPreferences
                logoutUseCase()
                // Notifica a la Activity para que, por ejemplo, navegue a LoginActivity y limpie el estado en memoria.
                onLogoutConfirmed?.invoke()
            }
            .setNegativeButton("No", null)

        val dialog = builder.create()

        // Personalización de estilos
        dialog.setOnShowListener {
            dialog.window?.setBackgroundDrawableResource(R.color.superficieContenedorAlta)

            val titleId = resources.getIdentifier("alertTitle", "id", "android")
            val textViewTitle = dialog.findViewById<TextView>(titleId)
            textViewTitle?.setTextColor(resources.getColor(R.color.primario))

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                ?.setTextColor(resources.getColor(R.color.error))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                ?.setTextColor(resources.getColor(R.color.primario))
        }

        return dialog
    }
}
