
package com.veramed.inventario

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

import com.veramed.inventario.ui.theme.InventoryTheme


class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         if (ContextCompat.checkSelfPermission(
             this,
             Manifest.permission.CAMERA
         ) != PackageManager.PERMISSION_GRANTED ){
             requestPermissionLauncher.launch(
                 Manifest.permission.CAMERA
             )
         }


        setContent {
            InventoryTheme {
                InventoryApp()
            }
        }
    }
}
