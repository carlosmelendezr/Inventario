package com.veramed.inventario.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veramed.inventario.InventoryTopAppBar
import com.veramed.inventario.R
import com.veramed.inventario.data.Lista
import com.veramed.inventario.ui.AppViewModelProvider
import com.veramed.inventario.ui.navigation.NavigationDestination
import com.veramed.inventario.ui.theme.InventoryTheme


object HomeListaDestino : NavigationDestination {
    override val route = "home_lista"
    override val titleRes = R.string.app_lista
}

/**
 * Entry route for HomeLista
 */
@Composable
fun HomeLista(
    navigateToListaEntry: () -> Unit,
    navigateToListaAgregarItem: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeListaViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val listaUiState by viewModel.listaUiState.collectAsState()
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToListaEntry,
                modifier = Modifier.navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.item_entry_title),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
    ) { innerPadding ->
        HomeListaBody(
            itemList = listaUiState.listas,
            //onItemClick = navigateToItemUpdate,
            onItemClick = navigateToListaAgregarItem,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun HomeListaBody(
    itemList: List<Lista>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //InventoryListHeader()
        Divider()
        if (itemList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
                style = MaterialTheme.typography.subtitle2
            )
        } else {
            ListaInventario(itemList = itemList, onItemClick = { onItemClick(it.id) })
        }
    }
}

@Composable
private fun ListaInventario(
    itemList: List<Lista>,
    onItemClick: (Lista) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = itemList, key = { it.id }) { listaitem ->
            InventoryListRow(lista = listaitem, onItemClick = onItemClick)
            Divider()
        }
    }
}

@Composable
private fun InventoryListHeader(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        headerList.forEach {
            Text(
                text = stringResource(it.headerStringId),
                modifier = Modifier.weight(it.weight),
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Composable
private fun InventoryListRow(
    lista: Lista,
    onItemClick: (Lista) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { onItemClick(lista) }
        .padding(vertical = 16.dp)
    ) {
        val paddingModifier  = Modifier
            .padding(10.dp)
            .fillMaxWidth()
        Card(elevation = 10.dp, modifier = paddingModifier) {
            Row() {
                Text(
                    text = lista.descrip,
                    modifier = Modifier.weight(1.5f),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = lista.idusuario.toString(),
                    modifier = Modifier.weight(1.0f)
                )
                Text(text = lista.centro.toString(), modifier = Modifier.weight(1.0f))
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
fun HomeListaScreenRoutePreview() {
    InventoryTheme {
        HomeListaBody(
            listOf(
                Lista(1, 1, "Lista de Productos 1",1,6565656,"19/04/2023",1,1 ),

            ),
            onItemClick = {}
        )
    }
}
