package com.MoneyFlow.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.MoneyFlow.app.data.model.Transaction
import kotlinx.coroutines.tasks.await

class TransactionRepository {
    private val db = FirebaseFirestore.getInstance()
    private fun col(uid: String) = db.collection("users").document(uid).collection("transactions")

    suspend fun getAll(uid: String): List<Transaction> {
        return try {
            col(uid).orderBy("date", Query.Direction.DESCENDING).get().await()
                .toObjects(Transaction::class.java)
        } catch (e: Exception) { emptyList() }
    }

    suspend fun getById(uid: String, id: String): Transaction? {
        return try {
            col(uid).document(id).get().await().toObject(Transaction::class.java)
        } catch (e: Exception) { null }
    }

    suspend fun save(uid: String, transaction: Transaction): Result<String> {
        return try {
            val ref = if (transaction.id.isEmpty()) col(uid).document()
                      else col(uid).document(transaction.id)
            val toSave = transaction.copy(id = ref.id, userId = uid)
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
