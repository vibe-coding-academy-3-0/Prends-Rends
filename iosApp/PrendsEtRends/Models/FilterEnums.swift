import Foundation

public enum TabType: String, CaseIterable, Identifiable {
    case all = "ALL"
    case lent = "LENT"
    case borrowed = "BORROWED"

    public var id: String { rawValue }
}

public enum StatusFilter: String, CaseIterable, Identifiable {
    case all = "ALL"
    case active = "ACTIVE"
    case overdue = "OVERDUE"
    case returned = "RETURNED"

    public var id: String { rawValue }
}

public struct DashboardSummary: Equatable {
    public var totalLentCount: Int = 0
    public var totalBorrowedCount: Int = 0
    public var totalOverdueCount: Int = 0
    public var totalReturnedCount: Int = 0

    public init(
        totalLentCount: Int = 0,
        totalBorrowedCount: Int = 0,
        totalOverdueCount: Int = 0,
        totalReturnedCount: Int = 0
    ) {
        self.totalLentCount = totalLentCount
        self.totalBorrowedCount = totalBorrowedCount
        self.totalOverdueCount = totalOverdueCount
        self.totalReturnedCount = totalReturnedCount
    }
}
