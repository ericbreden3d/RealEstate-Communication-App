package com.example.kw_prototype

import java.util.Vector

// Data Model Structure to hold all data related to a single Client.
// For the purposes of this application, a Client can be defined as the group of
// ClientMembers who are involved in the buying or selling of any given property.
// This is the root node for each Client object in the cloud database.
class Client (
    var data: ClientData = ClientData(),
    var members: MutableList<ClientMember> = mutableListOf(),
    var updates: MutableList<Update> = mutableListOf()
    // Boolean determines whether account has already been created
)