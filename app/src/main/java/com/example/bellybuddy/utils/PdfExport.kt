package com.example.bellybuddy.utils

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.example.bellybuddy.data.model.BowelMovement
import com.example.bellybuddy.data.model.DailyJournal
import com.example.bellybuddy.data.model.FoodLog
import com.example.bellybuddy.data.model.Symptom
import java.io.File
import java.io.FileOutputStream

fun generatePdf(
    context: Context,
    bowelMovements: List<BowelMovement>,
    dailyJournals: List<DailyJournal>,
    foodLogs: List<FoodLog>,
    symptoms: List<Symptom>
) {
    // TODO: Handle runtime permissions for writing to external storage

    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas
    val paint = android.graphics.Paint()

    // Title
    paint.textSize = 24f
    canvas.drawText("BellyBuddy Report", 20f, 40f, paint)

    var yPosition = 80f

    // Bowel Movements
    paint.textSize = 18f
    canvas.drawText("Bowel Movements", 20f, yPosition, paint)
    yPosition += 20f
    paint.textSize = 12f
    bowelMovements.forEach {
        canvas.drawText("Date: ${it.date}, Consistency: ${it.consistency}, Color: ${it.color}", 20f, yPosition, paint)
        yPosition += 15f
    }
    yPosition += 20f

    // Daily Journals
    paint.textSize = 18f
    canvas.drawText("Daily Journals", 20f, yPosition, paint)
    yPosition += 20f
    paint.textSize = 12f
    dailyJournals.forEach {
        canvas.drawText("Date: ${it.date}, Notes: ${it.notes}", 20f, yPosition, paint)
        yPosition += 15f
    }
    yPosition += 20f

    // Food Logs
    paint.textSize = 18f
    canvas.drawText("Food Logs", 20f, yPosition, paint)
    yPosition += 20f
    paint.textSize = 12f
    foodLogs.forEach {
        canvas.drawText("Date: ${it.date}, Food: ${it.foodName}, Meal: ${it.mealType}", 20f, yPosition, paint)
        yPosition += 15f
    }
    yPosition += 20f

    // Symptoms
    paint.textSize = 18f
    canvas.drawText("Symptoms", 20f, yPosition, paint)
    yPosition += 20f
    paint.textSize = 12f
    symptoms.forEach {
        canvas.drawText("Date: ${it.date}, Type: ${it.symptomType}, Intensity: ${it.intensity}", 20f, yPosition, paint)
        yPosition += 15f
    }

    pdfDocument.finishPage(page)

    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "BellyBuddyReport.pdf")
    try {
        pdfDocument.writeTo(FileOutputStream(file))
    } catch (e: Exception) {
        e.printStackTrace()
    }

    pdfDocument.close()
}
