package com.example.kw_prototype

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// TODO
// This feature will allow the agent and ClientMembers to converse in a forum-based layout
// similar to Quora or Piazza.
// ClientMembers can post a question, then the agent will be notified and can respond.
// Then, Client members can post followup questions
class QuestionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_questions, container, false)
    }

}