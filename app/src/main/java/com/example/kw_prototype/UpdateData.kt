package com.example.kw_prototype

// Global Update data for relaying when a new update event is added to the database by the Agent.
// This is referenced by HomeFragment and UpdateFragment
class UpdateData {

    companion object {
        var updates: MutableList<Update> =  mutableListOf()
        var alreadyCached: Boolean = false
        var updateFragment: UpdateFragment? = null

        // Passes new update to UpdateFragment when new Update is created
        // by agent and ChildEventListener is triggered
        fun sendNewUpdate(update: Update) {
            updates.add(0, update)
            if (updateFragment != null) updateFragment?.receiveNewUpdate(update)
        }
    }
}