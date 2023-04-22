
package com.veramed.inventario

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.veramed.inventario.ui.theme.InventoryTheme


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
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
