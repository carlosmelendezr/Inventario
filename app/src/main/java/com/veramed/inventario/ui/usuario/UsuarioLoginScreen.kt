package com.veramed.inventario.ui.usuario

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veramed.inventario.InventoryTopAppBar
import com.veramed.inventario.R
import com.veramed.inventario.ui.AppViewModelProvider
import com.veramed.inventario.ui.item.ItemEditDestination
import com.veramed.inventario.ui.navigation.NavigationDestination
import com.veramed.inventario.ui.theme.InventoryTheme
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale

object UsuarioLoginDestination : NavigationDestination {
    override val route = "usuario_login"
    override val titleRes = R.string.usuario_login

}

@Composable
fun UsuarioLoginScreen(
    navigateToUsuarioEntry: () -> Unit,
    navigateToListaEntry: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: UsuarioLoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(UsuarioLoginDestination.titleRes),
                canNavigateBack = canNavigateBack

            )
        }
    ) { innerPadding ->


            UsuarioLoginBody(
                usuarioUiState = viewModel.usuarioUiState,
                onUsuarioValueChange = viewModel::updateUiState,
                onSaveClick = {

                    coroutineScope.launch {
                        viewModel.buscarUsuario()
                        if (viewModel.usuarioUiState.existe) {
                            Log.d("USR","Usuario CORRECTO ${viewModel.usuarioUiState.usuarioDetails.id}")
                            viewModel.abrirSesion()
                            if (viewModel.sesionOk)  navigateToListaEntry()
                        } else {
                            Log.d("USR","Usuario no existe ${viewModel.usuarioUiState.usuarioDetails.id}")

                        }
                    }
                },
                onRegisterClick = {
                    coroutineScope.launch {
                           navigateToUsuarioEntry()
                    }
                },
                modifier = modifier.padding(innerPadding)
            )

    }
}

@Composable
fun UsuarioLoginBody(
    usuarioUiState: UsuarioUiState,
    onUsuarioValueChange: (UsuarioDetails) -> Unit,
    onSaveClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        UsuarioLoginForm(
            usuarioDetails = usuarioUiState.usuarioDetails,
            onValueChange = onUsuarioValueChange
        )

        Button(
            onClick = onSaveClick,
            enabled = usuarioUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.usuario_login))
        }

        Button(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !usuarioUiState.existe,
        ) {
            Text(stringResource(R.string.usuario_entry))
        }

    }
}



@Composable
fun UsuarioLoginForm(
    usuarioDetails: UsuarioDetails,
    modifier: Modifier = Modifier,
    onValueChange: (UsuarioDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = usuarioDetails.id,
            onValueChange = { onValueChange(usuarioDetails.copy(id = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.usuario_cedula_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = usuarioDetails.password,
            onValueChange = { onValueChange(usuarioDetails.copy(password = it)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = { Text(stringResource(R.string.usuario_password_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )



    }
}


@Preview(showBackground = true)
@Composable
private fun ItemLoginScreenPreview() {
    InventoryTheme {
        UsuarioLoginBody(
            usuarioUiState = UsuarioUiState(
                UsuarioDetails(
                    id="12641955",
                    name = "NO Name",
                    nivel = 0,
                    password = ""

                )
            ),
            onUsuarioValueChange = {},
            onSaveClick = {},
            onRegisterClick = {}
        )
    }
}
