package com.iesvdc.acceso.orgalife

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Clase de aplicación que inicializa Hilt en toda la app.
 */
@HiltAndroidApp
class MyApplication : Application() {
    // Aquí puedes sobreescribir métodos como onCreate() si necesitas lógica de inicialización global.
}
