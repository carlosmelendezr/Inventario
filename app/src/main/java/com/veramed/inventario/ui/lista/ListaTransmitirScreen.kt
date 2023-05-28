package com.veramed.inventario.ui.lista

import androidx.annotation.StringRes
import androidx.compose.foundation.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veramed.inventario.InventoryTopAppBar
import com.veramed.inventario.R
import com.veramed.inventario.camara.CameraPreview
import com.veramed.inventario.data.Lista
import com.veramed.inventario.data.ListaItems
import com.veramed.inventario.data.PostListaHomeServer
import com.veramed.inventario.ui.AppViewModelProvider
import com.veramed.inventario.ui.navigation.NavigationDestination
import com.veramed.inventario.ui.theme.InventoryTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

object ListaTransmitirDestination : NavigationDestination {
    override val route = "lista_transmitir"
    override val titleRes = R.string.lista_transmitir
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}


@Composable
fun ListaTransmitirScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListaTransmitirViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {
    val coroutineScope = rememberCoroutineScope()

    val listaArticulosUiState by viewModel.listaArticulosUIState.collectAsState()

    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(ListaAgregarItemDestination.titleRes) ,
                canNavigateBack = true

            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = {PostListaHomeServer(
                    lista=viewModel.listaTUiState.toLista(),
                    listaItem=listaArticulosUiState.itemList)},
                modifier = Modifier.navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = stringResource(R.string.item_entry_title),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
    ) { innerPadding ->
        TransmitirBody(

            listaTUiState = viewModel.listaTUiState,

            modifier = modifier.padding(innerPadding),

        )


    }
}

@Composable
fun TransmitirBody(
    listaTUiState: ListaTUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {

        Card(elevation = 10.dp,
            backgroundColor= androidx.compose.ui.graphics.Color.DarkGray

        ) {
            Row() {

                Column(Modifier.fillMaxWidth()) {
                    // Encabezado

                    Text(
                        text = listaTUiState.descrip,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.h6
                    )
                    // Subtítulo

                }


            }
        }


    }
}






@Preview(showBackground = true)
@Composable
fun ListaTransmitorScreenPreview() {
    InventoryTheme {
        ListaTransmitirScreen(navigateBack = { /*Do nothing*/ }

        )
    }
}
