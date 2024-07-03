package ar.edu.uade.tpo.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ar.edu.uade.tpo.R
import ar.edu.uade.tpo.ui.catalog.CatalogActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class HomeActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var btnToLogin: Button
    private lateinit var btnToRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        firebaseAuth = FirebaseAuth.getInstance()

        initComponents()
        initListeners()
    }

    private fun initComponents() {
        btnToLogin = findViewById(R.id.btnToLogin)
    }

    private fun initListeners() {
        btnToLogin.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAcoount(account)
            } catch (e: Exception) {
                Log.e("Firebase", "Error: " + e)
            }
        }
    }

    private fun firebaseAuthWithGoogleAcoount(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                val firebaseUser = firebaseAuth.currentUser
                val uid = firebaseUser!!.uid
                val email = firebaseUser.email

                if (authResult.additionalUserInfo!!.isNewUser) {
                    Toast.makeText(this, "Cuenta creada", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Ingreso exitoso", Toast.LENGTH_LONG).show()
                }

                startActivity(Intent(this, CatalogActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Login fallido...", Toast.LENGTH_LONG).show()
            }
    }
}























