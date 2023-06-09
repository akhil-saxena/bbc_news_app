package com.wmhack.navdrawer.ui.home

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.Glide
import com.wmhack.navdrawer.R
import com.wmhack.navdrawer.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.adText
//        textView.setText("Test Title Page")
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("requestKey"){requestKey, bundle ->
            val result = bundle.getString("bundleKey")

            //use that result
            displayImage(result)
        }
    }

    fun displayImage(result: String?){
        // Load our ad image into this ImageView
        activity?.let {
            Glide.with(it)  // You can use a library like Glide or Picasso to handle image loading
                .load(result)
                .placeholder(R.drawable.default_image) // Set a placeholder image
                .error(R.drawable.banner_1) // Set an error image
                .into(binding.adImage)
        }
    }
}


//        val categoryLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        categoryRecyclerView.layoutManager = categoryLayoutManager
//
//        val categories = listOf(
//            Category("Sports"),
//            Category("Electronics"),
//            Category("Gaming"),
//            Category("Grocery"),
//            Category("Utilities"),
//            Category("Appliances"),
//            Category("Cosmetics"),
//            Category("Kids"),
//            Category("Health")
//        )
//
//        categoryAdapter = CategoryAdapter(categories)
//        categoryRecyclerView.adapter = categoryAdapter
//
//
//    }
//    private fun openWebsite(url: String) {
//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.data = Uri.parse(url)
//        startActivity(intent)
//    }
//}