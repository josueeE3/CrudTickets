package Modelo

import java.sql.Connection
import java.sql.DriverManager

class Conexion {

    fun cadenaConexion(): Connection?{
        try {
            val ip = "jdbc:oracle:thin:@192.168.1.18:1521:xe"
            val usuario = "Josue_Develop"
            val contrasena = "josue1234"

            val conexion = DriverManager.getConnection(ip, usuario, contrasena)
            return conexion
        }catch (e: Exception){
            println("Este es el error: $e")
            return null
        }
    }
}