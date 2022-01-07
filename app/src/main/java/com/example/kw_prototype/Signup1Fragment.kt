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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue

// Step one of signup process.
// User must provide Client ID, which is a code that would have been provided already by their Agent.
// The agent would have also already added this id to the database from their version of the app
class Signup1Fragment(
    private val database: DatabaseReference
    ) : SignupBaseFragment(R.layout.fragment_signup1) {

    private lateinit var binding: FragmentSignup1Binding
    private lateinit var edtClientID: EditText
    private lateinit var btnSubmit: ImageButton
    private lateinit var  btnInfo: ImageButton
    private lateinit var btnBack: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignup1Binding.bind(view)
        edtClientID = binding.edtClientID
        btnSubmit = binding.btnSubmit
        btnInfo = binding.btnInfo
        btnBack = binding.btnBack

        btnBack.setOnClickListener {backHandler()}
        btnSubmit.setOnClickListener {submitHandler()}
        btnInfo.setOnClickListener {infoHandler()}
    }

    // Retrieve ClientID from UI EditText data and verify that it exists in database
    private fun submitHandler() {
        val clientID = edtClientID.text.toString()

        // iterate through children of "Clients" node, searching for ID within ClientData structure
        database.child("Clients").get().addOnSuccessListener { clients ->
            for (data in clients.children) {
                val clientData = data.child("data").getValue<ClientData>()  // get data
                if (clientData?.id == clientID) {
                    val members: MutableList<ClientMember> = mutableListOf()
                    for (member in data.child("members").children) {  // get members
                        members.add(member.getValue<ClientMember>()!!)
                    }

                    // construct client object and pass on data for further processing
                    val client = Client(clientData, members)
                    (context as LoginActivity).signInProgress(client)
                    return@addOnSuccessListener
                }
            }
            val errToast = Toast.makeText(context, "This address has not been registered. Please retry or contact your agent", Toast.LENGTH_SHORT)
            errToast.setGravity(Gravity.TOP, 0, 0)
            errToast.show()
        }
    }

    // info button handler
    // displays information about Client ID system
    private fun infoHandler() {
        val errToast = Toast.makeText(context, "This is the code provided to you by your agent", Toast.LENGTH_SHORT)
        errToast.setGravity(Gravity.TOP, 0, 0)
        errToast.show()
    }

    private fun backHandler() {
        parentFragmentManager.popBackStack()
        (context as LoginActivity).decrementState()
    }

    override fun transferFragData(client: Client?, memberName: String?) {
//         Not needed
    }
}
