package com.example.happyfood

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.happyfood.databinding.ActivityLoginBinding
import com.example.happyfood.model.SHARED_PREFERENCE_KEY
import com.example.happyfood.model.USER_NODE
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var dialog: Dialog
    private lateinit var sharedPreferences: SharedPreferences

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        // Initialisation
        auth = Firebase.auth
        database = Firebase.database.reference
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        // Setting the Value of shared Preferences.
        sharedPreferences = this@LoginActivity.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)

        setupProgressBar()

        binding.loginBtn.setOnClickListener {
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Kindly enter all details", Toast.LENGTH_SHORT).show()
            } else {
                dialog.show()
                loginUser(email, password)
                Toast.makeText(this, "Logged in Successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        binding.gotoSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }

        binding.googleBtn.setOnClickListener {
            dialog.show()
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
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
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task->
            dialog.dismiss()
            if(task.isSuccessful) {

                // Save the login details in the user.
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(SHARED_PREFERENCE_KEY, USER_NODE)
                editor.apply()

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Account Doesn't Exist", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupProgressBar() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.progress_bar)
        dialog.setCancelable(false)
    }
}