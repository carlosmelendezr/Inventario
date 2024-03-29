package com.veramed.inventario.ui.lista


import android.util.Log
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veramed.inventario.InventoryTopAppBar
import com.veramed.inventario.R

import com.veramed.inventario.data.PostListaHomeServer
import com.veramed.inventario.data.toAPI
import com.veramed.inventario.ui.AppViewModelProvider
import com.veramed.inventario.ui.navigation.NavigationDestination
import com.veramed.inventario.ui.theme.InventoryTheme
import kotlinx.coroutines.launch


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

    val coroutineScope = rememberCoroutineScope()

    val listaArticulosUiState by viewModel.listaArticulosUIState.collectAsState()

    Scaffold(
        bottomBar = {BottomBar(navigateBack)},
        topBar = {
            InventoryTopAppBar(
                title = stringResource(ListaAgregarItemDestination.titleRes) ,
                canNavigateBack = true

            )
        })

     { innerPadding ->
        Column() {
            TransmitirBody(
                listaTUiState = viewModel.listaTUiState,
                cantArticulos = listaArticulosUiState.itemList.size,
                modifier = modifier.padding(innerPadding)
            )
            Row() {
                if (!viewModel.envioExitoso) {
                    Button(modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            visible = true
                            PostListaHomeServer(
                                listaAPI = viewModel.listaTUiState.toListaAPI(0, 0),
                                listaItem = listaArticulosUiState.itemList, viewModel
                            )
                        }) {
                        Text(text = "COMENZAR TRANSMISION")
                    }
                }

                    if (visible) {
                        LinearProgressIndicator(
                            color = androidx.compose.ui.graphics.Color.Green,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        if (viewModel.envioExitoso && viewModel.tranmisionOk()) {
                            Text(text = "Transmision existosa....")
                            Log.d("TRX","envio exitoso, id=${viewModel.idservidor} count ${viewModel.itemcount}")
                            LaunchedEffect(coroutineScope){
                                viewModel.guardarLista()
                                navigateBack()

                            }
                        }
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
            Row {

                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    // Encabezado

                    Text(
                        text = listaTUiState.descrip,
                        fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h5
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Articulos :$cantArticulos",
                        fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h5
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text( text = "NOTA : DEBE ESTAR CONECTADO A UNA RED INTERNA LOCATEL",
                        fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "TeleEmpleado o T-Tienda",
                        fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(10.dp))

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
