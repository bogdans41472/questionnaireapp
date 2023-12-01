package com.emotionsatwork.questionnaireapp.data

import android.content.res.AssetManager
import com.emotionsatwork.questionnaireapp.datamodel.Question
import com.emotionsatwork.questionnaireapp.datamodel.Questions
import com.google.gson.Gson


class QuestionnaireLoader(private val assetManager: AssetManager) {

    fun loadQuestions(): List<Question> {
        val unserializedQuestionnaire = assetManager.readAssetsFile("questionnaire.json")
        return Gson().fromJson(unserializedQuestionnaire, Questions::class.java).questions
    }

    fun AssetManager.readAssetsFile(fileName: String): String =
        open(fileName).bufferedReader().use { it.readText() }

}