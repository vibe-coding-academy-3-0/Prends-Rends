import SwiftUI

public struct SummaryHeaderView: View {
    public let summary: DashboardSummary
    public let lang: AppLanguage
    @Binding public var selectedTab: TabType
    @Binding public var selectedStatus: StatusFilter

    public init(
        summary: DashboardSummary,
        lang: AppLanguage,
        selectedTab: Binding<TabType>,
        selectedStatus: Binding<StatusFilter>
    ) {
        self.summary = summary
        self.lang = lang
        self._selectedTab = selectedTab
        self._selectedStatus = selectedStatus
    }

    public var body: some View {
        HStack(spacing: 10) {
            // Lent Card
            SummaryStatCard(
                title: AppStrings.statsLent(lang),
                count: summary.totalLentCount,
                icon: "arrow.up.right",
                color: Color.statusGreen,
                isSelected: selectedTab == .lent && selectedStatus != .overdue,
                onClick: {
                    if selectedTab == .lent && selectedStatus != .overdue {
                        selectedTab = .all
                        selectedStatus = .all
                    } else {
                        selectedTab = .lent
                        selectedStatus = .all
                    }
                }
            )

            // Borrowed Card
            SummaryStatCard(
                title: AppStrings.statsBorrowed(lang),
                count: summary.totalBorrowedCount,
                icon: "arrow.down.left",
                color: Color.statusBlue,
                isSelected: selectedTab == .borrowed && selectedStatus != .overdue,
                onClick: {
                    if selectedTab == .borrowed && selectedStatus != .overdue {
                        selectedTab = .all
                        selectedStatus = .all
                    } else {
                        selectedTab = .borrowed
                        selectedStatus = .all
                    }
                }
            )

            // Overdue Card
            SummaryStatCard(
                title: AppStrings.statsOverdue(lang),
                count: summary.totalOverdueCount,
                icon: "exclamationmark.triangle.fill",
                color: Color.statusRed,
                isSelected: selectedStatus == .overdue,
                onClick: {
                    if selectedStatus == .overdue {
                        selectedStatus = .all
                    } else {
                        selectedStatus = .overdue
                    }
                }
            )
        }
        .padding(.horizontal, 16)
    }
}

private struct SummaryStatCard: View {
    let title: String
    let count: Int
    let icon: String
    let color: Color
    let isSelected: Bool
    let onClick: () -> Void

    var body: some View {
        Button(action: onClick) {
            VStack(alignment: .leading, spacing: 10) {
                HStack {
                    ZStack {
                        Circle()
                            .fill(isSelected ? color : color.opacity(0.15))
                            .frame(width: 36, height: 36)
                        Image(systemName: icon)
                            .font(.system(size: 14, weight: .bold))
                            .foregroundColor(isSelected ? .white : color)
                    }

                    Spacer()

                    if isSelected {
                        Text("ACTIF")
                            .font(.system(size: 9, weight: .bold))
                            .foregroundColor(.white)
                            .padding(.horizontal, 6)
                            .padding(.vertical, 2)
                            .background(color)
                            .cornerRadius(10)
                    }
                }

                Text("\(count)")
                    .font(.system(size: 22, weight: .heavy, design: .rounded))
                    .foregroundColor(isSelected ? color : (count > 0 && color == Color.statusRed ? color : Color.primary))

                Text(title)
                    .font(.system(size: 12, weight: isSelected ? .bold : .medium))
                    .foregroundColor(isSelected ? color : Color.appTextSecondaryLight)
                    .lineLimit(1)
            }
            .padding(12)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(isSelected ? color.opacity(0.08) : Color.appCardBackgroundLight)
            .overlay(
                RoundedRectangle(cornerRadius: 20)
                    .stroke(isSelected ? color : Color.appCardBorderLight, lineWidth: isSelected ? 2 : 1)
            )
            .cornerRadius(20)
            .shadow(color: Color.black.opacity(isSelected ? 0.08 : 0.03), radius: isSelected ? 8 : 4, x: 0, y: 2)
        }
        .buttonStyle(PlainButtonStyle())
    }
}
