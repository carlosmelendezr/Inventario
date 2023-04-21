package com.veramed.inventario.ui.lista


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veramed.inventario.InventoryTopAppBar
import com.veramed.inventario.R
import com.veramed.inventario.ui.AppViewModelProvider

import com.veramed.inventario.ui.navigation.NavigationDestination
import com.veramed.inventario.ui.theme.InventoryTheme
import kotlinx.coroutines.launch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


object ListaEntryDestination : NavigationDestination {
    override val route = "lista_entry"
    override val titleRes = R.string.lista_entry
}

@Composable
fun ListaEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: ListaEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(ListaEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        ItemEntryBody(
            listaUiState = viewModel.listaUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the item may not be saved in the Database. This is because when config
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
fun ItemEntryBody(
    listaUiState: ListaUiState,
    onItemValueChange: (ListaDetalles) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        ItemInputForm(listaDetalles = listaUiState.listaDetails, onValueChange = onItemValueChange)
        Button(
            onClick = onSaveClick,
            enabled = listaUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}

@Composable
fun ItemInputForm(
    listaDetalles: ListaDetalles,
    modifier: Modifier = Modifier,
    onValueChange: (ListaDetalles) -> Unit = {},
    enabled: Boolean = true
) {
    val list = listOf("1-TOMA DE INVENTARIO",
        "2-CONTEO CICLICO",
        "3-LISTA",
        "4-TRASLADO ALMACEN TIENDA",
        "5-TRASLADO ENTRE TIENDAS")

    var expanded by remember { mutableStateOf(false) }
    val currentValue = remember { mutableStateOf(list[0]) }

    Column(modifier = modifier.fillMaxWidth().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)) {
        OutlinedTextField(
            value = listaDetalles.descrip,
            onValueChange = { onValueChange(listaDetalles.copy(descrip = it)) },
            label = { Text(stringResource(R.string.lista_descrip)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
Box(modifier = Modifier.fillMaxWidth()) {
    Row(modifier = Modifier.clickable {
        expanded = !expanded
    }.align(Alignment.Center)) {

        OutlinedTextField(
            value = listaDetalles.tipo,
            onValueChange = {onValueChange(listaDetalles.copy(tipo = it))},
            label={Text("Tipo de Movimiento")},
            modifier = Modifier.fillMaxWidth().clickable {
                expanded = !expanded
            },
            enabled = false,
            readOnly = true,
            singleLine = true)

        Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {


            list.forEach {

                DropdownMenuItem(onClick = {
                    onValueChange(listaDetalles.copy(tipo=it))
                    expanded = false
                }) {

                    Text(modifier = Modifier.fillMaxWidth(),text = it)

                }
            }

        }
    }

    }


    }
 }




@Preview(showBackground = true)
@Composable
private fun ItemEntryScreenPreview() {
    InventoryTheme {
        ItemEntryBody(
            listaUiState = ListaUiState(
                ListaDetalles(1,1,"Toma de Prueba",0,"","19/04/2023","",1

                )
            ),
            onItemValueChange = {},
            onSaveClick = {}
        )
    }
}
