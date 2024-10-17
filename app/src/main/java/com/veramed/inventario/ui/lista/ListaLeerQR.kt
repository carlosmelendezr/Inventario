package com.veramed.inventario.ui.lista

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veramed.inventario.R
import com.veramed.inventario.camara.CameraQRPreview
import com.veramed.inventario.ui.AppViewModelProvider
import com.veramed.inventario.ui.navigation.NavigationDestination

object ListaLeerQRDestination : NavigationDestination {
    override val route = "lista_leer_qr"
    override val titleRes = R.string.lista_leer_qr
}

@Composable
fun ListaLeerQRScreen(
    onNavigateBack: () -> Unit,
    navigateToIngresoSap:(Int) ->Unit,
    viewModel: ListaLeerQRViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    CameraQRPreview(viewModel.codigoQRUiState,viewModel::updateUiState)
}