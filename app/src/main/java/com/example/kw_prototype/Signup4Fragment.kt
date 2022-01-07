package com.example.kw_prototype

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.kw_prototype.databinding.FragmentSignup4Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DatabaseReference
import java.lang.Exception

// Final step in signup process.
// user is asked to provide email and create and verify password
class Signup4Fragment(
    private val auth: FirebaseAuth,
    private val database: DatabaseReference
    ) : SignupBaseFragment(R.layout.fragment_signup4) {
    private lateinit var binding: FragmentSignup4Binding
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtPwdVerify: EditText
    private lateinit var  txtGreeting: TextView
    private lateinit var btnSubmit: ImageButton
    private lateinit var btnBack: ImageButton
    private lateinit var client: Client
    private lateinit var memberName: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignup4Binding.bind(view)
        edtEmail = binding.edtEmail
        edtPassword = binding.edtPassword
        edtPwdVerify = binding.edtPwdVerify
        txtGreeting = binding.txtGreeting
        btnSubmit = binding.btnSubmit
        btnBack = binding.btnBack

        edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        edtPwdVerify.transformationMethod = PasswordTransformationMethod.getInstance()

        btnBack.setOnClickListener {backHandler()}
        btnSubmit.setOnClickListener {submitHandler()}
    }

    // retrieve data from Login activity.
    // implements SignupBaseFragment abstract function
    override fun transferFragData(client: Client?, memberName: String?) {
        this.client = client!!
        this.memberName = memberName!!
    }

    // when fragment is loaded, display member-specific message
    override fun onStart() {
        super.onStart()
        txtGreeting.text = "Hello $memberName! Please create your login credentials."
    }

    // handler for submit button.
    //   - retrieve and verify email and password data from UI fields.
    //   - Attempt to create account with firebase
    //   - if success, update ClientMember info in database
    //   - pass data to LoginActivity's loginSuccessful(data) to progress to Main Activity
    private fun submitHandler() {
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()
        val pwdConfirm = edtPwdVerify.text.toString()

        val errMessage = Toast.makeText(context, "Please complete all fields", Toast.LENGTH_SHORT)
        errMessage.setGravity(Gravity.TOP, 0, 0)
        val pwdErrMessage = Toast.makeText(context, "Password confirmation does not match password", Toast.LENGTH_SHORT)
        pwdErrMessage.setGravity(Gravity.TOP, 0, 0)

        if(email.isEmpty() || password.isEmpty() || pwdConfirm.isEmpty()) {
            errMessage.show()
            return
        }
        else if (password != pwdConfirm) {
            pwdErrMessage.show()
            return
        }

        // attempt to signup with firebase
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val successText = Toast.makeText(context, "Your account has been created!", Toast.LENGTH_SHORT)
//                auth.currentUser?.sendEmailVerification()  // maybe in future, implement this functionality
                successText.setGravity(Gravity.CENTER, 0, 0)
                successText.show()
                addToDatabase(email, password)  // add info to database
                (context as LoginActivity).loginSuccessful(client.data.id, memberName)
            } else {
                if (task.exception is FirebaseAuthUserCollisionException) {
                    val failText = Toast.makeText(context,
                        "Email already in use by another account",
                        Toast.LENGTH_SHORT)
                    failText.setGravity(Gravity.TOP, 0, 0)
                    failText.show()
                } else {
                    val failText = Toast.makeText(context,
                        "Authentication failed. Please be sure that email is formatted correctly and password is at least 6 characters",
                        Toast.LENGTH_SHORT)
                    failText.setGravity(Gravity.TOP, 0, 0)
                    failText.show()
                }
            }
        }
    }

    // update ClientMember data in database
    private fun addToDatabase(email: String, password: String) {
        val member = client.members.find{m -> m.name == memberName}!!
        member?.accountCreated = true
        member.email = email
        member.password = password
        member.uid = auth.currentUser!!.uid
        val formattedEmail = email.replace('.', '-') // nodes cannot have '.'

        database.child("Clients").child(client.data.id!!).child("members").child(memberName)
            .setValue(member)
        database.child("EmailToClientMapping").child(formattedEmail)
            .setValue(mapOf("id" to client.data.id, "name" to memberName))
    }

    private fun backHandler() {
        parentFragmentManager.popBackStack()
        (context as LoginActivity).decrementState()
    }
}