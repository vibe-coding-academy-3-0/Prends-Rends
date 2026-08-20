package com.example.data

import kotlinx.coroutines.flow.Flow

class LoanRepository(private val loanDao: LoanDao) {
    val allLoans: Flow<List<LoanItem>> = loanDao.getAllLoans()

    fun getLoansByType(type: LoanType): Flow<List<LoanItem>> = loanDao.getLoansByType(type)

    fun getLoanByIdFlow(id: Long): Flow<LoanItem?> = loanDao.getLoanByIdFlow(id)

    suspend fun getLoanById(id: Long): LoanItem? = loanDao.getLoanById(id)

    suspend fun insertLoan(loan: LoanItem): Long = loanDao.insertLoan(loan)

    suspend fun updateLoan(loan: LoanItem) = loanDao.updateLoan(loan)

    suspend fun setReturnedStatus(id: Long, isReturned: Boolean) {
        val returnedDate = if (isReturned) System.currentTimeMillis() else null
        loanDao.setReturnedStatus(id, isReturned, returnedDate)
    }

    suspend fun deleteLoan(loan: LoanItem) = loanDao.deleteLoan(loan)

    suspend fun deleteLoanById(id: Long) = loanDao.deleteLoanById(id)
}
