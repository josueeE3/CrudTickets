package aplicacion.josuehernandez.aplicacioncrud

import Modelo.Conexion
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class Registrarse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrarse)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtCorreo = findViewById<EditText>(R.id.txtCorreoRegistro)
        val txtContrasenaRegistro = findViewById<EditText>(R.id.txtContrasenaRegistro)
        val txtConfirmarPassword = findViewById<EditText>(R.id.txtConfirmarContrasenaRegistro)
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)
        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSession)
        val imgVerConfirmacionContrasena = findViewById<ImageView>(R.id.imgOcultarContrasena)
        val imgAtras = findViewById<ImageView>(R.id.imgAtras)



        btnCrearCuenta.setOnClickListener{

            val correo = txtCorreo.text.toString().trim()
            val contrasena = txtContrasenaRegistro.text.toString().trim()
            val confirmarContrasena = txtConfirmarPassword.text.toString().trim()

            if (correo.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
                Toast.makeText(this@Registrarse, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contrasena != confirmarContrasena) {
                Toast.makeText(this@Registrarse, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            GlobalScope.launch(Dispatchers.IO){
                try{
                    val objConexion = Conexion().cadenaConexion()

                    val crearUsuario =
                        objConexion?.prepareStatement("INSERT INTO tbUsuarios(UUID_usuario, correoElectronico, clave) VALUES (?, ?, ?)")!!
                    crearUsuario.setString(1, UUID.randomUUID().toString())
                    crearUsuario.setString(2, txtCorreo.text.toString())
                    crearUsuario.setString(3, txtContrasenaRegistro.text.toString())
                    crearUsuario.executeUpdate()
                    withContext(Dispatchers.Main) {
                        //Abro otra corrutina o "Hilo" para mostrar un mensaje y limpiar los campos
                        //Lo hago en el Hilo Main por que el hilo IO no permite mostrar nada en pantalla
                        Toast.makeText(this@Registrarse, "Usuario creado", Toast.LENGTH_SHORT).show()
                        txtCorreo.setText("")
                        txtContrasenaRegistro.setText("")
                        txtConfirmarPassword.setText("")
                    }
                }catch (e:Exception){
                    withContext(Dispatchers.Main) {

                        Toast.makeText(this@Registrarse, "Error al crear el usuario: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        imgVerConfirmacionContrasena.setOnClickListener {
            if (txtConfirmarPassword.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                txtConfirmarPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                txtConfirmarPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        btnIniciarSesion.setOnClickListener {
            val pantallaIniciarSesion = Intent(this, IniciarSesion::class.java)
            startActivity(pantallaIniciarSesion)
        }
            imgAtras.setOnClickListener {
                val pantallaIniciarSesion = Intent(this, IniciarSesion::class.java)
                startActivity(pantallaIniciarSesion)
            }
        }
    }
}