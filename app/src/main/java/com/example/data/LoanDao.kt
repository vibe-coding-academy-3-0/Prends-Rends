package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LoanDao {
    @Query("SELECT * FROM loans ORDER BY isReturned ASC, dueDate ASC, createdDate DESC")
    fun getAllLoans(): Flow<List<LoanItem>>

    @Query("SELECT * FROM loans WHERE type = :type ORDER BY isReturned ASC, dueDate ASC, createdDate DESC")
    fun getLoansByType(type: LoanType): Flow<List<LoanItem>>

    @Query("SELECT * FROM loans WHERE id = :id")
    suspend fun getLoanById(id: Long): LoanItem?

    @Query("SELECT * FROM loans WHERE id = :id")
    fun getLoanByIdFlow(id: Long): Flow<LoanItem?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoan(loan: LoanItem): Long

    @Update
    suspend fun updateLoan(loan: LoanItem)

    @Query("UPDATE loans SET isReturned = :isReturned, returnedDate = :returnedDate WHERE id = :id")
    suspend fun setReturnedStatus(id: Long, isReturned: Boolean, returnedDate: Long?)

    @Delete
    suspend fun deleteLoan(loan: LoanItem)

    @Query("DELETE FROM loans WHERE id = :id")
    suspend fun deleteLoanById(id: Long)
}
