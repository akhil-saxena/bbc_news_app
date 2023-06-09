package com.wmhack.navdrawer

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class BottomDialogFragment : DialogFragment() {

    private lateinit var latitudeEditText: EditText
    private lateinit var longitudeEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var myInterface: MyInterface

    interface MyInterface {
        fun onVariablePassed(value: APIResponseData)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        myInterface = context as MyInterface
    }

    // Wherever you want to pass the variable value, call the interface function
    fun passVariableToActivity(value: APIResponseData) {
        myInterface.onVariablePassed(value)
    }


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
            // Navigate to the homepage after saving the location data
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
//                activity?.runOnUiThread() {
//                    Toast.makeText(activity, "API request failed", Toast.LENGTH_SHORT)
//                        .show()
//                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()

                    val gson = Gson()
                    // Parse the API response using Gson
                    val json_response = gson.fromJson(responseBody, APIResponse::class.java)

                    // Retrieve the image URL from the API response
                    val imageUrl = json_response?.data?.img_url
                    val adTitle = json_response?.data?.adTitle
                    val lpUrl = json_response?.data?.lp_url

                    Log.d("Image URL in Response", imageUrl.toString())

                    val API_Response = APIResponseData(imageUrl, lpUrl, adTitle)

                    if (API_Response != null) {
                        passVariableToActivity(API_Response)
                    }
                    
                    else{
                        Log.d("API_Data_Invalid", "API_Data_Invalid")
                    }
                } else { // Request failed, handle the error
                    Log.d("Failing", "Hallo")
//                    activity?.runOnUiThread() {
//                        Toast.makeText(activity, "API request failed", Toast.LENGTH_SHORT)
//                            .show()
//                    }
                }
            }

        }
        )
    }

}