package com.sea.cameraapp

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var btnOpenCamera: Button
    private lateinit var ivPhoto: ImageView
    private  var i: Int =1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnOpenCamera = findViewById(R.id.btnCamera);
        ivPhoto = findViewById(R.id.image)
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result-> if (result.resultCode == Activity.RESULT_OK){
                handleCameraImage(result.data)

        }
        }
        btnOpenCamera.setOnClickListener {

            //intent to open camera app
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(cameraIntent)

        }

    }

    private fun handleCameraImage(intent: Intent?) {
        val bitmap = intent?.extras?.get("data") as Bitmap
        ivPhoto.setImageBitmap(bitmap)
        val path = saveToInternalStorage(bitmap)
        print(path)
        print("ggggggg")
    }
    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {
        i++;
        val cw = ContextWrapper(applicationContext)
        // path to /data/data/yourapp/app_data/imageDir
        val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, i.toString() + "_profile.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            print(bitmapImage.width);
            print(bitmapImage.height);

            val bitmap : Bitmap = Bitmap.createScaledBitmap( bitmapImage,bitmapImage.width, bitmapImage.height, true);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directory.getAbsolutePath()
    }
}