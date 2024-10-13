package com.cker.noty.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cker.noty.ui.addorupdatenote.AddOrUpdateNote
import com.cker.noty.ui.addorupdatenote.AddOrUpdateNoteRoute
import com.cker.noty.ui.notes.ListScreen
import com.cker.noty.ui.notes.NotesList

@Composable
fun NotesAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ListScreen) {
        setUpNavGraph(navController)
    }
}


private fun NavGraphBuilder.setUpNavGraph(navController: NavController) {
    composable<ListScreen> {
        NotesList(navController)
    }

    composable<AddOrUpdateNote> {
        AddOrUpdateNoteRoute()
    }
}
