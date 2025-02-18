package com.iesvdc.acceso.orgalife.domain.usercase

import com.iesvdc.acceso.orgalife.domain.models.Anuncio

/**
 * Caso de uso para obtener la lista de anuncios.
 * (Aquí podrías tener lógica extra, como filtrar anuncios,
 *  llamar a un repositorio remoto, etc.)
 */
class GetAnunciosUseCase {

    operator fun invoke(): List<Anuncio> {
        // Hard-coded por simplicidad.
        // En un proyecto real, llamas a un repositorio, API, etc.
        return listOf(
            Anuncio("Oferta especial en productos ecológicos"),
            Anuncio("Nuevo evento de bienestar el próximo sábado"),
            Anuncio("Descuento del 20% en suscripción premium")
        )
    }
}