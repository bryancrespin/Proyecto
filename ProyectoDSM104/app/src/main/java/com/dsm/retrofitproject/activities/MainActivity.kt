package com.dsm.retrofitproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.dsm.retrofitproject.R
import com.dsm.retrofitproject.activities.alumno.EstudiantesListaActivity
import com.dsm.retrofitproject.activities.profesor.ProfesoresListaActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cardStudents = findViewById<CardView>(R.id.studentsCard)
        val cardTeachers = findViewById<CardView>(R.id.teachersCard)
        val cardTeachers2 = findViewById<CardView>(R.id.teachersCard2)

        cardStudents.setOnClickListener{
            val intent = Intent(this, EstudiantesListaActivity::class.java)
            startActivity(intent)
        }
        cardTeachers.setOnClickListener{
            val intent = Intent(this, ProfesoresListaActivity::class.java)
            startActivity(intent)
        }


        cardTeachers2.setOnClickListener{
            val intent = Intent(this, Login1Activity::class.java)
            startActivity(intent)
        }

    }

}





