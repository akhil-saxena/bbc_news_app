package com.wmhack.navdrawer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.CategoryAdapter
import com.google.android.material.navigation.NavigationView
import com.wmhack.navdrawer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), BottomDialogFragment.MyInterface {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var horizontalRecyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var image: ImageView

    fun displayImage(result: String?) {
//         Load our ad image into this ImageView

        runOnUiThread {
            Glide.with(this)  // You can use a library like Glide or Picasso to handle image loading
                .load(result)
                .placeholder(R.drawable.rounded_edit_text) // Set a placeholder image
                .error(R.drawable.default_one) // Set an error image
                .into(image)
        }

    }

    override fun onVariablePassed(value: String) {
        // Access the passed variable value here
        // You can use it as needed in your activity
        Log.d("HelloWorld", value)
        displayImage(value)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        binding.appBarMain.toolbar.title = "BBC News"

        binding.appBarMain.fab.setOnClickListener {
            val bottomDialog = BottomDialogFragment()
            bottomDialog.show(supportFragmentManager, "BottomDialog")
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        horizontalRecyclerView = findViewById(R.id.recyclerView)
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView)


            // Set up RecyclerView with a horizontal layout
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            horizontalRecyclerView.layoutManager = layoutManager

            // Set up your RecyclerView adapter and populate it with data
            val products = listOf(
                Product(R.drawable.carousel_first),
                Product(R.drawable.carousel_first)
            )

            productAdapter = ProductAdapter(products)
            horizontalRecyclerView.adapter = productAdapter

// One more recycler view
            val categoryLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            categoryRecyclerView.layoutManager = categoryLayoutManager

            val categories = listOf(
                Category(R.drawable.img1),
                Category(R.drawable.img2),
                Category(R.drawable.img3),
                Category(R.drawable.img4),
                Category(R.drawable.img5)
            )

            categoryAdapter = CategoryAdapter(categories)
            categoryRecyclerView.adapter = categoryAdapter


        image = binding.appBarMain.homepage.homefragment.adImage
        image.setOnClickListener {
            // Handle the click event here
            // Example: show a toast message
            openWebsite("https://www.walmart.com")
            Toast.makeText(this@MainActivity, "Image clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openWebsite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

//    private fun callRecyclerView() {
//
//        // Set up RecyclerView with a horizontal layout
//        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        horizontalRecyclerView.layoutManager = layoutManager
//
//        // Set up your RecyclerView adapter and populate it with data
//        val products = listOf(
//            Product(R.drawable.carousel_first),
//            Product(R.drawable.carousel_first)
//        )
//
//        productAdapter = ProductAdapter(products)
//        horizontalRecyclerView.adapter = productAdapter
//
//// One more recycler view
//        val categoryLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        categoryRecyclerView.layoutManager = categoryLayoutManager
//
//        val categories = listOf(
//            Category(R.drawable.img1),
//            Category(R.drawable.img2),
//            Category(R.drawable.img3),
//            Category(R.drawable.img4),
//            Category(R.drawable.img5)
//        )
//
//        categoryAdapter = CategoryAdapter(categories)
//        categoryRecyclerView.adapter = categoryAdapter
//    }

}
