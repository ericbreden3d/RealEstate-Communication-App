package com.example.kw_prototype

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.isGone
import com.example.kw_prototype.databinding.FragmentSignup1Binding
import com.example.kw_prototype.databinding.FragmentSignup2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import kotlin.math.log

//
// NO LONGER IN USE
// delete if not needed, but may end up being inserted as 3rd step.
class Signup2Fragment : SignupBaseFragment(R.layout.fragment_signup2) {

    private lateinit var binding: FragmentSignup2Binding
    private lateinit var edtClientID: EditText
    private lateinit var btnSubmit: ImageButton
    private lateinit var btnInfo: ImageButton
    private lateinit var client: Client


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignup2Binding.bind(view)
        edtClientID = binding.edtClientID
        btnSubmit = binding.btnSubmit
        btnInfo = binding.btnInfo

        btnSubmit.setOnClickListener { submitHandler() }
        btnInfo.setOnClickListener {infoHandler()}
    }

    override fun transferFragData(client: Client?, memberName: String?) {
         this.client = client!!
    }

    private fun submitHandler() {
        val clientID = edtClientID.text.toString()

        if (clientID == this.client?.data.id) (context as LoginActivity).signInProgress(client)
        else {
            val errToast = Toast.makeText(context, "This address has not been registered. Please retry or contact your agent", Toast.LENGTH_SHORT)
            errToast.setGravity(Gravity.TOP, 0, 0)
            errToast.show()
        }
    }

    private fun infoHandler() {
        val errToast = Toast.makeText(context, "This is the code provided to you by your agent", Toast.LENGTH_SHORT)
        errToast.setGravity(Gravity.TOP, 0, 0)
        errToast.show()
    }
}