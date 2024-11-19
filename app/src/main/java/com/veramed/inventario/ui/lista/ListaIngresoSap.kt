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
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.veramed.inventario.data.ArticuloSap
import com.veramed.inventario.data.Lista
import com.veramed.inventario.data.ListaItems
import com.veramed.inventario.data.Moveventos
import com.veramed.inventario.data.PostListaHomeServer
import com.veramed.inventario.data.api.PostMovEventos
import com.veramed.inventario.ui.AppViewModelProvider
import com.veramed.inventario.ui.navigation.NavigationDestination
import com.veramed.inventario.ui.theme.InventoryTheme
import com.veramed.util.convertLongToTime
import kotlinx.coroutines.launch
import java.util.Date

object ListaIngresoSapDestination : NavigationDestination {
    override val route = "lista_ingreso_sap"
    override val titleRes = R.string.lista_ingreso_sap
    const val docmat = "docmat"
    val routeWithArgs = "$route/{$docmat}"
}


@Composable
fun ListaIngrespSapScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListaIngresoSapViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val listaItems = viewModel.mRoomList
    val despuesto = viewModel.puesto
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = despuesto.substring(despuesto.indexOf("-")+1) ,
                canNavigateBack = true,
                navigateUp = onNavigateUp
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
    ) { innerPadding ->


            ListaIngrespSapBody(
                listaUiState = listaItems,
                modifier = modifier.padding(innerPadding), onItemOk = viewModel::guardaItemOk,
                viewModel = viewModel
            )


    }
}

@Composable
fun ListaIngrespSapBody(
    listaUiState: List<ArticuloSap>?,
    onItemOk: (Int) -> Unit,
    viewModel: ListaIngresoSapViewModel,
    modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        if (viewModel.puesto.isNotBlank()) {
            ListaArticulosSap(
                itemList = listaUiState, onItemOk = onItemOk,
                viewModel = viewModel
            )
        }else{
            SeleccionarPuesto(viewModel = viewModel)
        }

    }
}

@Composable
private fun ListaArticulosSap(
    itemList: List<ArticuloSap>?,
    onItemOk: (Int) -> Unit,
    viewModel: ListaIngresoSapViewModel,
    modifier: Modifier = Modifier) {
    if (itemList != null) {
        Log.d("SAP"," Tamano de articulos "+itemList.size)
    }

    if ( viewModel.corregir ) {
        ItemCorregirBody(
            itemCorregir = viewModel.itemCorregir,
            onValueChange = viewModel::actualizaUiState,
            onSaveItem = {
                viewModel.guardaItemError()
            }
        )
    }
    if ( !viewModel.corregir ) {
        LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (itemList != null) {
                items(items = itemList.filter { it.estatus == 0 }, key = { it.Sap }) { listaitem ->
                    ListItemSapRow(
                        lista = listaitem, onItemOk = onItemOk,
                        viewModel = viewModel
                    )
                    Divider()

                }
            }
        }

    }


}



@Composable
private fun ListItemSapRow(
    lista: ArticuloSap,
    onItemOk: (Int) -> Unit,
    viewModel: ListaIngresoSapViewModel,
    modifier: Modifier = Modifier
) {
    if (lista.estatus>0) return


    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { }
        .padding(vertical = 5.dp, horizontal = 5.dp)
        .clickable { }


    ) {
        Column() {

            Row() {
                Box(modifier = Modifier.weight(2f, fill = true)) {
                    Text(
                        text = lista.Sap,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = lista.cantidad.toString(),
                        fontWeight = FontWeight.Bold,
                        modifier= Modifier.align(Alignment.CenterEnd),
                        style = MaterialTheme.typography.h6,

                    )
                }
            }
            Row() {
                Box(modifier = Modifier.weight(5f, fill = true)) {
                    Text(
                        text = lista.Descripcion,
                        fontWeight = FontWeight.Normal,
                        style = MaterialTheme.typography.h6
                    )
                }
            }

            Row( modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly) {

                OutlinedButton(onClick =  {onItemOk(lista.id)} ,
                    colors = buttonColors(androidx.compose.ui.graphics.Color.Green),)
                    {
                        Text("Aceptar")
                    }
                OutlinedButton(onClick = {viewModel.corregirItem(lista)},
                    colors = buttonColors(androidx.compose.ui.graphics.Color.Red)) {
                    Text("Rechazar")
                }

            }

        }

    }
}

@Composable
fun SeleccionarPuesto(
    viewModel: ListaIngresoSapViewModel,
    modifier: Modifier = Modifier
) {

    val list = listOf("3-SANTA MONICA PCP",
        "2-SANTA MONICA TIENDA")

    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(5.dp)) {

        Box(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.clickable {
                expanded = !expanded
            }.align(Alignment.Center)) {

                OutlinedTextField(
                    value = viewModel.puesto,
                    onValueChange = {viewModel.puesto =it},
                    label={Text("Puesto")},
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
                            viewModel.puesto =it
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

private data class ListaSapHeader(@StringRes val headerStringId: Int, val weight: Float)

private val headerList = listOf(
    ListaSapHeader(headerStringId = R.string.lista_id, weight = 1.5f),
    ListaSapHeader(headerStringId = R.string.lista_nombre, weight = 1.0f),
    ListaSapHeader(headerStringId = R.string.lista_fecha, weight = 1.0f)
)


@Preview(showBackground = true)
@Composable
fun ListaIngrespSapScreenPreview() {
    InventoryTheme {
        ListaIngrespSapScreen(navigateBack = { /*Do nothing*/ },
            onNavigateUp = { /*Do nothing*/ },

        )
    }
}
