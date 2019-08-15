package com.jamjan.drawing_application

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.widget.Button

import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.widget.ImageView


const val PICK_IMAGE = 1

class MainActivity : AppCompatActivity() {

    var buttonImageChooser : Button? = null
    var bitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        // Get the button
        buttonImageChooser = findViewById(R.id.fragment_main_choose_image_button)

        // Add a listener to the button
        buttonImageChooser?.setOnClickListener{view ->
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // At this point check the result code!
        if(resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE) {

                // load the bitmap by uri
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data?.data)

                // Switch to edit fragment / drawing fragment
                setContentView(R.layout.fragment_drawing)

                // Load the bitmap into the fragment
                findViewById<ImageView>(R.id.fragment_drawing_background_image_view)?.setImageBitmap(bitmap)

            } else {
                // Not known!
            }
        }
    }
}