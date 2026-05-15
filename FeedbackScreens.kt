package com.shalenammapride.app.data.model

import com.google.firebase.Timestamp

enum class AppLanguage { ENGLISH, KANNADA }
enum class UserRole { ADMIN, HEADMASTER, SDMC, PARENT }
enum class FeedbackCategory { MEAL, FACILITY, TEACHING, SAFETY, OTHER }
enum class FacilityCategory { CLASSROOMS, SMART_CLASSES, LABS, LIBRARY, TOILETS, PLAYGROUND }

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: UserRole = UserRole.PARENT,
    val schoolName: String = "",
    val preferredLanguage: AppLanguage = AppLanguage.ENGLISH
) {
    val canAddUpdates: Boolean get() = role != UserRole.PARENT
}

data class DailyMeal(
    val date: String = "",
    val imageUrl: String = "",
    val menuEnglish: String = "",
    val menuKannada: String = "",
    val descriptionEnglish: String = "",
    val descriptionKannada: String = "",
    val uploadedBy: String = "",
    val createdAt: Timestamp? = null
)

data class Facility(
    val id: String = "",
    val titleEnglish: String = "",
    val titleKannada: String = "",
    val descriptionEnglish: String = "",
    val descriptionKannada: String = "",
    val imageUrl: String = "",
    val category: FacilityCategory = FacilityCategory.CLASSROOMS,
    val createdAt: Timestamp? = null
)

data class StudentStar(
    val id: String = "",
    val studentName: String = "",
    val titleEnglish: String = "",
    val titleKannada: String = "",
    val descriptionEnglish: String = "",
    val descriptionKannada: String = "",
    val imageUrl: String = "",
    val createdAt: Timestamp? = null
)

data class SchoolFeedback(
    val id: String = "",
    val message: String = "",
    val category: FeedbackCategory = FeedbackCategory.OTHER,
    val anonymous: Boolean = false,
    val userId: String = "",
    val userName: String? = null,
    val createdAt: Timestamp? = null
)

data class AnnouncementDraft(
    val input: String = "",
    val english: String = "",
    val kannada: String = ""
)
