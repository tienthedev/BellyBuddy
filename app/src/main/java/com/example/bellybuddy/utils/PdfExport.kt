package com.example.bellybuddy.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.example.bellybuddy.data.model.BowelMovement
import com.example.bellybuddy.data.model.DailyJournal
import com.example.bellybuddy.data.model.FoodLog
import com.example.bellybuddy.data.model.Symptom
import com.example.bellybuddy.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

suspend fun generatePdf(
    context: Context,
    user: User?,
    bowelMovements: List<BowelMovement>,
    dailyJournals: List<DailyJournal>,
    foodLogs: List<FoodLog>,
    symptoms: List<Symptom>,
    onProgress: (Float) -> Unit
) = withContext(Dispatchers.IO) {
    val pdfDocument = PdfDocument()
    val pageWidth = 595
    val pageHeight = 842
    var pageNumber = 1
    
    val accentColor = Color.rgb(76, 175, 80) // BellyGreen-ish
    val textColor = Color.rgb(33, 33, 33)
    val secondaryTextColor = Color.rgb(117, 117, 117)

    val titlePaint = Paint().apply {
        color = accentColor
        textSize = 28f
        isFakeBoldText = true
    }
    val headerPaint = Paint().apply {
        color = accentColor
        textSize = 18f
        isFakeBoldText = true
    }
    val subHeaderPaint = Paint().apply {
        color = textColor
        textSize = 14f
        isFakeBoldText = true
    }
    val textPaint = Paint().apply {
        color = textColor
        textSize = 11f
    }
    val smallTextPaint = Paint().apply {
        color = secondaryTextColor
        textSize = 9f
    }
    val linePaint = Paint().apply {
        color = Color.LTGRAY
        strokeWidth = 1f
    }

    var currentPage = pdfDocument.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
    var canvas = currentPage.canvas
    var yPosition = 50f

    onProgress(0.05f)

    // Header and Logo area
    canvas.drawText("BellyBuddy", 40f, yPosition, titlePaint)
    val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    canvas.drawText("Generated on: ${dateFormat.format(Date())}", 40f, yPosition + 20f, smallTextPaint)
    yPosition += 60f

    // Profile Section
    canvas.drawText("PATIENT PROFILE", 40f, yPosition, subHeaderPaint)
    yPosition += 10f
    canvas.drawLine(40f, yPosition, pageWidth - 40f, yPosition, linePaint)
    yPosition += 25f

    val nameStr = user?.name ?: "N/A"
    val ageStr = user?.age?.toString() ?: "N/A"
    val weightStr = user?.weight?.toString() ?: "N/A"
    
    canvas.drawText("Name: $nameStr", 40f, yPosition, textPaint)
    canvas.drawText("Age: $ageStr", 200f, yPosition, textPaint)
    canvas.drawText("Weight: $weightStr lbs", 350f, yPosition, textPaint)
    yPosition += 40f

    fun checkNewPage(neededHeight: Float = 40f) {
        if (yPosition > pageHeight - 60 - neededHeight) {
            pdfDocument.finishPage(currentPage)
            pageNumber++
            currentPage = pdfDocument.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
            canvas = currentPage.canvas
            yPosition = 50f
            // Redraw header on new page
            canvas.drawText("BellyBuddy Report - Page $pageNumber", 40f, yPosition, smallTextPaint)
            yPosition += 30f
        }
    }

    // Bowel Movements Section
    checkNewPage(60f)
    canvas.drawText("Bowel Movements", 40f, yPosition, headerPaint)
    yPosition += 10f
    canvas.drawLine(40f, yPosition, pageWidth - 40f, yPosition, linePaint)
    yPosition += 25f

    if (bowelMovements.isEmpty()) {
        canvas.drawText("No data recorded.", 40f, yPosition, textPaint)
        yPosition += 20f
    } else {
        bowelMovements.forEach {
            checkNewPage(20f)
            canvas.drawText("• ${it.date}: Consistency: ${it.consistency}, Color: ${it.color}", 50f, yPosition, textPaint)
            yPosition += 20f
        }
    }
    yPosition += 20f
    onProgress(0.3f)

    // Daily Journals Section
    checkNewPage(60f)
    canvas.drawText("Daily Journals", 40f, yPosition, headerPaint)
    yPosition += 10f
    canvas.drawLine(40f, yPosition, pageWidth - 40f, yPosition, linePaint)
    yPosition += 25f

    if (dailyJournals.isEmpty()) {
        canvas.drawText("No data recorded.", 40f, yPosition, textPaint)
        yPosition += 20f
    } else {
        dailyJournals.forEach {
            checkNewPage(30f)
            canvas.drawText("• ${it.date}:", 50f, yPosition, subHeaderPaint)
            yPosition += 15f
            val notes = if (it.notes.length > 80) it.notes.take(77) + "..." else it.notes
            canvas.drawText("  $notes", 50f, yPosition, textPaint)
            yPosition += 25f
        }
    }
    yPosition += 20f
    onProgress(0.5f)

    // Food Logs Section
    checkNewPage(60f)
    canvas.drawText("Food & Nutrition Logs", 40f, yPosition, headerPaint)
    yPosition += 10f
    canvas.drawLine(40f, yPosition, pageWidth - 40f, yPosition, linePaint)
    yPosition += 25f

    if (foodLogs.isEmpty()) {
        canvas.drawText("No data recorded.", 40f, yPosition, textPaint)
        yPosition += 20f
    } else {
        foodLogs.forEach {
            checkNewPage(20f)
            canvas.drawText("• ${it.date} (${it.mealType}): ${it.foodName}", 50f, yPosition, textPaint)
            yPosition += 20f
        }
    }
    yPosition += 20f
    onProgress(0.7f)

    // Symptoms Section
    checkNewPage(60f)
    canvas.drawText("Symptom Tracking", 40f, yPosition, headerPaint)
    yPosition += 10f
    canvas.drawLine(40f, yPosition, pageWidth - 40f, yPosition, linePaint)
    yPosition += 25f

    if (symptoms.isEmpty()) {
        canvas.drawText("No data recorded.", 40f, yPosition, textPaint)
        yPosition += 20f
    } else {
        symptoms.forEach {
            checkNewPage(20f)
            canvas.drawText("• ${it.date}: ${it.symptomType} (Intensity: ${it.intensity}/10)", 50f, yPosition, textPaint)
            yPosition += 20f
        }
    }
    onProgress(0.9f)

    pdfDocument.finishPage(currentPage)

    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "BellyBuddy_Report_${System.currentTimeMillis()}.pdf")
    try {
        pdfDocument.writeTo(FileOutputStream(file))
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        pdfDocument.close()
    }
    onProgress(1.0f)
}
