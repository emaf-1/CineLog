package it.progmob.cinelog.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import it.progmob.cinelog.MainActivity
import it.progmob.cinelog.R
import it.progmob.cinelog.model.User

class WelcomeActivity : AppCompatActivity() {

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res -> this.onSignInResult(res) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // utente loggato--> MainActivity
        if (FirebaseAuth.getInstance().currentUser != null) {
            goToMain()
            return
        }

        // login
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.Theme_CineLog)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            val firebaseUser = FirebaseAuth.getInstance().currentUser!!
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(firebaseUser.uid)

            // se non esiste crea user su firestore
            userRef.get().addOnSuccessListener { document ->
                if (!document.exists()) {
                    val newUser = User(
                        uid = firebaseUser.uid,
                        displayName = firebaseUser.displayName ?: "",
                        email = firebaseUser.email ?: "",
                        role = "user",
                        createdAt = System.currentTimeMillis()
                    )
                    userRef.set(newUser)
                }
                goToMain()
            }
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}