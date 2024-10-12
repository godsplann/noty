package com.cker.noty.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cker.noty.ui.theme.ListScreen

@Composable
fun ToDoApp(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ListScreen) {
        setUpNavGraph()
    }
}


private fun NavGraphBuilder.setUpNavGraph() {
    composable<ListScreen> {
        ListScreen()
    }
}
