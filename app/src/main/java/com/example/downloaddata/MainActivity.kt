package com.example.downloaddata

import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.util.jar.Manifest

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private val STORAGE_PERMISSION_CODE: Int = 1000
    private lateinit var startButton:Button
    private lateinit var urlInput:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startButton = findViewById(R.id.start_download)
        urlInput = findViewById(R.id.url_input)
        startButton.setOnClickListener {
//            handle Permission for write external storage if mobile android>=marshmello
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                    PackageManager.PERMISSION_DENIED){
//                  Permission Denied, Request Again
                    requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),STORAGE_PERMISSION_CODE)
                }
                else{
//                  Permission Granted, Start Download
                    startDownloading()
                }
            }
            else{
//          System os is less than marshmello
            }
        }
    }

    private fun startDownloading() {
        var url = urlInput.text.toString()
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("webData")
        request.setDescription("The file is downloading...")
//        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED       )
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,  q)
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            STORAGE_PERMISSION_CODE->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startDownloading()
                }
                else{
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show()
                }
            }

        }

    }
}