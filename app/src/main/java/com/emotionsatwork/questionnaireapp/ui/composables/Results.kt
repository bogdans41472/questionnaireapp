package com.emotionsatwork.questionnaireapp.ui.composables

import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.emotionsatwork.questionnaireapp.ui.viewmodel.QuestionnaireViewModel
import java.util.concurrent.TimeUnit

@Composable
fun Results(
    viewModel: QuestionnaireViewModel,
    onExercise: (Boolean) -> Unit
) {

    val results = viewModel.getResultForUser().get(2, TimeUnit.SECONDS)
    Text(text = "results: ${results.name}")

}

@Composable
fun IndeterminateCircularIndicator(shouldShow: Boolean) {
    val loading by remember(false) { mutableStateOf(shouldShow) }

    if (!loading) return

    CircularProgressIndicator(
        modifier = Modifier.width(64.dp),
        color = Color.Green
    )
}