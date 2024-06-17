package RecyclerViewHelpers

import Modelo.Conexion
import Modelo.tbTicket
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import aplicacion.josuehernandez.aplicacioncrud.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.SQLException
import java.util.UUID

class Adaptador(private var Datos: List<tbTicket>): RecyclerView.Adapter<ViewHolder>(){

    fun actualizarRecyclerView(nuevaLista: List<tbTicket>){
        Datos = nuevaLista
        notifyDataSetChanged()
    }

    fun eliminarRegistro(titulo: String, posicion: Int) {
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO) {
            //Dos pasos para eliminar de la base de datos

            //1- Crear un objeto de la clase conexion
            val objConexion = Conexion().cadenaConexion()

            //2- Creo una variable que contenga un PrepareStatement
            val deleteProducto =
                objConexion?.prepareStatement("delete tbTicket where titulo = ?")!!
            deleteProducto.setString(1, titulo)
            deleteProducto.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }

        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    fun actualizarListadoDespuesDeEditar(uuid: String, nuevoNombre: String, estado: String) {
        val identificador = Datos.indexOfFirst { it.UUID == uuid }
        if (identificador != -1) {
            Datos[identificador].titulo = nuevoNombre
            Datos[identificador].estado = estado
            notifyItemChanged(identificador)
        } else {
            Log.e("ActualizarListado", "UUID no encontrado: $uuid")
        }
    }

    fun editarProducto(uuid: String,titulo: String, estado: String){
        //-Creo una corrutina
        GlobalScope.launch(Dispatchers.IO){
            //1- Creo un objeto de la clase conexion
            val objConexion = Conexion().cadenaConexion()

            //2- Creo una variable que contenga un PrepareStatement
            val updateProducto = objConexion?.prepareStatement("update tbTicket set Titulo = ?, Estado = ? where uuid_Ticket = ?")!!
            updateProducto.setString(1, titulo)
            updateProducto?.setString(2, estado)
            updateProducto.setString(3, uuid)

            updateProducto.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = Datos[position]
        holder.txtTituloTicket.text = item.titulo
        //holder.txtEstadoTicket.text = item.estado

        when (item.estado) {
            "Activo" -> holder.imgEstado.setImageResource(R.drawable.ic_estadoverde)
            "Inactivo" -> holder.imgEstado.setImageResource(R.drawable.ic_estadored)
        }

        holder.imgEliminar.setOnClickListener {
            val context = holder.txtTituloTicket.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Estas seguro que deseas eliminar?")

            //botones de mi alerta
            builder.setPositiveButton("Si") { dialog, which ->
                val ticket = Datos[position]
                eliminarRegistro(ticket.titulo, position)
            }

            builder.setNegativeButton("No") { dialog, wich ->
                //Si doy en clic en "No" se cierra la alerta
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()

            holder.imgEditar.setOnClickListener {
                val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
                alertDialogBuilder.setTitle("Editar Ticket")
                alertDialogBuilder.setMessage("Ingrese el nuevo título del ticket:")

                val layout = LinearLayout(holder.itemView.context)
                layout.orientation = LinearLayout.VERTICAL

                val inputTitulo = EditText(holder.itemView.context)
                inputTitulo.setText(item.titulo)
                layout.addView(inputTitulo)

                val estadoSpinner = Spinner(holder.itemView.context)
                val opcionesEstado = arrayOf("Activo", "Inactivo")
                estadoSpinner.adapter = ArrayAdapter(holder.itemView.context, android.R.layout.simple_spinner_dropdown_item, opcionesEstado)
                val estadoPosicion = opcionesEstado.indexOf(item.estado)
                if (estadoPosicion >= 0) {
                    estadoSpinner.setSelection(estadoPosicion)
                }
                layout.addView(estadoSpinner)

                alertDialogBuilder.setView(layout)

                alertDialogBuilder.setPositiveButton("Guardar") { dialog, which ->
                    val nuevoTitulo = inputTitulo.text.toString().trim()
                    val nuevoEstado = estadoSpinner.selectedItem.toString()
                    if (nuevoTitulo.isNotEmpty()) {
                        editarProducto(item.UUID, nuevoTitulo, nuevoEstado)
                        actualizarListadoDespuesDeEditar(item.UUID, nuevoTitulo, nuevoEstado)
                    } else {
                        Toast.makeText(holder.itemView.context, "Ingrese un título válido", Toast.LENGTH_SHORT).show()
                    }
                }

                alertDialogBuilder.setNegativeButton("Cancelar") { dialog, which ->
                    dialog.dismiss()
                }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }

        }

    }

}

