package com.example.kw_prototype

import android.content.Context
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.view.isGone
import com.example.kw_prototype.databinding.FragmentSignup1Binding
import com.example.kw_prototype.databinding.FragmentSignup2Binding
import com.example.kw_prototype.databinding.FragmentSignup3Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

// Note: This is currently used as step 2
//
// On creation of this fragment, application retrieves ClientMember data from database and asks
// which member is signing up.
// Agent should have already added authorized ClientMembers from their version of the app
// If member has already created account, user will not be able to continue and must return to login
class Signup3Fragment : SignupBaseFragment(R.layout.fragment_signup3) {

    private lateinit var binding: FragmentSignup3Binding
    private lateinit var radioGroup: RadioGroup
    private lateinit var btnRadio1: RadioButton
    private lateinit var btnRadio2: RadioButton
    private lateinit var btnSubmit: ImageButton
    private lateinit var curChecked: RadioButton
    private lateinit var  btnBack: ImageButton
    private lateinit var client: Client

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignup3Binding.bind(view)
        btnSubmit = binding.btnSubmit
        radioGroup = binding.radioGroup
        btnBack = binding.btnBack

        buildMemberButtons()

        btnBack.setOnClickListener {backHandler()}
        btnSubmit.setOnClickListener { submitHandler() }
    }

    // Takes Client data from LoginActivity
    // implementation of SignupBaseFragment function
    override fun transferFragData(client: Client?, memberName: String?) {
        this.client = client!!
    }

    // loop through members of Client and generate radio button with first name
    private fun buildMemberButtons() {
        for (member in client!!.members) {
            val newButton = layoutInflater.inflate(R.layout.radio_button, null) as RadioButton
            newButton.text = member.name
            newButton.id = View.generateViewId()  // important: need unique id to work properly
            radioGroup.addView(newButton)  // add to radio group
        }
    }

    // handler for submit button.
    // checks if currently selected member's account has already been created.
    // if not, pass data to Login activity and progress to final signup step
    private fun submitHandler() {
        val checkedID = radioGroup.checkedRadioButtonId
        val checkedButton = view?.findViewById<RadioButton>(checkedID)
        val memberName = checkedButton?.text.toString()

        if (checkedButton == null) return

        if (client.members.find{ m -> m.name == memberName }!!.accountCreated) {
            val errMessage = Toast.makeText(context,"This account has already been created!", Toast.LENGTH_SHORT)
            errMessage.setGravity(Gravity.TOP, 0, 0)
            errMessage.show()
            return
//            (context as LoginActivity).backToLogin()
        }

        // Pass name to Login activity and progress
        (context as LoginActivity).signInProgress(client, checkedButton?.text.toString())
    }

    private fun backHandler() {
        parentFragmentManager.popBackStack()
        (context as LoginActivity).decrementState()
    }
}