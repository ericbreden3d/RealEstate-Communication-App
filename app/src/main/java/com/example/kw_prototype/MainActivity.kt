package com.example.kw_prototype

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kw_prototype.databinding.ActivityMainBinding
import com.example.kw_prototype.databinding.ToolbarBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DatabaseReference


// Hosts fragment destinations.
// Includes, bottom navigation menu, toolbar, and a nav host fragment container
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingTB: ToolbarBinding
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var btnBack: ImageButton
    private lateinit var navController: NavController
    public lateinit var curUserName: String
    public lateinit var curClientID: String

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingTB = ToolbarBinding.inflate(layoutInflater)
        btnBack = bindingTB.btnBack

        btnBack.setOnClickListener {moveTaskToBack(true)}

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = ""

        // DEBUG: if skipping login fragment for debug, comment this out to avoid null exception
        curUserName = intent.extras!!["curUserName"] as String
        curClientID = intent.extras!!["curClientID"] as String

        // set up navigation controller with bottom menu
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomMenu)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.frag_host) as NavHostFragment
        navController = navHostFragment.navController
        bottomNav.setupWithNavController(navController)

        // sync frag titles with actionbar if desired
//        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.questionsFragment, R.id.timelineFragment))
//        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.label == "Home") {
            moveTaskToBack(true)
        }
        else super.onBackPressed()
    }
}