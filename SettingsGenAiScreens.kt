package com.shalenammapride.app.data.repository

import com.google.firebase.firestore.SetOptions
import com.shalenammapride.app.data.model.AppLanguage
import com.shalenammapride.app.data.model.UserProfile
import com.shalenammapride.app.data.model.UserRole
import com.shalenammapride.app.data.service.FirebaseProvider
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseProvider.auth
    private val users = FirebaseProvider.firestore.collection("users")

    val currentUid: String? get() = auth.currentUser?.uid

    suspend fun login(email: String, password: String): UserProfile {
        val result = auth.signInWithEmailAndPassword(email.trim(), password).await()
        return getProfile(result.user?.uid.orEmpty()) ?: UserProfile()
    }

    suspend fun signup(email: String, password: String): String {
        val result = auth.createUserWithEmailAndPassword(email.trim(), password).await()
        return result.user?.uid.orEmpty()
    }

    suspend fun saveProfile(profile: UserProfile) {
        users.document(profile.uid).set(profile, SetOptions.merge()).await()
    }

    suspend fun getProfile(uid: String = currentUid.orEmpty()): UserProfile? {
        if (uid.isBlank()) return null
        return users.document(uid).get().await().toObject(UserProfile::class.java)
    }

    suspend fun ensureProfile(
        name: String,
        email: String,
        role: UserRole,
        schoolName: String,
        preferredLanguage: AppLanguage
    ): UserProfile {
        val uid = currentUid.orEmpty()
        val profile = UserProfile(uid, name, email.ifBlank { auth.currentUser?.email.orEmpty() }, role, schoolName, preferredLanguage)
        saveProfile(profile)
        return profile
    }

    fun logout() {
        auth.signOut()
    }
}
