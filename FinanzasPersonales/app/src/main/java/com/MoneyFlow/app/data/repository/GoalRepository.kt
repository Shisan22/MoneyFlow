package com.MoneyFlow.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.MoneyFlow.app.data.model.Goal
import kotlinx.coroutines.tasks.await

class GoalRepository {
    private val db = FirebaseFirestore.getInstance()
    private fun col(uid: String) = db.collection("users").document(uid).collection("goals")

    suspend fun getAll(uid: String): List<Goal> {
        return try {
            col(uid).get().await().toObjects(Goal::class.java)
        } catch (e: Exception) { emptyList() }
    }

    suspend fun save(uid: String, goal: Goal): Result<String> {
        return try {
            val ref = if (goal.id.isEmpty()) col(uid).document()
                      else col(uid).document(goal.id)
            val toSave = goal.copy(id = ref.id, userId = uid)
            ref.set(toSave).await()
            Result.success(ref.id)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun delete(uid: String, id: String): Result<Unit> {
        return try {
            col(uid).document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }
}
