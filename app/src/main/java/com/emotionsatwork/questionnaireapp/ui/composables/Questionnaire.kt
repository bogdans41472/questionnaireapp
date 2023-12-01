package com.emotionsatwork.questionnaireapp.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.emotionsatwork.questionnaireapp.data.AppDatabase
import com.emotionsatwork.questionnaireapp.ui.theme.PrimaryText
import com.emotionsatwork.questionnaireapp.ui.theme.QuestionnaireAppTheme
import com.emotionsatwork.questionnaireapp.ui.viewmodel.QuestionnaireViewModel

@Composable
fun Questionnaire(
    viewModel: QuestionnaireViewModel,
    onQuestionnaireComplete: (Boolean) -> Unit
) {
    QuestionnaireAppTheme {
        Column(modifier = Modifier
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally,
        ) {
            Question(viewModel = viewModel, onQuestionnaireComplete)
        }
    }
}


@Composable
fun Question(viewModel: QuestionnaireViewModel, onQuestionnaireComplete: (Boolean) -> Unit) {
    val question by viewModel.questionFlow.collectAsStateWithLifecycle()
    if (question == null) {
        onQuestionnaireComplete.invoke(true)
    } else {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            elevation = CardDefaults.cardElevation()
        ) {
            Column(horizontalAlignment = CenterHorizontally) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(
                        color = PrimaryText,
                        text = question!!.title,
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    horizontalAlignment = CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(12.dp)
                ) {
                    var sliderPosition by remember(0.0f) { mutableFloatStateOf(0.0f) }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // slider
                            Slider(
                                value = sliderPosition,
                                onValueChange = { sliderPosition = it },
                                steps = 10,
                                valueRange = 0f..10f
                            )
                        }
                    }

                    Button(
                        onClick = {
                            // Get value from radio group
                            viewModel.submitAnswerForQuestion(sliderPosition)
                            sliderPosition = 0f
                        },
                        elevation = ButtonDefaults.buttonElevation(5.dp)
                    ) {
                        val textColor = Color.Unspecified.takeOrElse {
                            LocalTextStyle.current.color.takeOrElse {
                                LocalContentColor.current
                            }
                        }
                        val mergedStyle = LocalTextStyle.current.merge(
                            TextStyle(
                                color = textColor
                            )
                        )
                        BasicText(
                            text = "Submit",
                            modifier = Modifier.padding(16.dp),
                            style = mergedStyle
                        )
                    }

                    Text(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .align(Alignment.End)
                        ,
                        textAlign = TextAlign.End,
                        text = "Question ${question!!.id + 1} out of 40"
                    )
                }
            }
        }
    }
}