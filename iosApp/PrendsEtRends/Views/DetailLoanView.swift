import SwiftUI

public struct DetailLoanView: View {
    @ObservedObject public var viewModel: LoanViewModel
    public let loan: LoanItem
    @Environment(\.presentationMode) private var presentationMode

    @State private var showEditSheet: Bool = false
    @State private var showDeleteConfirmation: Bool = false

    public init(viewModel: LoanViewModel, loan: LoanItem) {
        self.viewModel = viewModel
        self.loan = loan
    }

    private var currentLoan: LoanItem {
        viewModel.allLoans.first(where: { $0.id == loan.id }) ?? loan
    }

    private var lang: AppLanguage { viewModel.currentLanguage }
    private var isLent: Bool { currentLoan.type == .lent }

    public var body: some View {
        NavigationView {
            ZStack {
                Color.appSurfaceLight
                    .ignoresSafeArea()

                ScrollView {
                    VStack(spacing: 16) {
                        // 1. Main Info Header Card
                        VStack(alignment: .leading, spacing: 14) {
                            HStack {
                                HStack(spacing: 6) {
                                    Image(systemName: isLent ? "arrow.up.right" : "arrow.down.left")
                                        .font(.system(size: 13, weight: .bold))
                                    Text(isLent ? AppStrings.typeLent(lang) : AppStrings.typeBorrowed(lang))
                                        .font(.system(size: 13, weight: .bold))
                                }
                                .foregroundColor(isLent ? Color.statusGreen : Color.statusBlue)
                                .padding(.horizontal, 10)
                                .padding(.vertical, 5)
                                .background(isLent ? Color.statusGreenContainer : Color.statusBlueContainer)
                                .cornerRadius(12)

                                Spacer()

                                StatusBadgeView(loan: currentLoan, lang: lang)
                            }

                            Text(currentLoan.title)
                                .font(.system(size: 22, weight: .heavy, design: .rounded))
                                .foregroundColor(Color.primary)

                            if let val = currentLoan.valueOrCategory, !val.isEmpty {
                                HStack(spacing: 6) {
                                    Image(systemName: "tag.fill")
                                        .font(.system(size: 12))
                                    Text(val)
                                        .font(.system(size: 14, weight: .semibold))
                                }
                                .foregroundColor(Color.primaryIndigo)
                                .padding(.horizontal, 10)
                                .padding(.vertical, 5)
                                .background(Color.primaryIndigoContainerLight)
                                .cornerRadius(10)
                            }

                            Divider()

                            // Dates Row
                            HStack(spacing: 20) {
                                VStack(alignment: .leading, spacing: 2) {
                                    Text("Enregistré le")
                                        .font(.system(size: 11))
                                        .foregroundColor(Color.appTextSecondaryLight)
                                    Text(formatDate(currentLoan.createdDate))
                                        .font(.system(size: 13, weight: .semibold))
                                }

                                if let due = currentLoan.dueDate {
                                    VStack(alignment: .leading, spacing: 2) {
                                        Text("Date de retour")
                                            .font(.system(size: 11))
                                            .foregroundColor(Color.appTextSecondaryLight)
                                        Text(formatDate(due))
                                            .font(.system(size: 13, weight: .semibold))
                                            .foregroundColor(currentLoan.isOverdue ? Color.statusRed : Color.primary)
                                    }
                                }
                            }
                        }
                        .padding(16)
                        .background(Color.appCardBackgroundLight)
                        .overlay(
                            RoundedRectangle(cornerRadius: 20)
                                .stroke(Color.appCardBorderLight, lineWidth: 1)
                        )
                        .cornerRadius(20)

                        // 2. Contact Section & Quick Shortcuts
                        VStack(alignment: .leading, spacing: 14) {
                            HStack(spacing: 12) {
                                ZStack {
                                    Circle()
                                        .fill(Color.primaryIndigoContainerLight)
                                        .frame(width: 44, height: 44)
                                    Text(String(currentLoan.contactName.prefix(1)).uppercased())
                                        .font(.system(size: 18, weight: .bold))
                                        .foregroundColor(Color.primaryIndigo)
                                }

                                VStack(alignment: .leading, spacing: 2) {
                                    Text(isLent ? AppStrings.lentTo(lang) : AppStrings.borrowedFrom(lang))
                                        .font(.system(size: 11, weight: .medium))
                                        .foregroundColor(Color.appTextSecondaryLight)
                                    Text(currentLoan.contactName)
                                        .font(.system(size: 16, weight: .bold))
                                        .foregroundColor(Color.primary)
                                    if let phone = currentLoan.contactPhone, !phone.isEmpty {
                                        Text(phone)
                                            .font(.system(size: 13))
                                            .foregroundColor(Color.appTextSecondaryLight)
                                    }
                                }

                                Spacer()
                            }

                            // Quick Action Buttons (Call, SMS, WhatsApp)
                            if let phone = currentLoan.contactPhone, !phone.isEmpty {
                                HStack(spacing: 10) {
                                    ContactShortcutButton(
                                        title: AppStrings.callBtn(lang),
                                        icon: "phone.fill",
                                        color: Color.statusGreen,
                                        urlStr: "tel://\(phone.replacingOccurrences(of: " ", with: ""))"
                                    )
                                    ContactShortcutButton(
                                        title: AppStrings.smsAction(lang),
                                        icon: "message.fill",
                                        color: Color.accentCyan,
                                        urlStr: "sms:\(phone.replacingOccurrences(of: " ", with: ""))"
                                    )
                                    ContactShortcutButton(
                                        title: AppStrings.whatsappAction(lang),
                                        icon: "bubble.left.and.bubble.right.fill",
                                        color: Color(hex: 0x25D366),
                                        urlStr: "whatsapp://send?phone=\(phone.replacingOccurrences(of: " ", with: "").replacingOccurrences(of: "+", with: ""))"
                                    )
                                }
                            }
                        }
                        .padding(16)
                        .background(Color.appCardBackgroundLight)
                        .overlay(
                            RoundedRectangle(cornerRadius: 20)
                                .stroke(Color.appCardBorderLight, lineWidth: 1)
                        )
                        .cornerRadius(20)

                        // 3. Media Carousel
                        if !currentLoan.mediaList.isEmpty {
                            VStack(alignment: .leading, spacing: 10) {
                                Text(AppStrings.mediaHeader(lang))
                                    .font(.system(size: 14, weight: .bold))
                                    .foregroundColor(Color.primary)

                                MediaGalleryCarouselView(mediaList: currentLoan.mediaList)
                            }
                            .padding(16)
                            .background(Color.appCardBackgroundLight)
                            .overlay(
                                RoundedRectangle(cornerRadius: 20)
                                    .stroke(Color.appCardBorderLight, lineWidth: 1)
                            )
                            .cornerRadius(20)
                        }

                        // 4. Voice Note Card
                        if let audio = currentLoan.audioPath, !audio.isEmpty {
                            VStack(alignment: .leading, spacing: 10) {
                                Text(AppStrings.voiceNoteHeader(lang))
                                    .font(.system(size: 14, weight: .bold))
                                    .foregroundColor(Color.primary)

                                AudioPlayerCardView(audioPath: audio, durationMs: currentLoan.audioDurationMs)
                            }
                            .padding(16)
                            .background(Color.appCardBackgroundLight)
                            .overlay(
                                RoundedRectangle(cornerRadius: 20)
                                    .stroke(Color.appCardBorderLight, lineWidth: 1)
                            )
                            .cornerRadius(20)
                        }

                        // 5. Notes Card
                        if let notes = currentLoan.notes, !notes.isEmpty {
                            VStack(alignment: .leading, spacing: 10) {
                                Text(AppStrings.notesLabel(lang))
                                    .font(.system(size: 14, weight: .bold))
                                    .foregroundColor(Color.primary)
                                Text(notes)
                                    .font(.system(size: 14))
                                    .foregroundColor(Color.primary)
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding(16)
                            .background(Color.appCardBackgroundLight)
                            .overlay(
                                RoundedRectangle(cornerRadius: 20)
                                    .stroke(Color.appCardBorderLight, lineWidth: 1)
                            )
                            .cornerRadius(20)
                        }

                        // 6. Mark as Returned / Active Toggle Button
                        Button(action: {
                            viewModel.toggleReturnedStatus(loan: currentLoan)
                        }) {
                            HStack(spacing: 8) {
                                Image(systemName: currentLoan.isReturned ? "arrow.uturn.backward" : "checkmark.circle.fill")
                                    .font(.system(size: 16, weight: .bold))
                                Text(currentLoan.isReturned ? AppStrings.markActiveBtn(lang) : AppStrings.markReturnedBtn(lang))
                                    .font(.system(size: 15, weight: .bold))
                            }
                            .foregroundColor(.white)
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 15)
                            .background(currentLoan.isReturned ? Color.appTextSecondaryLight : Color.statusGreen)
                            .cornerRadius(18)
                            .shadow(color: (currentLoan.isReturned ? Color.gray : Color.statusGreen).opacity(0.3), radius: 8, x: 0, y: 4)
                        }
                        .padding(.top, 6)
                        .padding(.bottom, 30)
                    }
                    .padding(16)
                }
            }
            .navigationTitle(AppStrings.detailTitle(lang))
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarItems(
                leading: Button(action: { presentationMode.wrappedValue.dismiss() }) {
                    Image(systemName: "xmark")
                        .foregroundColor(Color.primary)
                },
                trailing: HStack(spacing: 16) {
                    Button(action: { showEditSheet = true }) {
                        Image(systemName: "pencil")
                            .foregroundColor(Color.primaryIndigo)
                    }
                    Button(action: { showDeleteConfirmation = true }) {
                        Image(systemName: "trash")
                            .foregroundColor(Color.red)
                    }
                }
            )
            .sheet(isPresented: $showEditSheet) {
                CreateEditLoanView(viewModel: viewModel, existingLoan: currentLoan)
            }
            .alert(isPresented: $showDeleteConfirmation) {
                Alert(
                    title: Text(AppStrings.deleteConfirmTitle(lang)),
                    message: Text(AppStrings.deleteConfirmDesc(lang, title: currentLoan.title)),
                    primaryButton: .destructive(Text(AppStrings.deleteBtn(lang))) {
                        viewModel.deleteLoan(currentLoan) {
                            presentationMode.wrappedValue.dismiss()
                        }
                    },
                    secondaryButton: .cancel(Text(AppStrings.cancelBtn(lang)))
                )
            }
        }
    }

    private func formatDate(_ millis: Int64) -> String {
        let date = Date(timeIntervalSince1970: Double(millis) / 1000.0)
        let formatter = DateFormatter()
        formatter.dateStyle = .medium
        formatter.timeStyle = .none
        return formatter.string(from: date)
    }
}

private struct ContactShortcutButton: View {
    let title: String
    let icon: String
    let color: Color
    let urlStr: String

    var body: some View {
        Button(action: {
            if let url = URL(string: urlStr), UIApplication.shared.canOpenURL(url) {
                UIApplication.shared.open(url)
            }
        }) {
            HStack(spacing: 6) {
                Image(systemName: icon)
                    .font(.system(size: 13, weight: .bold))
                Text(title)
                    .font(.system(size: 12, weight: .bold))
            }
            .foregroundColor(color)
            .frame(maxWidth: .infinity)
            .padding(.vertical, 9)
            .background(color.opacity(0.12))
            .cornerRadius(12)
        }
    }
}
