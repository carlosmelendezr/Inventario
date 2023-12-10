package com.veramed.inventario.ui.item

import android.util.Log
import android.view.KeyEvent.KEYCODE_ENTER
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.key.Key.Companion.Enter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veramed.inventario.InventoryTopAppBar
import com.veramed.inventario.R
import com.veramed.inventario.camara.CameraPreview
import com.veramed.inventario.data.Item
import com.veramed.inventario.ui.AppViewModelProvider
import com.veramed.inventario.ui.lista.AgregarItemUiState
import com.veramed.inventario.ui.lista.ListaItemDetails
import com.veramed.inventario.ui.navigation.NavigationDestination
import com.veramed.inventario.ui.theme.InventoryTheme
import java.text.DecimalFormat

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
        }
    ) { innerPadding ->

        ConsultorEntryBody(item = viewModel.articulo,
            modifier = modifier.padding(innerPadding),
            onValueChange = viewModel::updateUiState,
            onItemBuscar = viewModel::buscarBarra, itemUiState = viewModel.listaItemUiState)


    }
}

@Composable
fun ConsultorEntryBody(
    item:Item,
    itemUiState: AgregarItemUiState,
    onValueChange: (ListaItemDetails) -> Unit,
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

@OptIn(ExperimentalComposeUiApi::class)
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

            //CameraPreview(itemDetails, onValueChange)
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
                modifier = Modifier.width(200.dp)
                .onKeyEvent {
                    when (it.key) {
                        Enter ->  onItemBuscar()
                       else -> Log.d("TECLA", "TECLA DESCONOCIDA")
                    }
                    false
                },
                label = { Text("BARRA") },
                enabled = true,
                singleLine = true
            )
           VerPrecio(item = item )        }


    }
}




@Composable
private fun VerPrecio(
    item: Item,
    modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 5.dp)

    ) {
        /*Box(modifier= Modifier.weight(2f,fill=true)) {
            Text(
                text = item.sap,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h6
            )
        }
        Spacer(modifier = Modifier.weight(1f,fill = true))
        Box(modifier= Modifier.weight(3f,fill=true)) {
            Text(
                text = item.name,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.h6
            )
        }*/
        val iva = item.quantity * item.price / 100
        val total = item.price + iva
        Box(modifier = Modifier.weight(3f, fill = true)) {
            Column() {
                Text(
                    text = item.barra,
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.h6
                )

                if (iva > 0) {
                    Text(
                        text = "Precio sin IVA  ${currencyFormat(item.price)} Bs.",
                        fontWeight = FontWeight.Normal,
                        fontSize = 30.sp,
                        style = MaterialTheme.typography.h6
                    )

                    Text(
                        text = "IVA ${currencyFormat(iva)} Bs.",
                        fontWeight = FontWeight.Normal,
                        fontSize = 30.sp,
                        style = MaterialTheme.typography.h6
                    )
                }
                Text(
                    text = "Precio Total ${currencyFormat(total)} Bs.",
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    style = MaterialTheme.typography.h6
                )
            }

        }
        }


}

fun currencyFormat(amount: Double): String? {
    val formatter = DecimalFormat("###,###,##0.00")
    return formatter.format(amount)
}


@Preview(showBackground = true)
@Composable
fun ListaAgregarItemScreenPreview() {
    InventoryTheme {
        ConsultorScreen(
            navigateBack = { /*Do nothing*/ },
            onNavigateUp = { /*Do nothing*/ },
        )
    }
}
