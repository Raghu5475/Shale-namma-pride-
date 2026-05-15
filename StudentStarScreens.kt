package com.shalenammapride.app.data.repository

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.shalenammapride.app.data.model.DailyMeal
import com.shalenammapride.app.data.model.Facility
import com.shalenammapride.app.data.model.SchoolFeedback
import com.shalenammapride.app.data.model.StudentStar
import com.shalenammapride.app.data.service.FirebaseProvider
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

class SchoolRepository {
    private val db = FirebaseProvider.firestore
    private val storage = FirebaseProvider.storage.reference

    suspend fun todayMeal(): DailyMeal? = mealForDate(today())

    suspend fun mealForDate(date: String): DailyMeal? =
        db.collection("dailyMeals").document(date).get().await().toObject(DailyMeal::class.java)

    suspend fun addDailyMeal(meal: DailyMeal, imageUri: Uri?) {
        val date = meal.date.ifBlank { today() }
        val existing = mealForDate(date)
        require(existing == null) { "A daily meal update already exists for $date." }
        val imageUrl = imageUri?.let { uploadImage("schoolUpdates/dailyMeals/$date.jpg", it) }.orEmpty()
        db.collection("dailyMeals").document(date)
            .set(meal.copy(date = date, imageUrl = imageUrl, createdAt = Timestamp.now()))
            .await()
    }

    suspend fun facilities(): List<Facility> =
        db.collection("facilities").orderBy("createdAt", Query.Direction.DESCENDING)
            .get().await().toObjects(Facility::class.java)

    suspend fun addFacility(facility: Facility, imageUri: Uri?) {
        val doc = db.collection("facilities").document()
        val imageUrl = imageUri?.let { uploadImage("schoolUpdates/facilities/${doc.id}.jpg", it) }.orEmpty()
        doc.set(facility.copy(id = doc.id, imageUrl = imageUrl, createdAt = Timestamp.now())).await()
    }

    suspend fun studentStars(): List<StudentStar> =
        db.collection("studentStars").orderBy("createdAt", Query.Direction.DESCENDING)
            .get().await().toObjects(StudentStar::class.java)

    suspend fun addStudentStar(star: StudentStar, imageUri: Uri?) {
        val doc = db.collection("studentStars").document()
        val imageUrl = imageUri?.let { uploadImage("schoolUpdates/studentStars/${doc.id}.jpg", it) }.orEmpty()
        doc.set(star.copy(id = doc.id, imageUrl = imageUrl, createdAt = Timestamp.now())).await()
    }

    suspend fun submitFeedback(feedback: SchoolFeedback) {
        val doc = db.collection("feedback").document()
        doc.set(feedback.copy(id = doc.id, createdAt = Timestamp.now())).await()
    }

    suspend fun feedback(): List<SchoolFeedback> =
        db.collection("feedback").orderBy("createdAt", Query.Direction.DESCENDING)
            .get().await().toObjects(SchoolFeedback::class.java)

    private suspend fun uploadImage(path: String, uri: Uri): String {
        val ref = storage.child(path.replace(".jpg", "-${UUID.randomUUID()}.jpg"))
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }

    companion object {
        fun today(): String = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    }
}
