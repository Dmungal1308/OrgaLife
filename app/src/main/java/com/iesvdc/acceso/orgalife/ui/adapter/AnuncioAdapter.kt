package com.iesvdc.acceso.orgalife.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.domain.models.Anuncio

class AnuncioAdapter(private var anuncios: List<Anuncio>) :
    RecyclerView.Adapter<AnuncioAdapter.AnuncioViewHolder>() {

    // Metodo para refrescar la lista de anuncios desde la Activity/Fragment
    fun setAnuncios(newList: List<Anuncio>) {
        anuncios = newList
        notifyDataSetChanged()  // En un proyecto real, usar DiffUtil
    }

    class AnuncioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.anuncioTitulo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnuncioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_anuncio, parent, false)
        return AnuncioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnuncioViewHolder, position: Int) {
        holder.titulo.text = anuncios[position].titulo
    }

    override fun getItemCount() = anuncios.size
}
