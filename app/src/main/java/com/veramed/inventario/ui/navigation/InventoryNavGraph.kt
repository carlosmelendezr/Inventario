
package com.veramed.inventario.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.veramed.inventario.ui.home.HomeDestination
import com.veramed.inventario.ui.home.HomeLista
import com.veramed.inventario.ui.home.HomeListaDestino
import com.veramed.inventario.ui.home.HomeScreen
import com.veramed.inventario.ui.item.*
import com.veramed.inventario.ui.lista.*
import com.veramed.inventario.ui.usuario.UsuarioEntryDestination
import com.veramed.inventario.ui.usuario.UsuarioEntryScreen
import com.veramed.inventario.ui.usuario.UsuarioLoginDestination
import com.veramed.inventario.ui.usuario.UsuarioLoginScreen


/**
 * Provides Navigation graph for the application.
 */
@Composable
fun InventoryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = UsuarioLoginDestination.route,
        modifier = modifier
    ) {

        composable(route = HomeListaDestino.route) {
            HomeLista(
                navigateToListaEntry = { navController.navigate(ListaEntryDestination.route) },
                navigateToListaAgregarItem = {
                    navController.navigate("${ListaAgregarItemDestination.route}/${it}")
                }
            )
        }
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemEntry = { navController.navigate(ItemEntryDestination.route) },
                navigateToItemUpdate = {
                    navController.navigate("${ItemDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = ListaAgregarItemDestination.route) {
            ItemEntryScreenOLD(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemDetailsScreen(
                navigateToEditItem = { navController.navigate("${ItemEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = ListaTransmitirDestination.routeWithArgs,
            arguments = listOf(navArgument(ListaTransmitirDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ListaTransmitirScreen(
                navigateBack = { navController.navigate(HomeListaDestino.route)  }
            )
        }

        composable(
            route = ItemEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = ListaAgregarItemDestination.routeWithArgs,
            arguments = listOf(navArgument(ListaAgregarItemDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ListaAgregarItemScreen(
                navigateToTransmitir = {navController.navigate("${ListaTransmitirDestination.route}/${it}")},
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navigateToDetalles = {navController.navigate("${ListaDetalleDestination.route}/${it}")},
            )
        }

        composable(
            route = ListaDetalleDestination.routeWithArgs,
            arguments = listOf(navArgument(ListaDetalleDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ListaDetalleScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(route =ListaEntryDestination.route) {
            ListaEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(route = UsuarioEntryDestination.route) {
            UsuarioEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(route = UsuarioLoginDestination.route) {
            UsuarioLoginScreen(
                navigateToListaEntry  = { navController.navigate(HomeListaDestino.route) },
                navigateToUsuarioEntry = { navController.navigate(UsuarioEntryDestination.route) }
            )
        }


    }
}
