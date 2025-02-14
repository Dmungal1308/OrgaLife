package com.iesvdc.acceso.orgalife.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iesvdc.acceso.orgalife.R
import com.iesvdc.acceso.orgalife.data.Anuncio

class AnuncioAdapter(private val anuncios: List<Anuncio>) :
    RecyclerView.Adapter<AnuncioAdapter.AnuncioViewHolder>() {

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
