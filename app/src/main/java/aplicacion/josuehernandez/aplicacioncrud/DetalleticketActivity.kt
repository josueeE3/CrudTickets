package aplicacion.josuehernandez.aplicacioncrud

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetalleticketActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalleticket)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val titulo = intent.getStringExtra("Titulo")
        val descripcion = intent.getStringExtra("Descripcion")
        val responsable = intent.getStringExtra("Responsable")
        val email = intent.getStringExtra("CorreoElectronico")
        val telefono = intent.getStringExtra("Telefono")
        val ubicacion = intent.getStringExtra("Ubicacion")
        val estado = intent.getStringExtra("Estado")

        findViewById<TextView>(R.id.textView12).text = titulo
        findViewById<TextView>(R.id.textView7).text = descripcion
        findViewById<TextView>(R.id.textView9).text = responsable
        findViewById<TextView>(R.id.textView8).text = email
        findViewById<TextView>(R.id.textView11).text = telefono
        findViewById<TextView>(R.id.textView10).text = ubicacion
        findViewById<TextView>(R.id.textView13).text = estado






    }
}