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
import com.veramed.inventario.data.ArticuloSap
import com.veramed.inventario.data.Lista
import com.veramed.inventario.data.ListaItems
import com.veramed.inventario.data.PostListaHomeServer
import com.veramed.inventario.ui.AppViewModelProvider
import com.veramed.inventario.ui.navigation.NavigationDestination
import com.veramed.inventario.ui.theme.InventoryTheme
import kotlinx.coroutines.launch

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

    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(ListaAgregarItemDestination.titleRes) ,
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
            listaUiState = viewModel.listaArticulosSapUIState,
            modifier = modifier.padding(innerPadding),
        )


    }
}

@Composable
fun ListaIngrespSapBody(
    listaUiState: ListaArticuloSapUiState,
    modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {

        ListaArticulosSap(itemList = listaUiState.itemList,{})

    }
}

@Composable
private fun ListaArticulosSap(
    itemList: List<ArticuloSap>,
    onItemClick: (ListaItems) -> Unit,
    modifier: Modifier = Modifier) {
    Log.d("SAP"," Tamano de articulos "+itemList.size)
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = itemList, key = { it.Sap }) { listaitem ->
            ListItemSapRow(lista = listaitem, onItemClick = onItemClick)
            Divider()
        }
    }
}



@Composable
private fun ListItemSapRow(
    lista: ArticuloSap,
    onItemClick: (ListaItems) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable {  }
        .padding(vertical = 5.dp, horizontal = 5.dp).clickable {   }


    ) {
        Box(modifier=Modifier.weight(2f,fill=true)) {
            Text(
                text = lista.Sap,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h6
            )
        }
        Spacer(modifier = Modifier.weight(1f,fill = true))
        Box(modifier=Modifier.weight(5f,fill=true)) {
            Text(
                text = lista.Descripcion,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.h6
            )
        }
        Spacer(modifier = Modifier.weight(1f,fill = true))
        Box(modifier=Modifier.weight(1f,fill=true)) {
            Text(
                text = lista.cantidad.toString(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h6
            )
        }
        Spacer(modifier = Modifier.weight(1f,fill = true))

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
            onNavigateUp = { /*Do nothing*/ }
        )
    }
}
