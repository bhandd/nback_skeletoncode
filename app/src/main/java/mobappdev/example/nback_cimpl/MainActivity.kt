package mobappdev.example.nback_cimpl

import GameScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mobappdev.example.nback_cimpl.ui.screens.HomeScreen
import mobappdev.example.nback_cimpl.ui.theme.NBack_CImplTheme
import mobappdev.example.nback_cimpl.ui.viewmodels.FakeVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameType
import mobappdev.example.nback_cimpl.ui.viewmodels.GameVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel
import java.util.Locale

/**
 * This is the MainActivity of the application
 *
 * Your navigation between the two (or more) screens should be handled here
 * For this application you need at least a homescreen (a start is already made for you)
 * and a gamescreen (you will have to make yourself, but you can use the same viewmodel)
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NBack_CImplTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Instantiate the viewmodel
                    val gameViewModel: GameVM = viewModel(
                        factory = GameVM.Factory
                    )

                    // Instantiate the homescreen
                    //HomeScreen(vm = gameViewModel)/
                    Navigation(gameViewModel)

                }


            }
        }
    }
}


@Composable
fun Navigation(gameVM: GameVM) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "HomeScreen") {
        composable("GameScreen/{gameType}") { backStackEntry ->
            val gameType = GameType.valueOf(backStackEntry.arguments?.getString("gameType")?.uppercase(Locale.getDefault()) ?: "VISUAL")

            GameScreen(
                vm = gameVM,
                navController = navController,
                gameType = gameType,
                navigateToHomeScreen = { navController.popBackStack()}
            )
        }
        composable("HomeScreen") {
            HomeScreen(
                onVisualButtonClicked = {
                    navController.navigate("GameScreen/visual")
                },
                onAudioButtonClicked = {
                    navController.navigate("GameScreen/audio")
                },
                vm = gameVM
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    NBack_CImplTheme {
//        HomeScreen(
//            onVisualButtonClicked = {},
//            onAudioButtonClicked = {},
//            vm = FakeVM()
//        )
//    }
//}