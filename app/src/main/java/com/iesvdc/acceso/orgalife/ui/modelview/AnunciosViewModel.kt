package com.iesvdc.acceso.orgalife.ui.modelview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iesvdc.acceso.orgalife.domain.models.Anuncio
import com.iesvdc.acceso.orgalife.domain.usercase.GetAnunciosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnunciosViewModel @Inject constructor(
    application: Application,
    private val getAnunciosUseCase: GetAnunciosUseCase
) : AndroidViewModel(application) {

    private val _anuncios = MutableLiveData<List<Anuncio>>()
    val anuncios: LiveData<List<Anuncio>> get() = _anuncios

    init {
        _anuncios.value = getAnunciosUseCase()
    }
}
