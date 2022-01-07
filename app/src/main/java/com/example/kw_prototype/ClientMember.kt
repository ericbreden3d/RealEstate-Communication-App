package com.example.kw_prototype

// Data Model Structure to hold data about a single ClientMember account of a Client
class ClientMember (
    val name: String = "",
    var accountCreated: Boolean = false,
    var email: String = "",
    var password: String = "",
    var uid: String = ""
)
