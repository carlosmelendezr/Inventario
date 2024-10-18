package com.veramed.inventario.ui.lista

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veramed.inventario.InventoryTopAppBar
import com.veramed.inventario.R
import com.veramed.inventario.camara.CameraQRPreview
import com.veramed.inventario.ui.AppViewModelProvider
import com.veramed.inventario.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object ListaLeerQRDestination : NavigationDestination {
    override val route = "lista_leer_qr"
    override val titleRes = R.string.lista_leer_qr
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ListaLeerQRScreen(
    onNavigateBack: () -> Unit,
    navigateToIngresoSap:(String) ->Unit,
    viewModel: ListaLeerQRViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(ListaAgregarItemDestination.titleRes) ,
                canNavigateBack = true,
                navigateUp = onNavigateBack
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                modifier = Modifier.navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = stringResource(R.string.item_entry_title),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
    ) {
        val texto = viewModel.codigoQRUiState.texto
        CameraQRPreview(viewModel.codigoQRUiState,viewModel::updateUiState)
        if (!texto.isEmpty()) {
            Log.d("INVBAR", "Docmat =" + viewModel.codigoQRUiState.texto)
            viewModel.codigoQRUiState = CodigoQR()
            LaunchedEffect(true) {
                navigateToIngresoSap(viewModel.codigoQRUiState.retDocMat(texto));
            }
        }

    }
}