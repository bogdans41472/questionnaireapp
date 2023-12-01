package com.emotionsatwork.questionnaireapp.datamodel

import com.google.gson.annotations.SerializedName

data class Question(
    val id: Int,
    val title: String,
    var answer: Int = 0,
    @SerializedName("type")
    val personalityType: PersonalityType,
)
