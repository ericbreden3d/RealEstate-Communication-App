package com.example.kw_prototype

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// TODO
// This feature is primarily informational and is intended to help guide the Client
// through the process of buying or selling a home, with emphasis on the steps that
// begin after a contract has been signed.
// It will include a list of clickable "milestones", each with an informational fragment
// containing a video, a synopsis, and a FAQ.
// It will also include a progress system so that the Agent can check off items when they are completed.
// Note: This will likely also be connected to the "Event" system with events having a "type"
// that relates to the milestones
class TimelineFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }
}