package com.veramed.inventario.ui.item

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
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
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
import com.veramed.inventario.data.Item
import com.veramed.inventario.data.ListaItems
import com.veramed.inventario.ui.AppViewModelProvider
import com.veramed.inventario.ui.lista.AgregarItemUiState
import com.veramed.inventario.ui.lista.ListaItemDetails
import com.veramed.inventario.ui.navigation.NavigationDestination
import com.veramed.inventario.ui.theme.InventoryTheme

object ConsultorDestination : NavigationDestination {
    override val route = "consultor"
    override val titleRes = 99
}


@Composable
fun ConsultorScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ConsultorViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {

    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = "Consultor de Precio",
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

        ConsultorEntryBody(item = viewModel.articulo,
            modifier = modifier.padding(innerPadding),

            onItemBuscar = viewModel::buscarBarra, itemUiState = viewModel.listaItemUiState)


    }
}

@Composable
fun ConsultorEntryBody(
    item:Item,
    itemUiState: AgregarItemUiState,
    onValueChange: (ListaItemDetails) -> Unit = {},
    onItemBuscar: () -> Unit,
    modifier: Modifier = Modifier,

    ) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        ConsultarPrecioForm(item= item,
            itemDetails=itemUiState.listaitemDetails,
            onValueChange=onValueChange,
            onItemBuscar=onItemBuscar)



    }
}

@Composable
fun ConsultarPrecioForm(
    item: Item,
    itemDetails: ListaItemDetails,
    onValueChange: (ListaItemDetails) -> Unit = {},
    onItemBuscar: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var enableBarra = !enabled;
    val cantFocusRequester = remember { FocusRequester() }

    val colorEstado: Color
    if (item.name.contains("ERROR"))
    {colorEstado=androidx.compose.ui.graphics.Color.Red} else {colorEstado=androidx.compose.ui.graphics.Color.Green}

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        TextField(
            value = item.name,
            modifier = Modifier
                .fillMaxWidth()
                .background(colorEstado),
            onValueChange = {},
            label = { Text(stringResource(R.string.lista_descrip)) },
            enabled = false,
            singleLine = true
        )

        Card(
            modifier = modifier
                .height(150.dp)
                .fillMaxWidth()
        ) {

            CameraPreview(itemDetails, onValueChange)
        }

        Row() {

            OutlinedTextField(
                value = itemDetails.barra,
                onValueChange = { onValueChange(itemDetails.copy(barra = it)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        onItemBuscar()
                    }
                ),
                modifier = Modifier.width(200.dp),
                label = { Text("BARRA") },
                enabled = enableBarra,
                singleLine = true
            )
           VerPrecio(item = item )        }

        SideEffect {
            if (enabled) {
                cantFocusRequester.requestFocus()
            }
        }



    }
}




@Composable
private fun VerPrecio(
    item: Item,
    modifier: Modifier = Modifier)
 {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 5.dp, horizontal = 5.dp)

    ) {
        Box(modifier= Modifier.weight(2f,fill=true)) {
            Text(
                text = item.sap,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h6
            )
        }
        Spacer(modifier = Modifier.weight(1f,fill = true))
        Box(modifier= Modifier.weight(5f,fill=true)) {
            Text(
                text = item.name,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.h6
            )
        }
        Spacer(modifier = Modifier.weight(1f,fill = true))
        Box(modifier= Modifier.weight(1f,fill=true)) {
            Text(
                text = item.price.toString(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h6
            )
        }
        Spacer(modifier = Modifier.weight(1f,fill = true))
        Box(modifier= Modifier.weight(2f,fill=true)) {
            Text(
                text = item.barra,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.h6
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ListaAgregarItemScreenPreview() {
    InventoryTheme {
        ConsultorScreen(navigateBack = { /*Do nothing*/ },
            onNavigateUp = { /*Do nothing*/ },
        )
    }
}
