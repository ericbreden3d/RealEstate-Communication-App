package com.example.kw_prototype

import androidx.fragment.app.Fragment

// All signup fragments inherit from this abstract class
// so that the signup system in Login Activity can generically
// pass necessary data to newly loaded fragments
abstract class SignupBaseFragment(private val fragId: Int) : Fragment(fragId) {
    abstract fun transferFragData(client: Client?, memberName: String?)
}