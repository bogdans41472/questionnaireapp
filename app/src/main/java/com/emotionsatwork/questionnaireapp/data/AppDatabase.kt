package com.emotionsatwork.questionnaireapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [QuestionDb::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun questionnaireDao(): QuestionnaireDao
}