package Modelo

import android.provider.ContactsContract.CommonDataKinds.Email

data class tbTicket(
    val UUID: String,
    var titulo: String,
    var descripcion: String,
    var responsable: String,
    var email: String,
    var telefonoAutor: String,
    var ubicacion: String,
    var estado: String
)

