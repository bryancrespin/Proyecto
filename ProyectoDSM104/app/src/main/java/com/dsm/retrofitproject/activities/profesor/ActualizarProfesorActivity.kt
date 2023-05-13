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
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import okhttp3.Credentials
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ActualizarProfesorActivity : AppCompatActivity() {

    private lateinit var api: AlumnoApi
    private var profesor: Profesor? = null

    private lateinit var nombreEditText: EditText
    private lateinit var apellidoEditText: EditText
    private lateinit var edadEditText: EditText
    private lateinit var especialidadEditText: EditText
    private lateinit var actualizarButton: Button


    val auth_username = "admin"
    val auth_password = "admin123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_profesor)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_previous)

        nombreEditText = findViewById(R.id.nombreEditText)
        apellidoEditText = findViewById(R.id.apellidoEditText)
        edadEditText = findViewById(R.id.edadEditText)
        especialidadEditText = findViewById(R.id.especialidadEditText)
        actualizarButton = findViewById(R.id.actualizarButton)


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


        val profesorId = intent.getIntExtra("profesor_id", -1)
        Log.e("API", "profesorId : $profesorId")

        val nombre = intent.getStringExtra("nombre").toString()
        val apellido = intent.getStringExtra("apellido").toString()
        val edad = intent.getIntExtra("edad", 1)
        val especialidad = intent.getStringExtra("especialidad").toString()

        nombreEditText.setText(nombre)
        apellidoEditText.setText(apellido)
        edadEditText.setText(edad.toString())
        especialidadEditText.setText(especialidad)

        val profesor = Profesor(0,nombre, apellido, edad, especialidad)


        actualizarButton.setOnClickListener {
            if (profesor != null) {

                val profesorActualizado = Profesor(
                    profesorId,
                    nombreEditText.text.toString(),
                    apellidoEditText.text.toString(),
                    edadEditText.text.toString().toInt(),
                    especialidadEditText.text.toString()
                )


                val jsonProfesorActualizado = Gson().toJson(profesorActualizado)
                Log.d("API", "JSON enviado: $jsonProfesorActualizado")

                val gson = GsonBuilder()
                    .setLenient()
                    .create()


                api.actualizarProfesor(profesorId, profesorActualizado).enqueue(object : Callback<Profesor> {
                    override fun onResponse(call: Call<Profesor>, response: Response<Profesor>) {
                        if (response.isSuccessful && response.body() != null) {

                            Toast.makeText(this@ActualizarProfesorActivity, "Profesor actualizado correctamente", Toast.LENGTH_SHORT).show()
                            val i = Intent(getBaseContext(), ProfesoresListaActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(i)
                        } else {

                            try {
                                val errorJson = response.errorBody()?.string()
                                val errorObj = JSONObject(errorJson)
                                val errorMessage = errorObj.getString("message")
                                Toast.makeText(this@ActualizarProfesorActivity, errorMessage, Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {

                                Toast.makeText(this@ActualizarProfesorActivity, "Error al actualizar el profesor", Toast.LENGTH_SHORT).show()
                                Log.e("API", "Error al parsear el JSON: ${e.message}")
                            }
                        }
                    }

                    override fun onFailure(call: Call<Profesor>, t: Throwable) {

                        Log.e("API", "onFailure : $t")
                        Toast.makeText(this@ActualizarProfesorActivity, "Error al actualizar el profesor", Toast.LENGTH_SHORT).show()


                        try {
                            val gson = GsonBuilder().setLenient().create()
                            val error = t.message ?: ""
                            val profesor = gson.fromJson(error, Profesor::class.java)

                        } catch (e: JsonSyntaxException) {
                            Log.e("API", "Error al parsear el JSON: ${e.message}")
                        } catch (e: IllegalStateException) {
                            Log.e("API", "Error al parsear el JSON: ${e.message}")
                        }
                    }
                })
            }
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