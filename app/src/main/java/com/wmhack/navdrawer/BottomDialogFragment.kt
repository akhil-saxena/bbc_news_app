package com.wmhack.navdrawer

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import androidx.fragment.app.DialogFragment
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_dialog, container, false)
        latitudeEditText = view.findViewById(R.id.latitudeEditText)
        longitudeEditText = view.findViewById(R.id.longitudeEditText)
        submitButton = view.findViewById(R.id.submitButton)

        val defaultLatitude = "123.67"
        val defaultLongitude = "167.45"
        submitButton.setOnClickListener {
            val latitude = latitudeEditText.text.toString()
            val longitude = longitudeEditText.text.toString()

            if (latitude.isBlank() || longitude.isBlank()) {
                latitudeEditText.setText(defaultLatitude)
                longitudeEditText.setText(defaultLongitude)

            }
            sendRequest(latitude, longitude)
            // Navigate to the homepage after saving the location data
            dismiss()
        }
        return view
    }

    private fun sendRequest(latitude: String, longitude: String) {
        val url = "https://www-qa.sams.com.mx/api/v1/dsp/getAds"
        //val url = "https://mocki.io/v1/87d432c7-26e0-4245-99a2-7e8db6234672"
        // Replace with your API endpoint
        val res = "{\n" +
                "    \"statusCode\": \"SUCCESS\",\n" +
                "    \"data\": {\n" +
                "        \"event_type\": \"Sunny\",\n" +
                "        \"event_name\": \"Weather\",\n" +
                "        \"img_url\": \"https://i5.walmartimages.com/dfwrs/d718042f-3580/k2-_d37b02fd-1ad9-462a-aa14-9e983faa0dcb.v1.jpg\",\n" +
                "        \"lp_url\": \"2023-05-31T17:59:41.970879Z\",\n" +
                "        \"adTitle\": \"Test Title Page\",\n" +
                "        \"adSubtitle\": \"Test Ad SubTitle\",\n" +
                "        \"adDescription\": \"Testing\",\n" +
                "        \"ctaText\": \"Button\",\n" +
                "        \"backgroundColor\": \"F1F1F2\",\n" +
                "        \"textDefault\": \"333333\",\n" +
                "        \"textHover\": \"FFFFFF\",\n" +
                "        \"textFocus\": \"FFFFFF\",\n" +
                "        \"buttonDefault\": \"F1F1F2\",\n" +
                "        \"buttonHover\": \"0071DC\",\n" +
                "        \"buttonFocus\": \"041E42\"\n" +
                "    }\n" +
                "}"

        val requestBody = FormBody.Builder()
            .add("{{geo", """{"longitude": $longitude, "latitude": $latitude}""")
            .build()


        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        Log.d("API request", request.toString())
        Log.d("API request body", requestBody.toString())

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
            }


            override fun onResponse(call: okhttp3.Call, response: Response) {
                if (response.isSuccessful) {
                   val responseBody = response.body().toString()
                    //val responseBody = res
                    Log.d("API response body", responseBody)
                    val gson = Gson()
                    // Parse the API response using Gson
                    val imageResponse = gson.fromJson(responseBody, APIResponse::class.java)

                    // Retrieve the image URL from the API response
                    val imageUrl = imageResponse?.imageUrl
                    Log.d("Image URL in Response", imageUrl.toString())

                    // Pass the image URL to the ImageActivity
                } else { // Request failed, handle the error
                    Log.d("API response fail", response.toString())
                }
            }
        }
        )
    }

}
