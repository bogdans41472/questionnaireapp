package com.emotionsatwork.questionnaireapp.data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import com.emotionsatwork.questionnaireapp.datamodel.PersonalityType

@Dao
interface QuestionnaireDao {

    @Query("SELECT * FROM QuestionDb")
    fun getAll(): List<QuestionDb>

    @Insert
    fun insertQuestion(question: QuestionDb)

    @Delete
    fun deleteQuestion(question: QuestionDb)

    @Update
    fun updateQuestion(question: QuestionDb)
}

@Entity
data class QuestionDb(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "answer") val answer: Int,
    @ColumnInfo(name = "personalityType") val personalityType: PersonalityType
)