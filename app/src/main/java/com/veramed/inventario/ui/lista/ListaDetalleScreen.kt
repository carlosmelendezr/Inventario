package com.veramed.inventario.ui.lista


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veramed.inventario.InventoryTopAppBar
import com.veramed.inventario.R
import com.veramed.inventario.ui.AppViewModelProvider
import com.veramed.inventario.ui.item.ItemDetails
import com.veramed.inventario.ui.item.ItemDetailsUiState
import com.veramed.inventario.ui.item.ItemDetailsViewModel
import com.veramed.inventario.ui.item.ItemInputFormOLD
import com.veramed.inventario.ui.navigation.NavigationDestination
import com.veramed.inventario.ui.theme.InventoryTheme

import kotlinx.coroutines.launch

object ListaDetalleDestination : NavigationDestination {
    override val route = "lista_detalle"
    override val titleRes = R.string.item_detail_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun ListaDetalleScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListaDetalleViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val datosVence = viewModel.venceUiState
    val uiState = viewModel.detalleUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(ListaDetalleDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },

    ) { innerPadding ->
        ItemDetallesBody(
            itemDetailsUiState = uiState.value,
            datosVence = datosVence ,
            onSaveItem = { viewModel::saveItem },
            onValueChange = {viewModel::actualizaUiState},
            onDelete = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the item may not be deleted from the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel.deleteItem()
                    navigateBack()
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun ItemDetallesBody(
    itemDetailsUiState: ListaItemDetalleUiState,
    onSaveItem: () -> Unit,
    onDelete: () -> Unit,
    datosVence: DatosVence,
    onValueChange: (DatosVence) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        EditItemInputForm(itemDetails = itemDetailsUiState.itemDetalle,
            enabled = false,
            onValueChange = onValueChange,
            datosVence = datosVence)
        Button(
            onClick = onSaveItem,
            enabled = true,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }

        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete))
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false }
            )
        }
    }
}
@Composable
fun EditItemInputForm(
    itemDetails: ListaItemDetails,
    datosVence: DatosVence,
    modifier: Modifier = Modifier,
    onValueChange: (DatosVence) -> Unit = {},
    enabled: Boolean = true
) {

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
             value = itemDetails.name,
             onValueChange = { },
             label = { Text(stringResource(R.string.item_name_req)) },
             modifier = Modifier.fillMaxWidth(),
             enabled = false,
             singleLine = true
         )
        Row() {
            OutlinedTextField(
                value = itemDetails.sap,
                onValueChange = {},
                label = { Text(stringResource(R.string.sap)) },
                enabled = false,
                singleLine = true
            )
            OutlinedTextField(
                value = itemDetails.barra,
                onValueChange = { },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(stringResource(R.string.barra)) },
                enabled = false,
                singleLine = true
            )
        }

       /* OutlinedTextField(
            value = datosVence.quantity,
            onValueChange = { onValueChange(datosVence.copy(quantity = it))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.quantity_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )*/
        Row() {
            OutlinedTextField(
                value = datosVence.lote,
                onValueChange = { onValueChange(datosVence.copy(lote = it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                label = { Text(stringResource(R.string.item_lote)) },
                enabled = true,
                singleLine = true
            )

            OutlinedTextField(
                value = datosVence.fecvenc,
                onValueChange = { onValueChange(datosVence.copy(fecvenc = it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(stringResource(R.string.item_vencimiento)) },
                enabled = true,
                singleLine = true
            )
        }
    }
}


@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier.padding(16.dp),
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreview() {
    InventoryTheme {
        ItemDetallesBody(
            itemDetailsUiState =ListaItemDetalleUiState(),
            onSaveItem = {},
            onDelete = {},
            datosVence = DatosVence()
        )
    }
}
