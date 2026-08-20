import SwiftUI

public struct LoanCardView: View {
    public let loan: LoanItem
    public let lang: AppLanguage
    public let onClick: () -> Void
    public let onToggleReturned: () -> Void

    public init(
        loan: LoanItem,
        lang: AppLanguage,
        onClick: @escaping () -> Void,
        onToggleReturned: @escaping () -> Void
    ) {
        self.loan = loan
        self.lang = lang
        self.onClick = onClick
        self.onToggleReturned = onToggleReturned
    }

    private var isLent: Bool { loan.type == .lent }

    public var body: some View {
        Button(action: onClick) {
            VStack(alignment: .leading, spacing: 12) {
                // Top row: Type indicator, Title, Status badge
                HStack(alignment: .center, spacing: 10) {
                    ZStack {
                        RoundedRectangle(cornerRadius: 12)
                            .fill(isLent ? Color.statusGreen.opacity(0.12) : Color.statusBlue.opacity(0.12))
                            .frame(width: 38, height: 38)
                        Image(systemName: isLent ? "arrow.up.right" : "arrow.down.left")
                            .font(.system(size: 16, weight: .bold))
                            .foregroundColor(isLent ? Color.statusGreen : Color.statusBlue)
                    }

                    VStack(alignment: .leading, spacing: 2) {
                        Text(loan.title)
                            .font(.system(size: 16, weight: .bold))
                            .foregroundColor(Color.primary)
                            .lineLimit(1)

                        Text(isLent ? "\(AppStrings.lentTo(lang)) \(loan.contactName)" : "\(AppStrings.borrowedFrom(lang)) \(loan.contactName)")
                            .font(.system(size: 13, weight: .medium))
                            .foregroundColor(Color.appTextSecondaryLight)
                            .lineLimit(1)
                    }

                    Spacer()

                    StatusBadgeView(loan: loan, lang: lang)
                }

                // Value or Category Tag & Due Date
                HStack(spacing: 8) {
                    if let val = loan.valueOrCategory, !val.isEmpty {
                        HStack(spacing: 4) {
                            Image(systemName: "tag.fill")
                                .font(.system(size: 10))
                            Text(val)
                                .font(.system(size: 12, weight: .semibold))
                        }
                        .padding(.horizontal, 8)
                        .padding(.vertical, 4)
                        .background(Color.primaryIndigoContainerLight.opacity(0.5))
                        .foregroundColor(Color.primaryIndigo)
                        .cornerRadius(8)
                    }

                    if let due = loan.dueDate {
                        let dueDateObj = Date(timeIntervalSince1970: Double(due) / 1000.0)
                        let formatter = DateFormatter()
                        let _ = formatter.dateStyle = .medium

                        HStack(spacing: 4) {
                            Image(systemName: "calendar")
                                .font(.system(size: 10))
                            Text(formatter.string(from: dueDateObj))
                                .font(.system(size: 11, weight: .medium))
                        }
                        .foregroundColor(loan.isOverdue ? Color.statusRed : Color.appTextSecondaryLight)
                    }

                    Spacer()

                    // Quick Return toggle button
                    Button(action: onToggleReturned) {
                        Image(systemName: loan.isReturned ? "checkmark.circle.fill" : "circle")
                            .font(.system(size: 20))
                            .foregroundColor(loan.isReturned ? Color.statusGreen : Color.appBorderLight)
                    }
                    .buttonStyle(PlainButtonStyle())
                }

                // Media Attachment Thumbnails preview if present
                if !loan.mediaList.isEmpty || loan.photoPath != nil || loan.audioPath != nil {
                    HStack(spacing: 6) {
                        if loan.photoPath != nil || loan.mediaList.contains(where: { $0.type == .photo }) {
                            Label("Photo", systemImage: "photo.fill")
                                .font(.system(size: 10, weight: .medium))
                                .padding(.horizontal, 6)
                                .padding(.vertical, 2)
                                .background(Color.appCardBorderLight.opacity(0.3))
                                .cornerRadius(6)
                                .foregroundColor(Color.appTextSecondaryLight)
                        }
                        if loan.mediaList.contains(where: { $0.type == .video }) {
                            Label("Vidéo", systemImage: "video.fill")
                                .font(.system(size: 10, weight: .medium))
                                .padding(.horizontal, 6)
                                .padding(.vertical, 2)
                                .background(Color.appCardBorderLight.opacity(0.3))
                                .cornerRadius(6)
                                .foregroundColor(Color.appTextSecondaryLight)
                        }
                        if loan.audioPath != nil {
                            Label("Audio", systemImage: "mic.fill")
                                .font(.system(size: 10, weight: .medium))
                                .padding(.horizontal, 6)
                                .padding(.vertical, 2)
                                .background(Color.appCardBorderLight.opacity(0.3))
                                .cornerRadius(6)
                                .foregroundColor(Color.appTextSecondaryLight)
                        }
                    }
                }
            }
            .padding(14)
            .background(Color.appCardBackgroundLight)
            .overlay(
                RoundedRectangle(cornerRadius: 18)
                    .stroke(Color.appCardBorderLight, lineWidth: 1)
            )
            .cornerRadius(18)
            .shadow(color: Color.black.opacity(0.02), radius: 6, x: 0, y: 2)
        }
        .buttonStyle(PlainButtonStyle())
    }
}
