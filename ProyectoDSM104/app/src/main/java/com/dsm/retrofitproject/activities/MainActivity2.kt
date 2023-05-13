package com.dsm.retrofitproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.dsm.retrofitproject.R
import com.dsm.retrofitproject.activities.alumno.EstudiantesListaActivity
import com.dsm.retrofitproject.activities.profesor.ProfesoresListaActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity2 : AppCompatActivity() {
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.actio_sign_out ->{
                FirebaseAuth.getInstance().signOut().also{
                    Toast.makeText(this, "sesion cerrada", Toast.LENGTH_SHORT).show()

                    val intent= Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }

            }


            R.id.actio_sign_out1 ->{
                FirebaseAuth.getInstance().signOut().also{
                    Toast.makeText(this, "Listado de Alumno", Toast.LENGTH_SHORT).show()

                    val intent= Intent(this, EstudiantesListaActivity::class.java)
                    startActivity(intent)
                    finish()

                }

            }





            R.id.actio_sign_out3 ->{
                FirebaseAuth.getInstance().signOut().also{
                    Toast.makeText(this, "Listado de profesor", Toast.LENGTH_SHORT).show()

                    val intent= Intent(this, ProfesoresListaActivity::class.java)
                    startActivity(intent)
                    finish()

                }

            }



        }

        return super.onOptionsItemSelected(item)
    }

}

