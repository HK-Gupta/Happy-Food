package com.example.happyfood.admin

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
import com.example.happyfood.MainActivity
import com.example.happyfood.R
import com.example.happyfood.databinding.ActivityAdminLoginBinding
import com.example.happyfood.model.ADMIN_USER_NODE
import com.example.happyfood.model.SHARED_PREFERENCE_KEY
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var googleSingIn: GoogleSignInClient
    private lateinit var dialog: Dialog
    private lateinit var sharedPreferences: SharedPreferences

    private val binding by lazy {
        ActivityAdminLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        // Initialising Auth and Database
        auth = Firebase.auth
        database = Firebase.database.reference
        // Initialising SignIn using Google
        googleSingIn = GoogleSignIn.getClient(this, googleSignInOptions)

        // Shared Preferences
        sharedPreferences = this@AdminLoginActivity.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)

        setupProgressBar()

        binding.loginBtn.setOnClickListener {

            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Kindly Fill all the fields", Toast.LENGTH_SHORT).show()
            } else{
                dialog.show()
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task->
                    if(task.isSuccessful) {

                        // Save the login details in the admin user.
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString(SHARED_PREFERENCE_KEY, ADMIN_USER_NODE)
                        editor.apply()

                        startActivity(Intent(this, AdminMainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Account Doesn't Exist", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
            }
        }

        binding.googleBtn.setOnClickListener {
            dialog.show()
            val signInIntent = googleSingIn.signInIntent
            launcher.launch(signInIntent)
            dialog.dismiss()
        }

        binding.gotoSignup.setOnClickListener {
            startActivity(Intent(this, AdminSignupActivity::class.java))
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                val account: GoogleSignInAccount = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener { authTask->
                    if (authTask.isSuccessful) {
                        Toast.makeText(this, "Logged In Successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, AdminMainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Log In Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Log In Failed!", Toast.LENGTH_SHORT).show()
            }
        }else {
            Toast.makeText(this, "Log In Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupProgressBar() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.progress_bar)
        dialog.setCancelable(false)
    }
}