package RecyclerViewHelpers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aplicacion.josuehernandez.aplicacioncrud.R

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val txtTituloTicket: TextView = view.findViewById(R.id.txtTituloTicket)
    val txtEstadoTicket: TextView = view.findViewById(R.id.txtEstadoTicket)
    val imgEstado: ImageView = view.findViewById(R.id.imgEstado)
    val imgEditar: ImageView = view.findViewById(R.id.imgEditar)
    val imgEliminar: ImageView = view.findViewById(R.id.imgBorrar)


}