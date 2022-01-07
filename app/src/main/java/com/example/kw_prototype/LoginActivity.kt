package com.example.kw_prototype

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.ui.AppBarConfiguration
import com.example.kw_prototype.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

// First activity show to user.
// User can either login or go through a series of signup steps (separate fragments).
// Signup system involves progressing the signupState and iterating through a list of fragment instances
class LoginActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityLoginBinding
    private lateinit var logo: ImageView
    private lateinit var btnSignUp: Button
    private lateinit var fragView: FragmentContainerView
    private lateinit var loginFrag: Fragment
    private lateinit var signupSteps: List<SignupBaseFragment>
    private var signupState = 0

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        logo = binding.logo
        fragView = binding.fragView
        btnSignUp = binding.btnSignup

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        // instatiate first fragment
        loginFrag = LoginFragment(auth, database)

        buildUI()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_view, loginFrag)
            commit()
        }

        btnSignUp.setOnClickListener {signupHandler()}

        // DEBUG
        // addDebugClientData()
    }

    // instantiate signup fragments and run fade in animations
    private fun buildUI() {
        // fragment instances stored in list for easy iteration with signupState as index
        signupSteps = listOf(
            Signup1Fragment(database),
            Signup3Fragment(),
            Signup4Fragment(auth, database))

        val animationLogo = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val animationText = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val animationButton = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        logo.startAnimation(animationLogo)

        // start gone and fade in
        fragView.isGone = true
        btnSignUp.isGone = true

        // delay then animate
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            fragView.isGone = false
            fragView.startAnimation(animationText)
            btnSignUp.isGone = false
            btnSignUp.isEnabled = true
            btnSignUp.startAnimation(animationText)
        },750)
    }

    // handler for signup button
    private fun signupHandler() {
        btnSignUp.isGone = true
        btnSignUp.isEnabled = false
        signInProgress()
    }

    // Move to next signup fragment and update signupState.
    // This function is called from within the current signup fragment.
    // This function takes as parameters the necessary information for the next fragment
    public fun signInProgress(client: Client? = null, memberName: String? = null) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_view, signupSteps[signupState])
            addToBackStack("$signupState")  // save to back stack for proper back button functionality
            commit()
        }

        // send necessary information to next fragment
        if (client != null) signupSteps[signupState].transferFragData(client, memberName)

        // fade in next fragment
        val animationText = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        fragView.startAnimation(animationText)

        signupState++
    }

    // if root fragment is open, then only minimize the app to avoid unloading and reloading data
    override fun onBackPressed() {
        if (signupState > 0) {
            decrementState()
            super.onBackPressed()  // normal back up
        }
        else moveTaskToBack(true)  // minimize to avoid deleting current data
    }

    // Moves back a step. If the oncoming fragment is root fragment, bring back signup button
    public fun decrementState() {
        if (signupState == 1) {
            btnSignUp.isGone = false
            btnSignUp.isEnabled = true
        }
        signupState--
    }

    // progresses to main activity, including necessary data retrieved during login/signup
    // called by either login fragment or last signup fragment when login is successful
    public fun loginSuccessful(curID: String, curName: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("curUserName", curName!!)
        intent.putExtra("curClientID", curID!!)
        startActivity((intent))
        finish()  // remove from back stack. Must logout to return to login screen
    }

    // Add test data to database
    private fun addDebugClientData() {
        val c1 = ClientMember("Jennifer")
        val c2 = ClientMember("Ben")
        val members = mutableListOf<ClientMember>(c1, c2)
        val data = ClientData("123456", "Jacobson's")
        val new = Client(data)

        val locRef = database.child("Clients").child(new.data.id)
        locRef.child("data").setValue(data)
        for (member in members) {
            locRef.child("members").child(member.name).setValue(member)
        }


        var str = ""
        for (i in 1..100) str += 'h'
        val t = DateMachine.getDate()
        val l: Update = Update("TEST TEST TEST TEST TEST TEST 14", t)
        locRef.child("updates").child(t).setValue(l)
    }

}