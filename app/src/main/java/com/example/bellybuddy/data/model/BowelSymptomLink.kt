package com.example.bellybuddy.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "bowel_symptom_link",
    primaryKeys = ["bowel_id", "symptom_id"],  // Composite primary key
    foreignKeys = [
        ForeignKey(
            entity = BowelMovement::class,
            parentColumns = ["bowel_id"],
            childColumns = ["bowel_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Symptom::class,
            parentColumns = ["symptom_id"],
            childColumns = ["symptom_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["bowel_id"]),
        Index(value = ["symptom_id"])
    ]
)
data class BowelSymptomLink(
    @ColumnInfo(name = "bowel_id")
    val bowelId: Int,

    @ColumnInfo(name = "symptom_id")
    val symptomId: Int
)