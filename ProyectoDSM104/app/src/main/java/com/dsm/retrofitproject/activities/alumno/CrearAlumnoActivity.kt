package com.dsm.retrofitproject.activities.alumno

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dsm.retrofitproject.R
import com.dsm.retrofitproject.activities.singletons.Global
import com.dsm.retrofitproject.models.Alumno
import com.dsm.retrofitproject.providers.AlumnoApi
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CrearAlumnoActivity : AppCompatActivity() {

    private lateinit var nombreEditText: EditText
    private lateinit var apellidoEditText: EditText
    private lateinit var edadEditText: EditText
    private lateinit var crearButton: Button

    // Obtener las credenciales de autenticación
    var auth_username = ""
    var auth_password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_alumno)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_previous)

        // Obtención de datos que envia actividad anterior
        val datos: Bundle? = intent.getExtras()
        if (datos != null) {
            auth_username = datos.getString("auth_username").toString()
            auth_password = datos.getString("auth_password").toString()
        }

        nombreEditText = findViewById(R.id.editTextNombre)
        apellidoEditText = findViewById(R.id.editTextApellido)
        edadEditText = findViewById(R.id.editTextEdad)
        crearButton = findViewById(R.id.btnGuardar)

        crearButton.setOnClickListener {
            val nombre = nombreEditText.text.toString()
            val apellido = apellidoEditText.text.toString()
            val edad = edadEditText.text.toString().toInt()

            val alumno = Alumno(0,nombre, apellido, edad)

            // Crea un cliente OkHttpClient con un interceptor que agrega las credenciales de autenticación
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", Credentials.basic(auth_username, auth_password))
                        .build()
                    chain.proceed(request)
                }
                .build()

            // Crea una instancia de Retrofit con el cliente OkHttpClient
            val retrofit = Retrofit.Builder()
                .baseUrl(Global.BaseAPIUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            // Crea una instancia del servicio que utiliza la autenticación HTTP básica
            val api = retrofit.create(AlumnoApi::class.java)

            api.crearAlumno(alumno).enqueue(object : Callback<Alumno> {
                override fun onResponse(call: Call<Alumno>, response: Response<Alumno>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CrearAlumnoActivity, "Alumno creado exitosamente", Toast.LENGTH_SHORT).show()
                        val i = Intent(getBaseContext(), EstudiantesListaActivity::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(i)
                    } else {
                        val error = response.errorBody()?.string()
                        Log.e("API", "Error crear alumno: $error")
                        Toast.makeText(this@CrearAlumnoActivity, "Error al crear el alumno", Toast.LENGTH_SHORT).show()
                    }
                    Log.e("API", response.toString())
                }

                override fun onFailure(call: Call<Alumno>, t: Throwable) {
                    Toast.makeText(this@CrearAlumnoActivity, "Error al crear el alumno", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here
        return when (item.itemId) {
            android.R.id.home -> {
                // Navigate to the previous activity
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
