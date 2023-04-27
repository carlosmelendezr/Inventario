
package com.veramed.inventario

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import com.veramed.inventario.ui.theme.InventoryTheme


class MainActivity : ComponentActivity() {

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InventoryTheme {
                InventoryApp()
            }
        }
    }
}
