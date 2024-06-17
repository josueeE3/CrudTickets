package aplicacion.josuehernandez.aplicacioncrud

import Modelo.Conexion
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IniciarSesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciar_sesion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtCorreoLogin = findViewById<EditText>(R.id.txtCorreoLogin)
        val txtContrasenaLogin = findViewById<EditText>(R.id.txtContrasenaLogin)
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)
        val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarme)

        btnIngresar.setOnClickListener {

            val pantallaPrincipal = Intent(this, Bienvenida::class.java)

            GlobalScope.launch(Dispatchers.IO) {

                val objConexion = Conexion().cadenaConexion()

                val comprobarUsuario = objConexion?.prepareStatement("SELECT * FROM tbUsuarios WHERE correoElectronico = ? AND clave = ?")!!
                comprobarUsuario.setString(1, txtCorreoLogin.text.toString())
                comprobarUsuario.setString(2, txtContrasenaLogin.text.toString())
                val resultado = comprobarUsuario.executeQuery()

                if (resultado.next()) {
                    startActivity(pantallaPrincipal)
                } else {
                    println("Usuario no encontrado, verifique las credenciales")
                }
            }
        }

        btnRegistrarse.setOnClickListener {

            val pantallaRegistrarse = Intent(this, Registrarse::class.java)
            startActivity(pantallaRegistrarse)
        }
    }
}