package com.example.bellybuddy.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

// This represents a bowel movement with all its associated symptoms
data class BowelMovementWithSymptoms(
    @Embedded
    val bowelMovement: BowelMovement,

    @Relation(
        parentColumn = "bowel_id",
        entityColumn = "symptom_id",
        associateBy = Junction(BowelSymptomLink::class)
    )
    val symptoms: List<Symptom>
)