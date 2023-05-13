package com.dsm.retrofitproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.dsm.retrofitproject.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {


    private var emailTV: EditText? = null
    private var passwordTV: EditText? = null
    private var loginBtn: Button? = null
    private var progressBar: ProgressBar? = null
    private var mAuth: FirebaseAuth? = null

    private lateinit var auth: FirebaseAuth

    private lateinit var btnLogin: Button
    private lateinit var textViewRegister: TextView






    private fun loginUserAccount() {
        progressBar?.setVisibility(View.VISIBLE)
        val email: String
        val password: String
        email = emailTV?.getText().toString()
        password = passwordTV?.getText().toString()
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_LONG)
                .show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG)
                .show()
            return
        }
        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Login successful!",
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar?.setVisibility(View.GONE)
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        getApplicationContext(),
                        "Login failed! Please try again later",
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar?.setVisibility(View.GONE)
                }
            }
    }

    private fun initializeUI() {
        emailTV = findViewById<EditText>(R.id.editTextEmailAddress)
        passwordTV = findViewById<EditText>(R.id.txtPassword)
        loginBtn = findViewById<Button>(R.id.btnLogin)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
    }















    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth= FirebaseAuth.getInstance()
        btnLogin= findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener{
            val email= findViewById<EditText>(R.id.editTextEmailAddress).text.toString()
            val password= findViewById<EditText>(R.id.editTextEmailAddress).text.toString()
            this.login(email,password)

        }

        textViewRegister=findViewById<TextView>(R.id.textViewRegister)
        textViewRegister.setOnClickListener{
            this.goToRegister()
        }


        mAuth = FirebaseAuth.getInstance()
        initializeUI()
        loginBtn!!.setOnClickListener { loginUserAccount() }


    }


    private fun login(email: String, password:String) {

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val intent= Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }

        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()

        }
    }
    private fun goToRegister(){
        val intent= Intent(this,LoginActivity::class.java)
        startActivity(intent)

    }

}