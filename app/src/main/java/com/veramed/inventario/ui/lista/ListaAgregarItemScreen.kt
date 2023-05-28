package com.veramed.inventario.ui.lista

import android.util.Log
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
import kotlinx.coroutines.launch

object ListaAgregarItemDestination : NavigationDestination {
    override val route = "lista_agregar_items"
    override val titleRes = R.string.lista_agregar_item
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}


@Composable
fun ListaAgregarItemScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    navigateToDetalles:(Int) ->Unit,
    navigateToTransmitir:(Int) ->Unit,
    modifier: Modifier = Modifier,
    viewModel: ListaAgregarItemViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {
    val coroutineScope = rememberCoroutineScope()
    val listaUiState by viewModel.listaArticulosUIState.collectAsState()
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(ListaAgregarItemDestination.titleRes) ,
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = {navigateToTransmitir(viewModel.listaId)},
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
        AgregarItemEntryBody(
            itemUiState = viewModel.listaItemUiState,
            listaUiState = listaUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the item may not be updated in the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel.saveItem()
                    //navigateBack()
                }
            },
            modifier = modifier.padding(innerPadding),
            navigateToDetalles=navigateToDetalles
        )


    }
}

@Composable
fun AgregarItemEntryBody(
    itemUiState: AgregarItemUiState,
    listaUiState: ListaArticulosUiState,
    onItemValueChange: (ListaItemDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetalles: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        //CameraPreview()
        AgregarItemInputForm(itemDetails = itemUiState.listaitemDetails,
            onValueChange = onItemValueChange,
            onSaveClick = onSaveClick,enabled=itemUiState.isEntryValid)

        ListaArticulos(itemList = listaUiState.itemList,{}, navigateToDetalles=navigateToDetalles)

    }
}

@Composable
fun AgregarItemInputForm(
    itemDetails: ListaItemDetails,
    modifier: Modifier = Modifier,
    onValueChange: (ListaItemDetails) -> Unit = {},
    onSaveClick: () -> Unit,
    enabled: Boolean = true
) {
    var enableBarra = !enabled;
    val cantFocusRequester = remember { FocusRequester() }



    val colorEstado: Color
    if (itemDetails.descrip.contains("ERROR"))
        {colorEstado=androidx.compose.ui.graphics.Color.Red} else {colorEstado=androidx.compose.ui.graphics.Color.Green}


    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextField(
            value = itemDetails.descrip,
            modifier = Modifier
                .fillMaxWidth()
                .background(colorEstado),
            onValueChange = {},
            label = { Text(stringResource(R.string.lista_descrip)) },
            enabled = false,
            singleLine = true
        )
        Row() {
            Card(modifier = modifier
                .width(300.dp)
                .height(150.dp)) {

                CameraPreview(itemDetails,onValueChange )

            }

            Column() {

                OutlinedTextField(
                    value = itemDetails.barra,
                    onValueChange = { onValueChange(itemDetails.copy(barra = it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(stringResource(R.string.item_barra_req)) },
                    //modifier = Modifier.onFocusEvent { it. },
                    enabled = enableBarra,
                    singleLine = true
                )
                OutlinedTextField(
                    value = itemDetails.quantity,
                    onValueChange = { onValueChange(itemDetails.copy(quantity = it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            onSaveClick()
                            cantFocusRequester.freeFocus()
                        }
                    ),
                    label = { Text(stringResource(R.string.quantity_req)) },
                    modifier = Modifier.focusRequester(cantFocusRequester),
                    enabled = enabled ,
                    singleLine = true ,

                )


                SideEffect {
                    if (enabled) {
                        cantFocusRequester.requestFocus()
                    }
                }

            }
       }
    }
}
@Composable
private fun ListaArticulos(
    itemList: List<ListaItems>,
    onItemClick: (ListaItems) -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetalles: (Int) -> Unit
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = itemList, key = { it.id }) { listaitem ->
            ListItemRow(lista = listaitem, onItemClick = onItemClick, navigateToDetalles=navigateToDetalles)
            Divider()
        }
    }
}



@Composable
private fun ListItemRow(
    lista: ListaItems,
    onItemClick: (ListaItems) -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetalles: (Int) -> Unit
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { onItemClick(lista) }
        .padding(vertical = 5.dp, horizontal = 5.dp).clickable {  navigateToDetalles(lista.id) }


    ) {
        Box(modifier=Modifier.weight(2f,fill=true)) {
            Text(
                text = lista.sap,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h6
            )
        }
        Spacer(modifier = Modifier.weight(1f,fill = true))
        Box(modifier=Modifier.weight(5f,fill=true)) {
            Text(
                text = lista.descrip,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.h6
            )
        }
        Spacer(modifier = Modifier.weight(1f,fill = true))
        Box(modifier=Modifier.weight(1f,fill=true)) {
            Text(
                text = lista.cant.toString(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h6
            )
        }
        Spacer(modifier = Modifier.weight(1f,fill = true))
        Box(modifier=Modifier.weight(2f,fill=true)) {
            Text(
                text = lista.barra,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.h6
            )
        }
    }
}

private data class ListaHeader(@StringRes val headerStringId: Int, val weight: Float)

private val headerList = listOf(
    ListaHeader(headerStringId = R.string.lista_id, weight = 1.5f),
    ListaHeader(headerStringId = R.string.lista_nombre, weight = 1.0f),
    ListaHeader(headerStringId = R.string.lista_fecha, weight = 1.0f)
)


@Preview(showBackground = true)
@Composable
fun ListaAgregarItemScreenPreview() {
    InventoryTheme {
        ListaAgregarItemScreen(navigateBack = { /*Do nothing*/ },
            onNavigateUp = { /*Do nothing*/ }, navigateToDetalles = {}, navigateToTransmitir = {}
        )
    }
}
