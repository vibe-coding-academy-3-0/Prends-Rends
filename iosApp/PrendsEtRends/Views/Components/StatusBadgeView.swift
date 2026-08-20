import SwiftUI

public struct StatusBadgeView: View {
    public let loan: LoanItem
    public let lang: AppLanguage

    public init(loan: LoanItem, lang: AppLanguage) {
        self.loan = loan
        self.lang = lang
    }

    public var body: some View {
        HStack(spacing: 4) {
            if loan.isReturned {
                Image(systemName: "checkmark.circle.fill")
                    .font(.system(size: 11, weight: .bold))
                    .foregroundColor(Color.statusGreen)
                Text(AppStrings.filterReturned(lang))
                    .font(.system(size: 11, weight: .bold))
                    .foregroundColor(Color.statusGreen)
            } else if loan.isOverdue {
                Image(systemName: "exclamationmark.triangle.fill")
                    .font(.system(size: 11, weight: .bold))
                    .foregroundColor(Color.statusRed)
                Text(AppStrings.filterOverdue(lang))
                    .font(.system(size: 11, weight: .bold))
                    .foregroundColor(Color.statusRed)
            } else {
                Image(systemName: "clock.fill")
                    .font(.system(size: 11, weight: .bold))
                    .foregroundColor(Color.statusBlue)
                Text(AppStrings.filterActive(lang))
                    .font(.system(size: 11, weight: .bold))
                    .foregroundColor(Color.statusBlue)
            }
        }
        .padding(.horizontal, 8)
        .padding(.vertical, 4)
        .background(
            loan.isReturned ? Color.statusGreenContainer :
            loan.isOverdue ? Color.statusRedContainer :
            Color.statusBlueContainer
        )
        .overlay(
            RoundedRectangle(cornerRadius: 12)
                .stroke(
                    loan.isReturned ? Color.statusGreenBorder :
                    loan.isOverdue ? Color.statusRedBorder :
                    Color.statusBlueBorder,
                    lineWidth: 1
                )
        )
        .cornerRadius(12)
    }
}
