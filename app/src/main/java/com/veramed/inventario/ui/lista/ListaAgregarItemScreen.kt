package com.veramed.inventario.ui.lista

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veramed.inventario.InventoryTopAppBar
import com.veramed.inventario.R
import com.veramed.inventario.camara.CameraPreview
import com.veramed.inventario.data.Lista
import com.veramed.inventario.data.ListaItems
import com.veramed.inventario.ui.AppViewModelProvider
import com.veramed.inventario.ui.navigation.NavigationDestination
import com.veramed.inventario.ui.theme.InventoryTheme
import kotlinx.coroutines.launch
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

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
    modifier: Modifier = Modifier,
    viewModel: ListaAgregarItemViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(ListaAgregarItemDestination.titleRes) ,
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        AgregarItemEntryBody(
            itemUiState = viewModel.listaItemUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the item may not be updated in the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel.saveItem()
                    navigateBack()
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun AgregarItemEntryBody(
    itemUiState: AgregarItemUiState,
    onItemValueChange: (ListaItemDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        //CameraPreview()
        AgregarItemInputForm(itemDetails = itemUiState.listaitemDetails, onValueChange = onItemValueChange)
        Button(
            onClick = onSaveClick,
            enabled = itemUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}

@Composable
fun AgregarItemInputForm(
    itemDetails: ListaItemDetails,
    modifier: Modifier = Modifier,
    onValueChange: (ListaItemDetails) -> Unit = {},
    enabled: Boolean = true
) {
    var textbarra by rememberSaveable { mutableStateOf("") }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row() {
            Card(modifier = modifier.width(300.dp).height(150.dp)) {
                CameraPreview(itemDetails,onValueChange )
                Text(text = "Escanee el codigo de barra",color = androidx.compose.ui.graphics.Color.Red)
            }
            Column() {
                //Text(text = "Desc:"+itemDetails.descrip)
                OutlinedTextField(
                    value = itemDetails.name,
                    onValueChange = {},
                    label = { Text(stringResource(R.string.lista_descrip)) },
                    enabled = false,
                    singleLine = true
                )
                OutlinedTextField(
                    value = itemDetails.barra,
                    onValueChange = { onValueChange(itemDetails.copy(barra = it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(stringResource(R.string.item_barra_req)) },
                    //modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    singleLine = true
                )
                OutlinedTextField(
                    value = itemDetails.quantity,
                    onValueChange = { onValueChange(itemDetails.copy(quantity = it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(stringResource(R.string.quantity_req)) },
                    //modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    singleLine = true
                )
            }
       }
    }
}
@Composable
private fun ListaArticulos(
    itemList: List<ListaItems>,
    onItemClick: (ListaItems) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = itemList, key = { it.id }) { listaitem ->
            ListItemRow(lista = listaitem, onItemClick = onItemClick)
            Divider()
        }
    }
}



@Composable
private fun ListItemRow(
    lista: ListaItems,
    onItemClick: (ListaItems) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { onItemClick(lista) }
        .padding(vertical = 16.dp)
    ) {
       /* val paddingModifier  = Modifier
            .padding(10.dp)
            .fillMaxWidth()*/

            Row() {

                Column(Modifier.fillMaxWidth()) {
                    // Encabezado

                    Text(
                        text = lista.descrip,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.h6
                    )

                    // Subtítulo


                }

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
            onNavigateUp = { /*Do nothing*/ }
        )
    }
}
