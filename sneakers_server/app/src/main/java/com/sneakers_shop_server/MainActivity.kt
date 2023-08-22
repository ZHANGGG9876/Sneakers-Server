package com.sneakers_shop_server

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.sneakers_shop_server.Fragment.BottomSheetDialog.BottomSheetDialogJoinUs
import com.sneakers_shop_server.model.SneakerModel


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var btnLogin : Button
    private lateinit var btnJoinUs : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

       // Write to my databse
        val database = Firebase.database
        val myRef = database.getReference("Sneaker")

        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue<SneakerModel?>()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("LoQueSea", "Failed to read value.", error.toException())
            }
        })

        val go = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,go)

        btnLogin = findViewById(R.id.sign_in_google_button)
        btnLogin.setOnClickListener{
            singInGoogle()
        }

        btnJoinUs = findViewById(R.id.join_us_button)
        btnJoinUs.setOnClickListener{
            val bottomSheetDialogJoinUs = BottomSheetDialogJoinUs()
            bottomSheetDialogJoinUs.show(supportFragmentManager, "myBottomSheet")
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode== Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data);
            if(task.isSuccessful){
                val account: GoogleSignInAccount? = task.result
                if(account != null){
                    updateUI(account)
                }else{
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    fun singInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
    private fun updateUI(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful) {
                val intent = Intent(this, MainActivity2::class.java)
                intent.putExtra("email", account.email)
                intent.putExtra("name", account.displayName)
                startActivity(intent)
            }else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account!=null) {
            updateUI(account)
        }
    }

}