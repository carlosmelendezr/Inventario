package com.veramed.inventario.ui.lista


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veramed.inventario.InventoryTopAppBar
import com.veramed.inventario.R
import com.veramed.inventario.ui.AppViewModelProvider
import com.veramed.inventario.ui.navigation.NavigationDestination
import com.veramed.inventario.ui.theme.InventoryTheme

import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

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
    val uiState = viewModel.detalleUiState
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
            itemDetailsUiState = uiState,
            datosVence = viewModel.venceUiState ,
            //onSaveItem =   viewModel::saveItem,
            onSaveItem = {
                viewModel.saveItem()
                navigateBack()
            },
            onValueChange = viewModel::actualizaUiState,
            onDelete = {

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
    onValueChange: (DatosVence) -> Unit ,
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
    onValueChange: (DatosVence) -> Unit ,
    enabled: Boolean = true
) {

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(5.dp)) {
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
                modifier = Modifier.width(150.dp),
                onValueChange = {},
                label = { Text(stringResource(R.string.sap)) },
                enabled = false,
                singleLine = true
            )
            OutlinedTextField(
                value = itemDetails.barra,
                onValueChange = { },
                modifier = Modifier.width(220.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(stringResource(R.string.barra)) },
                enabled = false,
                singleLine = true
            )


        }
        Row() {
            OutlinedTextField(
                value = itemDetails.quantity,
                modifier = Modifier.width(120.dp),
                onValueChange = { onValueChange(datosVence.copy(quantity =it.toInt())) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(stringResource(R.string.quantity_req)) },
                enabled = false,
                singleLine = true
            )
            OutlinedTextField(
                value = datosVence.lote,
                modifier = Modifier.width(200.dp),
                onValueChange = { onValueChange(datosVence.copy(lote = it.uppercase())) },
                label = { Text(stringResource(R.string.item_lote)) },
                enabled = true,
                singleLine = true
            )
        }

        Row() {

            ItemInputLote(datosVence, onValueChange=onValueChange, enabled = enabled)
        }
            /*OutlinedTextField(
                value = datosVence.fecvenc,
                //onValueChange = { if (it.matches(validEntry )) onValueChange(datosVence.copy(fecvenc = it)) },
                onValueChange = { onValueChange(datosVence.copy(fecvenc = it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(stringResource(R.string.item_vencimiento)) },
                enabled = true,
                singleLine = true
            )*/
        }

}

@Composable
fun ItemInputLote(
    datosVence: DatosVence,
    onValueChange: (DatosVence) -> Unit ,
    enabled: Boolean = true
) {
    val meses = listOf("01","02","03","04","05","06","07","08","09","10","11","12")
   // val formatter = DateTimeFormatter.ofPattern("yyyy")
    //val current = LocalDateTime.now().format(formatter).toInt()-5
    val current = 2018
    val years = mutableListOf<String>()
    for( i in current..current+10) {
        years.add(i.toString())
    }
    var mesexpan by remember { mutableStateOf(false) }
    var yearexpan by remember { mutableStateOf(false) }



        Box() {
            Row(modifier = Modifier
                .clickable {
                    mesexpan = !mesexpan
                }
                .align(Alignment.Center)) {

                TextField(
                    value = datosVence.mes,
                    onValueChange = { onValueChange(datosVence.copy(mes = it)) },

                    label={Text("Mes Vence")},
                    modifier = Modifier.width(100.dp)
                        .clickable {
                            mesexpan = !mesexpan
                        },
                    enabled = false,
                    readOnly = true,
                    singleLine = true)

                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
                DropdownMenu(
                    expanded = mesexpan,
                    onDismissRequest = {  mesexpan = false },

                ) {


                    meses.forEach {

                        DropdownMenuItem(
                            onClick = {onValueChange(datosVence.copy(mes = it))
                            mesexpan = false}
                        ) {

                            Text(modifier = Modifier.fillMaxWidth(),text = it)

                        }
                    }

                }
            }

        }

        Box() {
            Row(modifier = Modifier
                .clickable {
                    yearexpan = !yearexpan
                }
                .align(Alignment.Center)) {

                TextField(
                    value = datosVence.year,
                    onValueChange = {onValueChange(datosVence.copy(year = it))
                        },
                    label={Text("Año Vence")},
                    modifier = Modifier.width(100.dp)
                        .clickable {
                            yearexpan = !yearexpan
                        },
                    enabled = false,
                    readOnly = true,
                    singleLine = true)

                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
                DropdownMenu(
                    expanded = yearexpan,
                    onDismissRequest = { yearexpan = false },

                ) {

                    years.forEach {

                        DropdownMenuItem(onClick = {
                            onValueChange(datosVence.copy(year = it))
                            yearexpan = false
                        }) {
                            Text(modifier = Modifier.fillMaxWidth(),text = it)
                        }
                    }

                }
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
            datosVence = DatosVence(),
            onValueChange = {}
        )
    }
}
