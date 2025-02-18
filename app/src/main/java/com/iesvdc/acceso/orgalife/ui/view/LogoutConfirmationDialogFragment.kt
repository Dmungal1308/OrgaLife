package com.iesvdc.acceso.orgalife.ui.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.ui.modelview.MenuViewModel

class LogoutConfirmationDialogFragment : DialogFragment() {

    private lateinit var menuViewModel: MenuViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Obtenemos el mismo ViewModel que la Activity
        menuViewModel = ViewModelProvider(requireActivity()).get(MenuViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que deseas cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                // Mandamos la orden de logout al ViewModel
                menuViewModel.logout()
            }
            .setNegativeButton("No", null)

        val dialog = builder.create()

        // Personalizar colores
        dialog.setOnShowListener {
            dialog.window?.setBackgroundDrawableResource(R.color.superficieContenedorAlta)

            val titleId = resources.getIdentifier("alertTitle", "id", "android")
            val textViewTitle = dialog.findViewById<TextView>(titleId)
            textViewTitle?.setTextColor(resources.getColor(R.color.primario))

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(resources.getColor(R.color.error))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(resources.getColor(R.color.primario))
        }

        return dialog
    }
}
