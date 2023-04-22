
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
import com.veramed.inventario.ui.item.ItemDetailsDestination
import com.veramed.inventario.ui.item.ItemDetailsScreen
import com.veramed.inventario.ui.item.ItemEntryDestination
import com.veramed.inventario.ui.item.ItemEntryScreen
import com.veramed.inventario.ui.item.ItemEditDestination
import com.veramed.inventario.ui.item.ItemEditScreen
import com.veramed.inventario.ui.lista.ListaEntryDestination
import com.veramed.inventario.ui.lista.ListaEntryScreen
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
                navigateToItemUpdate = {
                    navController.navigate("${ItemDetailsDestination.route}/${it}")
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
        composable(route = ItemEntryDestination.route) {
            ItemEntryScreen(
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
                navigateUp= { navController.navigateUp() },
                navigateToUsuarioEntry = { navController.navigate(UsuarioEntryDestination.route) }
            )
        }


    }
}
