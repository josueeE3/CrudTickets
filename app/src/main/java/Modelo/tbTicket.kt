package Modelo

import android.provider.ContactsContract.CommonDataKinds.Email

data class tbTicket(
    val UUID: String,
    val titulo: String,
    val descripcion: String,
    val responsable: String,
    val email: String,
    val telefonoAutor: String,
    val ubicacion: String,
    val estado: String
)
