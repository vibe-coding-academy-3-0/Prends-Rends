import Foundation
import SwiftUI
import Combine

public final class LoanViewModel: ObservableObject {
    @Published public var currentLanguage: AppLanguage = .fr
    @Published public var currentThemeMode: AppThemeMode = .light
    @Published public var selectedTab: TabType = .all
    @Published public var selectedStatusFilter: StatusFilter = .all
    @Published public var searchQuery: String = ""

    @Published public var allLoans: [LoanItem] = []

    public let storageService = StorageService.shared
    public let notificationService = NotificationService.shared
    public let audioService = AudioRecorderPlayer.shared
    public let firebaseService = FirebaseService.shared

    public init() {
        loadData()
        notificationService.requestAuthorization()
    }

    public func loadData() {
        allLoans = storageService.loadLoans()
    }

    public func setLanguage(_ language: AppLanguage) {
        currentLanguage = language
    }

    public func setThemeMode(_ mode: AppThemeMode) {
        currentThemeMode = mode
    }

    public var filteredLoans: [LoanItem] {
        allLoans.filter { loan in
            // Tab filter
            let matchesTab: Bool
            switch selectedTab {
            case .all: matchesTab = true
            case .lent: matchesTab = (loan.type == .lent)
            case .borrowed: matchesTab = (loan.type == .borrowed)
            }

            // Status filter
            let matchesStatus: Bool
            switch selectedStatusFilter {
            case .all: matchesStatus = true
            case .active: matchesStatus = (!loan.isReturned && !loan.isOverdue)
            case .overdue: matchesStatus = loan.isOverdue
            case .returned: matchesStatus = loan.isReturned
            }

            // Search query
            let query = searchQuery.trimmingCharacters(in: .whitespacesAndNewlines)
            let matchesSearch: Bool
            if query.isEmpty {
                matchesSearch = true
            } else {
                let lower = query.lowercased()
                let titleMatch = loan.title.lowercased().contains(lower)
                let contactMatch = loan.contactName.lowercased().contains(lower)
                let noteMatch = loan.notes?.lowercased().contains(lower) ?? false
                let valueMatch = loan.valueOrCategory?.lowercased().contains(lower) ?? false
                matchesSearch = titleMatch || contactMatch || noteMatch || valueMatch
            }

            return matchesTab && matchesStatus && matchesSearch
        }
    }

    public var dashboardSummary: DashboardSummary {
        let lent = allLoans.filter { $0.type == .lent && !$0.isReturned }.count
        let borrowed = allLoans.filter { $0.type == .borrowed && !$0.isReturned }.count
        let overdue = allLoans.filter { $0.isOverdue }.count
        let returned = allLoans.filter { $0.isReturned }.count
        return DashboardSummary(
            totalLentCount: lent,
            totalBorrowedCount: borrowed,
            totalOverdueCount: overdue,
            totalReturnedCount: returned
        )
    }

    public func saveLoan(
        id: Int64 = 0,
        title: String,
        type: LoanType,
        contactName: String,
        contactPhone: String?,
        contactEmail: String?,
        valueOrCategory: String?,
        photoPath: String?,
        audioPath: String?,
        audioDurationMs: Int64,
        mediaList: [MediaItem],
        dueDate: Int64?,
        notes: String?,
        onSuccess: (Int64) -> Void = { _ in }
    ) {
        let savedId = (id == 0) ? Int64(Date().timeIntervalSince1970 * 1000) : id
        let firstPhoto = photoPath ?? mediaList.first(where: { $0.type == .photo })?.filePath

        let newLoan = LoanItem(
            id: savedId,
            title: title.trimmingCharacters(in: .whitespacesAndNewlines),
            type: type,
            contactName: contactName.trimmingCharacters(in: .whitespacesAndNewlines),
            contactPhone: contactPhone?.trimmingCharacters(in: .whitespacesAndNewlines),
            contactEmail: contactEmail?.trimmingCharacters(in: .whitespacesAndNewlines),
            valueOrCategory: valueOrCategory?.trimmingCharacters(in: .whitespacesAndNewlines),
            photoPath: firstPhoto,
            audioPath: audioPath,
            audioDurationMs: audioDurationMs,
            mediaList: mediaList,
            dueDate: dueDate,
            createdDate: (id == 0) ? Int64(Date().timeIntervalSince1970 * 1000) : (allLoans.first(where: { $0.id == id })?.createdDate ?? Int64(Date().timeIntervalSince1970 * 1000)),
            isReturned: allLoans.first(where: { $0.id == id })?.isReturned ?? false,
            returnedDate: allLoans.first(where: { $0.id == id })?.returnedDate,
            notes: notes?.trimmingCharacters(in: .whitespacesAndNewlines)
        )

        if let index = allLoans.firstIndex(where: { $0.id == savedId }) {
            allLoans[index] = newLoan
        } else {
            allLoans.insert(newLoan, at: 0)
        }

        storageService.saveLoans(allLoans)

        // Schedule or cancel notification reminder
        if let due = dueDate, due > Int64(Date().timeIntervalSince1970 * 1000) {
            notificationService.scheduleReminder(
                loanId: savedId,
                title: newLoan.title,
                contactName: newLoan.contactName,
                isLent: newLoan.type == .lent,
                triggerAtMillis: due
            )
        } else {
            notificationService.cancelReminder(loanId: savedId)
        }

        firebaseService.syncLoans(allLoans) { _ in }
        onSuccess(savedId)
    }

    public func toggleReturnedStatus(loan: LoanItem) {
        guard let index = allLoans.firstIndex(where: { $0.id == loan.id }) else { return }
        var updated = loan
        updated.isReturned.toggle()
        updated.returnedDate = updated.isReturned ? Int64(Date().timeIntervalSince1970 * 1000) : nil

        allLoans[index] = updated
        storageService.saveLoans(allLoans)

        if updated.isReturned {
            notificationService.cancelReminder(loanId: updated.id)
        } else if let due = updated.dueDate, due > Int64(Date().timeIntervalSince1970 * 1000) {
            notificationService.scheduleReminder(
                loanId: updated.id,
                title: updated.title,
                contactName: updated.contactName,
                isLent: updated.type == .lent,
                triggerAtMillis: due
            )
        }

        firebaseService.syncLoans(allLoans) { _ in }
    }

    public func deleteLoan(_ loan: LoanItem, onDeleted: () -> Void = {}) {
        notificationService.cancelReminder(loanId: loan.id)

        // Delete audio and media files
        storageService.deleteMediaFile(path: loan.audioPath)
        storageService.deleteMediaFile(path: loan.photoPath)
        for media in loan.mediaList {
            storageService.deleteMediaFile(path: media.filePath)
        }

        allLoans.removeAll { $0.id == loan.id }
        storageService.saveLoans(allLoans)
        firebaseService.syncLoans(allLoans) { _ in }
        onDeleted()
    }
}
