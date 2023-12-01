package com.emotionsatwork.questionnaireapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.emotionsatwork.questionnaireapp.data.AppDatabase
import com.emotionsatwork.questionnaireapp.data.QuestionnaireLoader
import com.emotionsatwork.questionnaireapp.ui.composables.Login
import com.emotionsatwork.questionnaireapp.ui.composables.Questionnaire
import com.emotionsatwork.questionnaireapp.ui.composables.Results
import com.emotionsatwork.questionnaireapp.ui.viewmodel.QuestionnaireViewModel
import com.emotionsatwork.questionnaireapp.ui.theme.QuestionnaireAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuestionnaireAppTheme {
                val navController = rememberNavController()
                val viewModel = QuestionnaireViewModel(QuestionnaireLoader(assetManager = assets),
                    applicationContext.getSharedPreferences(ANSWERS_KEY, MODE_PRIVATE),
                    getDb().questionnaireDao()
                )
                NavHost(navController = navController, startDestination = "auth") {
                    navigation(
                        startDestination = "login",
                        route = "auth"
                    ) {
                        composable("login") {
                            Login(getSharedPreferences(LOGIN_DETAILS, MODE_PRIVATE)) {
                                navController.navigate("questionnaire")
                            }
                        }
                    }
                    navigation(
                        startDestination = "questionnaire_overview",
                        route = "questionnaire"
                    ) {
                        composable("questionnaire_overview") {
                            Questionnaire(viewModel) { questionnaireComplete ->
                                if (questionnaireComplete) {
                                    navController.navigate("results")
                                }
                            }
                        }
                    }
                    navigation(
                        startDestination = "results_overview",
                        route = "results"
                    ) {
                        composable("results_overview") {
                            Results(viewModel)
                            { onExercise ->

                            }
                        }
                    }
                }
            }
        }
    }

    private fun getDb() = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "database-name"
    ).build()

    companion object {
        private const val ANSWERS_KEY = "ANSWERS"
        private const val LOGIN_DETAILS = "LOGIN"
    }
}