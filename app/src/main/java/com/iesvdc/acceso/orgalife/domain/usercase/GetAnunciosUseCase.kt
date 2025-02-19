package com.iesvdc.acceso.orgalife.domain.usercase

import com.iesvdc.acceso.orgalife.domain.models.Anuncio
import javax.inject.Inject

class GetAnunciosUseCase @Inject constructor() {
    operator fun invoke(): List<Anuncio> {
        return listOf(
            Anuncio("Oferta especial en productos ecológicos"),
            Anuncio("Nuevo evento de bienestar el próximo sábado"),
            Anuncio("Descuento del 20% en suscripción premium")
        )
    }
}
