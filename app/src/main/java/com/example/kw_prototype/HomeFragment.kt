package com.example.kw_prototype

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.kw_prototype.databinding.FragmentHomeBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.lang.StringBuilder
import java.lang.reflect.Array.get
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log

// Start destination of Main Activity.
// Contains 2 primary features
class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var txtUpdate: TextView
    private lateinit var txtDate: TextView
    private lateinit var btnUpdate: Button
    private lateinit var database: DatabaseReference
    private var updates: MutableList<Update> = mutableListOf()
    private lateinit var latestUpdate: Update
    private lateinit var curClientID: String
    private lateinit var  curUserName: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
        txtUpdate = binding.txtUpdate
        txtDate = binding.txtDate
        btnUpdate = binding.btnUpdate

        database = Firebase.database.reference

        btnUpdate.setOnClickListener { showAllUpdatesHandler(view) }

        curClientID = (context as MainActivity).curClientID
        curUserName = (context as MainActivity).curUserName

        // DEBUG: uncomment this and comment previous lines to skip LoginActivity
//        curClientID = "123456"
//        curUserName = "Ben"

        // avoids duplicates and extra work caused by reloading this fragment
        if (!UpdateData.alreadyCached) setNewUpdateListener()

        updateLatest()
    }

    // Retrieve all past updates from database and listen for new ones.
    // When new update is received by database:
    //    - add to updates and notify adapter
    //    - send to update to global data for later retrieval by update fragment
    //    - set global update data alreadyCached to true to avoid setting this listener more than once
    private fun setNewUpdateListener() {
        database.child("Clients").child(curClientID).child("updates")
            .addChildEventListener(object: ChildEventListener {
                override fun onChildAdded(childData: DataSnapshot, previousChildName: String?) {
                    val update = childData.getValue<Update>()
                    updates.add(0, update!!)
                    UpdateData.sendNewUpdate(update)  // updates global UpdateData that communicates with updateFragment
                    if (!UpdateData.alreadyCached) UpdateData.alreadyCached = true
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}

            })
    }

    // Update Latest Update displayed in Home Fragment
    private fun updateLatest() {
        if (UpdateData.updates.size < 1) return
        latestUpdate = UpdateData.updates.elementAt(0)
        txtUpdate.text = latestUpdate.message
        txtDate.text = latestUpdate.time
    }

    // Handler for 'show all' updates button. Opens Update Fragment
    private fun showAllUpdatesHandler(view: View) {
        Navigation.findNavController(view).navigate(R.id.home_to_update)
    }

}