package com.wmhack.navdrawer

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class BottomDialogFragment : DialogFragment() {

    private lateinit var latitudeEditText: EditText
    private lateinit var longitudeEditText: EditText
    private lateinit var submitButton: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setDimAmount(0.5f)
        return dialog
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_dialog, container, false)
        latitudeEditText = view.findViewById(R.id.latitudeEditText)
        longitudeEditText = view.findViewById(R.id.longitudeEditText)
        submitButton = view.findViewById(R.id.submitButton)

//        val defaultLatitude = "77.1025"
//        val defaultLongitude = "28.7041"

        submitButton.setOnClickListener {
            var latitude = latitudeEditText.text.toString()
            var longitude = longitudeEditText.text.toString()
            val defaultLatitude = "77.1025"
            val defaultLongitude = "28.7041"

            if (latitude.isNullOrEmpty() || longitude.isNullOrEmpty()) {
                latitude = defaultLatitude
                longitude = defaultLongitude
                Log.d("Latitude", latitude.toString())
                Log.d("longitude", longitude.toString())

            }
            sendRequest(latitude, longitude)

            dismiss()
        }
        return view
    }


    private fun sendRequest(latitude: String, longitude: String) {
        val url = "https://www-qa.sams.com.mx/api/v1/dsp/getAds"

        val jsonBody = """
        {
            "geo": {
                "longitude": $longitude,
                "latitude": $latitude
            }
        }
    """.trimIndent()


//        val mediaType = MediaType.parse("application/json")
//        val requestBody = RequestBody.create(mediaType, jsonBody)

        val mediaType = "application/json".toMediaType()
        val requestBody = jsonBody.toRequestBody(mediaType)


        val request = Request.Builder().url(url).post(requestBody).build()

        Log.d("API request", request.toString())
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Failing", e.toString())
                activity?.runOnUiThread() {
                    Toast.makeText(activity, "API request failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()

                    val gson = Gson()
                    // Parse the API response using Gson
                    val json_response = gson.fromJson(responseBody, APIResponse::class.java)

                    // Retrieve the image URL from the API response
                    val imageUrl = json_response?.data?.img_url
                    Log.d("Image URL in Response", imageUrl.toString())

                    val result = imageUrl
                    setFragmentResult("requestKey", bundleOf("bundleKey" to result))

                } else { // Request failed, handle the error
                    Log.d("Failing", "Hallo")
                    activity?.runOnUiThread() {
                        Toast.makeText(activity, "API request failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }
        )
    }
}