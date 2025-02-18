package com.iesvdc.acceso.orgalife.ui.modelview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iesvdc.acceso.orgalife.domain.models.Anuncio
import com.iesvdc.acceso.orgalife.domain.usercase.GetAnunciosUseCase

class AnunciosViewModel(application: Application) : AndroidViewModel(application) {

    private val getAnunciosUseCase = GetAnunciosUseCase()

    private val _anuncios = MutableLiveData<List<Anuncio>>()
    val anuncios: LiveData<List<Anuncio>> get() = _anuncios

    init {
        // Llamamos al Use Case para obtener la lista
        _anuncios.value = getAnunciosUseCase()
    }

    // Si quieres exponer un metodo para recargar anuncios (por ejemplo):
    fun loadAnuncios() {
        _anuncios.value = getAnunciosUseCase()
    }
}
