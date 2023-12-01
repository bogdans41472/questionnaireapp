package com.emotionsatwork.questionnaireapp.datamodel

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

enum class PersonalityType {
    CONFORMER,
    INSPECTOR,
    UNBREAKABLE,
    DREAMER,
    PESSIMIST,
    REJECTED,
    DOER,
    SAVIOR,
}