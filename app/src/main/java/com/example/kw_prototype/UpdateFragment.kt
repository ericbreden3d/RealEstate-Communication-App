package com.example.kw_prototype

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kw_prototype.databinding.FragmentUpdateBinding

// This is the resulting fragment when the user clicks "show all" on HomeFragment.
// It displays all past Updates created by the Agent in a Recycler View
class UpdateFragment : Fragment(R.layout.fragment_update) {
    private lateinit var binding: FragmentUpdateBinding
    private lateinit var rvUpdate: RecyclerView
    private lateinit var adapter: UpdateAdapter
    private var updates: MutableList<Update> = mutableListOf()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpdateBinding.bind(view)
        rvUpdate = binding.rvUpdate
        rvUpdate.layoutManager = LinearLayoutManager(context)
        adapter = UpdateAdapter(updates)
        rvUpdate.adapter = adapter

        fetchUpdatesFromCache()
        UpdateData.updateFragment = this
    }

    // Retrieve udpates from global UpdateData cache
    private fun fetchUpdatesFromCache() {
        Log.d(TAG, "fetchUpdatesFromCache: UPDATE")
        updates.addAll(UpdateData.updates)
        adapter.notifyDataSetChanged()

    }

    // Called by UpdateData class to relay a new update that triggered the ChildEventListener
    fun receiveNewUpdate(update: Update) {
        updates.add(0, update)
        adapter.notifyItemInserted(0)
    }
}