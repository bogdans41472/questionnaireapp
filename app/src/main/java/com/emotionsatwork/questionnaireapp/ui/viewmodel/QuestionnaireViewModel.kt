package com.emotionsatwork.questionnaireapp.ui.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionsatwork.questionnaireapp.data.PersonalityResultType
import com.emotionsatwork.questionnaireapp.data.QuestionDb
import com.emotionsatwork.questionnaireapp.data.QuestionnaireDao
import com.emotionsatwork.questionnaireapp.data.QuestionnaireLoader
import com.emotionsatwork.questionnaireapp.datamodel.PersonalityType
import com.emotionsatwork.questionnaireapp.datamodel.Question
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CompletableFuture

class QuestionnaireViewModel(
    questionLoader: QuestionnaireLoader,
    private val sharedPreferences: SharedPreferences,
    private val dao: QuestionnaireDao
) : ViewModel() {

    private val questions: List<Question> = questionLoader.loadQuestions()
    private var currentQuestionPosition: Int = getLastAnsweredQuestionIndex()

    private var _question: MutableStateFlow<Question?> = if (currentQuestionPosition != 39) {
        MutableStateFlow(questions[currentQuestionPosition])
    } else {
        MutableStateFlow(null)
    }
    val questionFlow = _question.asStateFlow()

    private val answers = mutableMapOf<Int, Question>()

    fun submitAnswerForQuestion(answer: Float) {
        answers[answer.toInt()] = questionFlow.value!!
        if (currentQuestionPosition < questions.size) {
            val toUpdate = currentQuestionPosition++
            storeLastQuestionIndex(toUpdate)
            storeLastQuestionAnswered(answer)
            val questionToUpdate = questions[toUpdate]
            _question.value = questionToUpdate
            return
        }

        _question.value = null
    }

    fun getResultForUser(): CompletableFuture<PersonalityResultType> {
        val personalityResultType: CompletableFuture<PersonalityResultType> =
            CompletableFuture()
        viewModelScope.launch(Dispatchers.IO) {
            val answers = dao.getAll()

            val personalityTypes = setOf(
                PersonalityType.DOER,
                PersonalityType.UNBREAKABLE,
                PersonalityType.REJECTED,
                PersonalityType.SAVIOR,
                PersonalityType.INSPECTOR,
                PersonalityType.PESSIMIST,
                PersonalityType.CONFORMER,
                PersonalityType.DREAMER
            )
            val result = personalityTypes.map {
                val scoreForPersonality = getScoreForPersonalityType(answers, it)
                scoreForPersonality
            }.maxBy {
                it.values.max()
            }

            personalityResultType.complete(PersonalityResultType(result.keys.first().name))

        }
        return personalityResultType
    }

    private fun getScoreForPersonalityType(
        answers: List<QuestionDb>,
        personalityType: PersonalityType
    ): Map<PersonalityType, Int> =
        mapOf(Pair(personalityType, answers.filter {
            it.personalityType == personalityType
        }.sumOf {
            it.answer
        }))


    private fun getLastAnsweredQuestionIndex(): Int {
        return sharedPreferences.getInt(LAST_QUESTION_INDEX, 0)
    }

    private fun storeLastQuestionIndex(toUpdate: Int) {
        sharedPreferences.edit().putInt(LAST_QUESTION_INDEX, toUpdate)
            .apply()
    }

    private fun storeLastQuestionAnswered(answer: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentQuestion = _question.value!!
            dao.insertQuestion(
                QuestionDb(
                    currentQuestion.id,
                    currentQuestion.title,
                    answer.toInt(),
                    currentQuestion.personalityType
                )
            )
        }
    }

    companion object {
        private const val ANSWERED_QUESTIONS = "QUESTION"
        private const val LAST_QUESTION_INDEX = "LAST_INDEX"
    }
}
