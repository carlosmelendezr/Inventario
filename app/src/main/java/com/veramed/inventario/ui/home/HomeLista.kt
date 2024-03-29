package com.veramed.inventario.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veramed.inventario.InventoryTopAppBar
import com.veramed.inventario.R
import com.veramed.inventario.data.Lista
import com.veramed.inventario.ui.AppViewModelProvider
import com.veramed.inventario.ui.navigation.NavigationDestination
import com.veramed.inventario.ui.theme.InventoryTheme
import com.veramed.inventario.ui.theme.colorTarjeta
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.veramed.util.convertLongToTimeScreen



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
    val listaUiState by viewModel.listaUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(HomeDestination.titleRes)+" ("+viewModel.sesion.name+")",
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
            onLongItemClick = viewModel::liberarLista,
            modifier = modifier.padding(innerPadding),
            onDelete = viewModel::deleteLista

        )
    }
}

@Composable
private fun HomeListaBody(
    itemList: List<Lista>,
    onItemClick: (Int) -> Unit,
    onLongItemClick: (Lista) -> Unit,
    modifier: Modifier = Modifier,
    onDelete: (Lista) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {


        if (itemList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
                style = MaterialTheme.typography.subtitle2
            )
        } else {
            ListaInventario(itemList = itemList,
                onItemClick = { onItemClick(it.id) },
                onLongItemClick = onLongItemClick,
                onDelete = onDelete
                )
        }
    }
}

@Composable
private fun ListaInventario(
    itemList: List<Lista>,
    onItemClick: (Lista) -> Unit,
    onLongItemClick: (Lista) -> Unit,
    modifier: Modifier = Modifier,
    onDelete: (Lista) -> Unit,
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(3.dp)) {
        items(items = itemList, key = { it.id }) { listaitem ->
            InventoryListRow(lista = listaitem,
                onItemClick = onItemClick,
                onLongItemClick=onLongItemClick,
                onDelete=onDelete
                )
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InventoryListRow(
    lista: Lista,
    onItemClick: (Lista) -> Unit,
    onLongItemClick: (Lista) -> Unit,
    modifier: Modifier = Modifier,
    onDelete: (Lista) -> Unit,

) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

    // if ( lista.color!=3 && lista.articulos!! < 150)
    Row(modifier = modifier
        .fillMaxWidth()
        .combinedClickable (
            onClick = {if ( lista.color!=3 && lista.articulos!! < 250) onItemClick(lista)},
            onLongClick = { onLongItemClick(lista.copy(color=0, idservidor = 0))}
        )
        //.clickable {  onItemClick(lista)}
        .padding(vertical = 5.dp)
    ) {
        val paddingModifier  = Modifier
            .padding(5.dp)
            .fillMaxWidth()
        Card(elevation = 5.dp,
            modifier = paddingModifier,
            backgroundColor=colorTarjeta(lista.color)

        ) {
            Row() {

                Column(Modifier.fillMaxWidth()) {
                    // Encabezado
                Row() {
                    Text(
                        text = lista.descrip,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = "ID : ${lista.idservidor.toString()}",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Right,
                        modifier=Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.h6
                    )
                }
                    // Subtítulo
                    Row() {
                        Text(
                            text = "Creado el " + convertLongToTimeScreen(lista.fecha),
                            style = MaterialTheme.typography.body1
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text ="Articulos "+lista.articulos,
                            textAlign = TextAlign.Right,
                            modifier=Modifier.fillMaxWidth()

                        )

                    }


                    IconButton(
                        onClick = { deleteConfirmationRequired = true }
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Borrar")
                    }
                    if (deleteConfirmationRequired) {
                        DeleteConfirmationDialog(
                            onDeleteConfirm = {
                                deleteConfirmationRequired = false
                                onDelete(lista)
                            },
                            onDeleteCancel = { deleteConfirmationRequired = false }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(1.dp))
                Column(Modifier.fillMaxWidth()) {
                Text(
                    text = "Usuario : "+lista.idusuario.toString(),
                    modifier = Modifier.weight(1.0f)
                )
                Text(text = lista.centro.toString(), modifier = Modifier.weight(1.0f))
                }

            }
        }
    }
}

private data class ListaHeader(@StringRes val headerStringId: Int, val weight: Float)

@Composable
private fun clickLista( lista: Lista,
                        onItemClick: (Lista) -> Unit) {
    if (lista.color==3 && lista.articulos!! < 150) {
        InformacionDialog("Ya la lista fué transmitida." )
        return
    }
    if ( lista.articulos!! >= 150) {
        InformacionDialog("Solo e permiten un maximo de 150 articulos por lista" )
        return
    }
    onItemClick(lista)
}

private val headerList = listOf(
    ListaHeader(headerStringId = R.string.lista_id, weight = 1.5f),
    ListaHeader(headerStringId = R.string.lista_nombre, weight = 1.0f),
    ListaHeader(headerStringId = R.string.lista_fecha, weight = 1.0f)
)

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

@Composable
private fun InformacionDialog(mensage:String

) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(mensage) },
        modifier = Modifier.padding(16.dp),
        dismissButton = {},
        confirmButton = {}
    )
}


@Preview(showBackground = true)
@Composable
fun HomeListaScreenRoutePreview() {
    InventoryTheme {
        HomeListaBody(
            listOf(
                Lista(1,
                    1,
                    "Lista de Productos 1",1,6565656,"19/04/2023",1,1,0 ),

            ),
            onItemClick = {}, onLongItemClick = {}, onDelete = {}

        )
    }
}
