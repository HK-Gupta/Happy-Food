package com.example.happyfood

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.happyfood.databinding.ActivitySignupBinding
import com.example.happyfood.model.USER_NODE
import com.example.happyfood.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSigIn: GoogleSignInClient
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    private val binding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        // Initialising Firebase Auth.
        auth = Firebase.auth
        database = Firebase.database.reference
        googleSigIn = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.signupBtn.setOnClickListener {
            name = binding.name.text.toString().trim()
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()
            if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Kindly Fill All the Details ", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password, name)
            }
        }

        binding.googleBtn.setOnClickListener {
            val signIntent = googleSigIn.signInIntent
            launcher.launch(signIntent)
        }

        binding.gotoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                val account: GoogleSignInAccount = task.result
                val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credentials).addOnCompleteListener {authTask->
                    if(authTask.isSuccessful) {
                        Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LocationActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "SignIn Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "SignIn Failed", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }
    }
    private fun createAccount(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account created Successfully /nKindly Choose Location", Toast.LENGTH_SHORT).show()
                saveUserData(email, password, name)
                startActivity(Intent(this, LocationActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Failed to Created Account", Toast.LENGTH_SHORT).show()
                Log.d("Creation", "Create Account : Failed", task.exception)
            }
        }
    }

    private fun saveUserData(email: String, password: String, name: String) {
        val user = UserModel(email, password, name)
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        database.child(USER_NODE).child(userID).setValue(user)
    }
}