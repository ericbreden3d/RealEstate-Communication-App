package com.example.kw_prototype

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.isGone
import com.example.kw_prototype.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue

// Root fragment of Login Activity. Allows pre-existing users to login
class LoginFragment(
    private val auth: FirebaseAuth,
    private val database: DatabaseReference) : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var btnSubmit: ImageButton
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        btnSubmit = binding.btnSubmit
        edtEmail = binding.edtEmail
        edtPassword = binding.edtPassword
        btnSubmit.setOnClickListener {onSubmit()}

        // hides password characters
        edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
    }

    // handler for submit button.
    // retrieves email and password from UI, check for validity, and attempt to login with Firebase
    private fun onSubmit() {
        val errMessage = Toast.makeText(context, "Invalid Login Credentials. Please Retry or Signup", Toast.LENGTH_SHORT)
        errMessage.setGravity(Gravity.TOP, 0, 0)
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) { errMessage.show(); return}

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {task ->
            if (task.isSuccessful) getInfoAndSignIn(email)
            else errMessage.show()
        }
    }

    // If Firebase authentication successful, retrieve data and pass to LoginActivity
    private fun getInfoAndSignIn(email: String) {
        val formattedEmail = email.replace('.', '-')  // Firebase doesn't take node values with '.'
        database.child("EmailToClientMapping")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val ref = task.result.child(formattedEmail)
                    val id   = ref.child("id").value as String
                    val name = ref.child("name").value as String
                    (context as LoginActivity).loginSuccessful(id, name)
                } else {
                    Log.d(TAG, "getNameAndID: " + task.exception.toString())
                    Toast.makeText(context, "Error retrieving login info, please try again", Toast.LENGTH_SHORT).show()
                }

            }
    }

}