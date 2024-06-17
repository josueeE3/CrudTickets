package aplicacion.josuehernandez.aplicacioncrud

import Modelo.Conexion
import Modelo.tbTicket
import RecyclerViewHelpers.Adaptador
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class Ticket : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ticket)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txttituloTicket = findViewById<TextView>(R.id.txtTitulo)
        val txtdescripcionTicket = findViewById<TextView>(R.id.txtDescripcion)
        val txtresponsable = findViewById<TextView>(R.id.txtResponsable)
        val txtemail = findViewById<TextView>(R.id.txtEmailAutor)
        val txttelefono = findViewById<TextView>(R.id.txtTelefonoAutor)
        val txtubicacion = findViewById<TextView>(R.id.txtUbicacion)
        val txtestado = findViewById<Spinner>(R.id.spnEstado)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val rcvTicket = findViewById<RecyclerView>(R.id.rcvTicket)


        val opcionesEstado = arrayOf("Activo", "Inactivo")

        txtestado.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, opcionesEstado)


        rcvTicket.layoutManager = LinearLayoutManager(this)

        fun obtenerTickets(): List<tbTicket>{
            val objConexion = Conexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("Select * from tbTicket")!!

            val listaTickets = mutableListOf<tbTicket>()

            while (resultSet.next()){
                val uuid = resultSet.getString("UUID_Ticket")
                val titulo = resultSet.getString("Titulo")
                val descripcion = resultSet.getString("Descripcion_Ticket")
                val responsable = resultSet.getString("Responsable_Ticket")
                val email = resultSet.getString("Email_Autor")
                val telefono = resultSet.getString("Telefono_Autor")
                val ubicacion = resultSet.getString("Ubicacion")
                val estado = resultSet.getString("Estado")
                val ticket = tbTicket(uuid,titulo,descripcion,responsable,email,telefono,ubicacion,estado)
                listaTickets.add(ticket)
            }
            return listaTickets
        }

        CoroutineScope(Dispatchers.IO).launch {
            val ticektsDB = obtenerTickets()
            withContext(Dispatchers.Main){
                val adapter = Adaptador(ticektsDB)
                rcvTicket.adapter = adapter
            }
        }


        btnAgregar.setOnClickListener {

            val estadoTicket = txtestado.selectedItem.toString()

            GlobalScope.launch(Dispatchers.IO){

                //1- Crear un objeto de la clase de conexion
                val objConexion = Conexion().cadenaConexion()

                //2- Crear una variable que sea igual a un PrepareStatement
                val addTicket = objConexion?.prepareStatement("insert into tbTicket(UUID_Ticket, Titulo, Descripcion_Ticket, Responsable_Ticket, Email_Autor, Telefono_Autor, Ubicacion, Estado) values(?, ?, ?, ?, ?, ?, ?, ?)")!!
                addTicket.setString(1, UUID.randomUUID().toString())
                addTicket.setString(2, txttituloTicket.text.toString())
                addTicket.setString(3, txtdescripcionTicket.text.toString())
                addTicket.setString(4, txtresponsable.text.toString())
                addTicket.setString(5, txtemail.text.toString())
                addTicket.setString(6, txttelefono.text.toString())
                addTicket.setString(7, txtubicacion.text.toString())
                addTicket.setString(8, estadoTicket)

                addTicket.executeUpdate()

                val nuevosTickets = obtenerTickets()

                withContext(Dispatchers.Main){
                    (rcvTicket.adapter as? Adaptador)?.actualizarRecyclerView(nuevosTickets)
                }
            }
        }
    }
}