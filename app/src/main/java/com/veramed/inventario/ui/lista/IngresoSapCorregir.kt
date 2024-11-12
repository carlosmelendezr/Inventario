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
import com.veramed.inventario.data.ArticuloSap
import com.veramed.inventario.data.Moveventos
import com.veramed.inventario.ui.AppViewModelProvider
import com.veramed.inventario.ui.navigation.NavigationDestination
import com.veramed.inventario.ui.theme.InventoryTheme

import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object IngrespSapCorregirDestination : NavigationDestination {
    override val route = "ingreso_sap_corregir"
    override val titleRes = R.string.item_detail_title
  }

@Composable
fun IngrespSapCorregirScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListaIngresoSapViewModel= viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.itemCorregir

    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(ListaDetalleDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },

        ) { innerPadding ->
        ItemCorregirBody(
            itemCorregir = uiState,
            onValueChange = viewModel::actualizaUiState ,
            onSaveItem = {
                viewModel.guardaItemError()
                navigateBack()
            },

            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun ItemCorregirBody(
    itemCorregir: MovEventoDetalle,
    onSaveItem: () -> Unit,
    onValueChange: (MovEventoDetalle) -> Unit ,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        EditItemSapInputForm(itemCorregir = itemCorregir,
            enabled = false,
            onValueChange = onValueChange)

        Button(
            onClick = onSaveItem,
            enabled = true,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}
@Composable
fun EditItemSapInputForm(
    itemCorregir: MovEventoDetalle,
    modifier: Modifier = Modifier,
    onValueChange: (MovEventoDetalle) -> Unit ,
    enabled: Boolean = true
) {

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(5.dp)) {
        OutlinedTextField(
            value = itemCorregir.descrip,
            onValueChange = { },
            label = { Text(stringResource(R.string.item_name_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            singleLine = true
        )
        Row() {
            OutlinedTextField(
                value = itemCorregir.sap,
                modifier = Modifier.width(150.dp),
                onValueChange = {},
                label = { Text(stringResource(R.string.sap)) },
                enabled = false,
                singleLine = true
            )


        }
        Row() {
            OutlinedTextField(
                value = itemCorregir.cantidad,
                modifier = Modifier.width(120.dp),
                onValueChange = { onValueChange(itemCorregir.copy(cantidad =it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(stringResource(R.string.quantity_req)) },
                enabled = false,
                singleLine = true
            )

        }


    }

}



@Preview(showBackground = true)
@Composable
fun IngrespSapCorregirScreenPreview() {
    InventoryTheme {
        ItemCorregirBody(
            itemCorregir = MovEventoDetalle(),
            onSaveItem = {},
            onValueChange = {}
        )
    }
}
