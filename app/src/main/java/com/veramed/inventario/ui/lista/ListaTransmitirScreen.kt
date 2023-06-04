package com.veramed.inventario.ui.lista


import androidx.compose.foundation.layout.*

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home

import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veramed.inventario.InventoryTopAppBar
import com.veramed.inventario.R

import com.veramed.inventario.data.PostListaHomeServer
import com.veramed.inventario.ui.AppViewModelProvider
import com.veramed.inventario.ui.navigation.NavigationDestination
import com.veramed.inventario.ui.theme.InventoryTheme


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
    var visible by remember { mutableStateOf(false) }

    val listaArticulosUiState by viewModel.listaArticulosUIState.collectAsState()

    Scaffold(
        bottomBar = {BottomBar(navigateBack)},
        topBar = {
            InventoryTopAppBar(
                title = stringResource(ListaAgregarItemDestination.titleRes) ,
                canNavigateBack = true

            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    visible=true
                    if (PostListaHomeServer(
                        lista=viewModel.listaTUiState.toLista(0),
                        listaItem=listaArticulosUiState.itemList))
                    {
                        viewModel.guardarLista()
                        navigateBack()
                    } else {visible=false}
                          },
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
        Column() {
            TransmitirBody(
                listaTUiState = viewModel.listaTUiState,
                cantArticulos = listaArticulosUiState.itemList.size,

                modifier = modifier.padding(innerPadding)
            )

                Column(modifier= Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    if (visible) {
                        LinearProgressIndicator(modifier=Modifier.fillMaxWidth())
                    }
                    if (!visible) {
                        LinearProgressIndicator(
                            color= androidx.compose.ui.graphics.Color.Red,
                            modifier=Modifier.fillMaxWidth())
                    }

                }
            }
        }

}

@Composable
fun TransmitirBody(
    listaTUiState: ListaTUiState,
    cantArticulos:Int,
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
                    Text(
                        text = "Articulos :$cantArticulos",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.h6
                    )

                }


            }
        }

    }
}

@Composable
fun BottomBar(navigateBack: () -> Unit) {
    val selectedIndex = remember { mutableStateOf(0) }
    BottomNavigation(elevation = 10.dp) {

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.ArrowBack,"")
        },
            label = { Text(text = "Regresar") },
            selected = (selectedIndex.value == 0),
            onClick = navigateBack
            )

        /*BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Favorite,"")
        },
            label = { Text(text = "Favorite") },
            selected = (selectedIndex.value == 1),
            onClick = {
                selectedIndex.value = 1
            })

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Person,"")
        },
            label = { Text(text = "Profile") },
            selected = (selectedIndex.value == 2),
            onClick = {
                selectedIndex.value = 2
            })*/
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
