package com.iesvdc.acceso.orgalife.ui.modelview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iesvdc.acceso.orgalife.domain.usercase.WaitForSplashUseCase
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val waitForSplashUseCase = WaitForSplashUseCase()

    // LiveData que indica cuándo navegar
    private val _navigateToLoginEvent = MutableLiveData<Boolean>()
    val navigateToLoginEvent: LiveData<Boolean> get() = _navigateToLoginEvent

    init {
        viewModelScope.launch {
            // Invoca el caso de uso para esperar el tiempo deseado
            val result = waitForSplashUseCase() // Por defecto, espera 1000 ms
            _navigateToLoginEvent.value = result
        }
    }
}
