package com.dsm.retrofitproject.activities.profesor

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
import com.dsm.retrofitproject.models.Profesor
import com.dsm.retrofitproject.providers.AlumnoApi
import com.dsm.retrofitproject.providers.ProfesorApi
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CrearProfesorActivity : AppCompatActivity() {

    private lateinit var nombreEditText: EditText
    private lateinit var apellidoEditText: EditText
    private lateinit var edadEditText: EditText
    private lateinit var especialidadEditText: EditText
    private lateinit var crearButton: Button


    var auth_username = ""
    var auth_password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_profesor)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_previous)


        val datos: Bundle? = intent.getExtras()
        if (datos != null) {
            auth_username = datos.getString("auth_username").toString()
            auth_password = datos.getString("auth_password").toString()
        }

        nombreEditText = findViewById(R.id.editTextNombre)
        apellidoEditText = findViewById(R.id.editTextApellido)
        edadEditText = findViewById(R.id.editTextEdad)
        especialidadEditText = findViewById(R.id.editTextEspecialidad)
        crearButton = findViewById(R.id.btnGuardar)

        crearButton.setOnClickListener {
            val nombre = nombreEditText.text.toString()
            val apellido = apellidoEditText.text.toString()
            val edad = edadEditText.text.toString().toInt()
            val especialidad = especialidadEditText.text.toString()

            val profesor = Profesor(0,nombre, apellido, edad, especialidad)


            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", Credentials.basic(auth_username, auth_password))
                        .build()
                    chain.proceed(request)
                }
                .build()


            val retrofit = Retrofit.Builder()
                .baseUrl(Global.BaseAPIUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()


            val api = retrofit.create(ProfesorApi::class.java)

            api.crearProfesor(profesor).enqueue(object : Callback<Profesor> {
                override fun onResponse(call: Call<Profesor>, response: Response<Profesor>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CrearProfesorActivity, "Profesor creado exitosamente", Toast.LENGTH_SHORT).show()
                        val i = Intent(getBaseContext(), ProfesoresListaActivity::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(i)
                    } else {
                        val error = response.errorBody()?.string()
                        Log.e("API", "Error crear profesor: $error")
                        Toast.makeText(this@CrearProfesorActivity, "Error al crear el profesor", Toast.LENGTH_SHORT).show()
                    }
                    Log.e("API", response.toString())
                }

                override fun onFailure(call: Call<Profesor>, t: Throwable) {
                    Toast.makeText(this@CrearProfesorActivity, "Error al crear el profesor", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {

                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
