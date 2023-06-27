package com.veramed.inventario.ui.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource


import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.navigation.NavHostController
import com.veramed.inventario.R
import com.veramed.inventario.ui.navigation.NavigationDestination

import com.veramed.inventario.ui.theme.Purple700
import com.veramed.inventario.ui.usuario.UsuarioLoginDestination
import kotlinx.coroutines.delay

object SplashDestino : NavigationDestination {
    override val route = "splash"
    override val titleRes = R.string.app_lista
}


@Composable
fun AnimatedSplashScreen(navController: NavHostController) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000
        )
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(4000)
        navController.popBackStack()
        navController.navigate(UsuarioLoginDestination.route)
    }
    Splash(alpha = alphaAnim.value)
}

@Composable
fun Splash(alpha: Float) {
    Box(
        modifier = Modifier
            .background(if (isSystemInDarkTheme()) Color.Black else Purple700)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {


        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text="FARMACIA VERAMED")
            Text(text="Aplicación Móvil de Inventario")
            Spacer(modifier = Modifier.height(32.dp))

            Image(painterResource(R.drawable.logoveramed),
                "Veramed")
            Text(text="Version 1.2")

        }


    }
}

@Composable
@Preview
fun SplashScreenPreview() {
    Splash(alpha = 1f)
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES)
fun SplashScreenDarkPreview() {
    Splash(alpha = 1f)
}