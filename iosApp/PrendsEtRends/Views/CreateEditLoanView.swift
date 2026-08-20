import SwiftUI

public struct CreateEditLoanView: View {
    @ObservedObject public var viewModel: LoanViewModel
    public var existingLoan: LoanItem?
    @Environment(\.presentationMode) private var presentationMode

    @State private var type: LoanType = .lent
    @State private var title: String = ""
    @State private var valueOrCategory: String = ""
    @State private var contactName: String = ""
    @State private var contactPhone: String = ""
    @State private var notes: String = ""
    @State private var dueDate: Date? = nil
    @State private var showDatePicker: Bool = false

    @State private var mediaList: [MediaItem] = []
    @State private var recordedAudioPath: String? = nil

    // Sheets & Pickers
    @State private var showContactPicker: Bool = false
    @State private var showMediaPicker: Bool = false
    @State private var mediaPickerSource: MediaPickerSource = .photoCamera

    @State private var validationError: String? = nil

    @ObservedObject private var audioRecorderPlayer = AudioRecorderPlayer.shared

    public init(viewModel: LoanViewModel, existingLoan: LoanItem? = nil) {
        self.viewModel = viewModel
        self.existingLoan = existingLoan
    }

    private var lang: AppLanguage { viewModel.currentLanguage }
    private var isEditing: Bool { existingLoan != nil }

    public var body: some View {
        NavigationView {
            ZStack {
                Color.appSurfaceLight
                    .ignoresSafeArea()

                ScrollView {
                    VStack(spacing: 20) {
                        // 1. Type Selection Cards
                        VStack(alignment: .leading, spacing: 8) {
                            Text(AppStrings.typeHeader(lang))
                                .font(.system(size: 13, weight: .bold))
                                .foregroundColor(Color.appTextSecondaryLight)

                            HStack(spacing: 12) {
                                TypeChoiceCard(
                                    title: AppStrings.typeLent(lang),
                                    subtitle: AppStrings.typeLentSub(lang),
                                    icon: "arrow.up.right",
                                    color: Color.statusGreen,
                                    isSelected: type == .lent,
                                    onClick: { type = .lent }
                                )

                                TypeChoiceCard(
                                    title: AppStrings.typeBorrowed(lang),
                                    subtitle: AppStrings.typeBorrowedSub(lang),
                                    icon: "arrow.down.left",
                                    color: Color.statusBlue,
                                    isSelected: type == .borrowed,
                                    onClick: { type = .borrowed }
                                )
                            }
                        }

                        // 2. Item & Value Inputs
                        VStack(spacing: 14) {
                            VStack(alignment: .leading, spacing: 6) {
                                Text(AppStrings.titleLabel(lang))
                                    .font(.system(size: 13, weight: .semibold))
                                    .foregroundColor(Color.primary)
                                TextField(AppStrings.titlePlaceholder(lang), text: $title)
                                    .padding(12)
                                    .background(Color.appCardBackgroundLight)
                                    .overlay(
                                        RoundedRectangle(cornerRadius: 14)
                                            .stroke(Color.appBorderLight, lineWidth: 1)
                                    )
                                    .cornerRadius(14)
                            }

                            VStack(alignment: .leading, spacing: 6) {
                                Text(AppStrings.valueLabel(lang))
                                    .font(.system(size: 13, weight: .semibold))
                                    .foregroundColor(Color.primary)
                                TextField("ex: 50 €, Clés, Perceuse...", text: $valueOrCategory)
                                    .padding(12)
                                    .background(Color.appCardBackgroundLight)
                                    .overlay(
                                        RoundedRectangle(cornerRadius: 14)
                                            .stroke(Color.appBorderLight, lineWidth: 1)
                                    )
                                    .cornerRadius(14)
                            }
                        }
                        .padding(16)
                        .background(Color.appCardBackgroundLight)
                        .overlay(
                            RoundedRectangle(cornerRadius: 18)
                                .stroke(Color.appCardBorderLight, lineWidth: 1)
                        )
                        .cornerRadius(18)

                        // 3. Contact Details
                        VStack(alignment: .leading, spacing: 14) {
                            HStack {
                                Text(AppStrings.personHeader(lang))
                                    .font(.system(size: 14, weight: .bold))
                                    .foregroundColor(Color.primary)
                                Spacer()
                                Button(action: { showContactPicker = true }) {
                                    HStack(spacing: 4) {
                                        Image(systemName: "person.crop.circle.badge.plus")
                                        Text(AppStrings.pickContactBtn(lang))
                                    }
                                    .font(.system(size: 12, weight: .bold))
                                    .foregroundColor(Color.primaryIndigo)
                                    .padding(.horizontal, 10)
                                    .padding(.vertical, 6)
                                    .background(Color.primaryIndigoContainerLight)
                                    .cornerRadius(12)
                                }
                            }

                            VStack(alignment: .leading, spacing: 6) {
                                Text(AppStrings.contactNameLabel(lang))
                                    .font(.system(size: 13, weight: .semibold))
                                    .foregroundColor(Color.primary)
                                TextField("Nom / Prénom", text: $contactName)
                                    .padding(12)
                                    .background(Color.appCardBackgroundLight)
                                    .overlay(
                                        RoundedRectangle(cornerRadius: 14)
                                            .stroke(Color.appBorderLight, lineWidth: 1)
                                    )
                                    .cornerRadius(14)
                            }

                            VStack(alignment: .leading, spacing: 6) {
                                Text(AppStrings.contactPhoneLabel(lang))
                                    .font(.system(size: 13, weight: .semibold))
                                    .foregroundColor(Color.primary)
                                TextField("+33 6 12 34 56 78", text: $contactPhone)
                                    .keyboardType(.phonePad)
                                    .padding(12)
                                    .background(Color.appCardBackgroundLight)
                                    .overlay(
                                        RoundedRectangle(cornerRadius: 14)
                                            .stroke(Color.appBorderLight, lineWidth: 1)
                                    )
                                    .cornerRadius(14)
                            }
                        }
                        .padding(16)
                        .background(Color.appCardBackgroundLight)
                        .overlay(
                            RoundedRectangle(cornerRadius: 18)
                                .stroke(Color.appCardBorderLight, lineWidth: 1)
                        )
                        .cornerRadius(18)

                        // 4. Attachments & Media
                        VStack(alignment: .leading, spacing: 12) {
                            Text(AppStrings.mediaHeader(lang))
                                .font(.system(size: 14, weight: .bold))
                                .foregroundColor(Color.primary)

                            HStack(spacing: 8) {
                                MediaActionButton(
                                    title: "Photo",
                                    icon: "camera.fill",
                                    color: Color.primaryIndigo,
                                    onClick: {
                                        mediaPickerSource = .photoCamera
                                        showMediaPicker = true
                                    }
                                )
                                MediaActionButton(
                                    title: "Vidéo",
                                    icon: "video.fill",
                                    color: Color.accentCyan,
                                    onClick: {
                                        mediaPickerSource = .videoCamera
                                        showMediaPicker = true
                                    }
                                )
                                MediaActionButton(
                                    title: "Galerie",
                                    icon: "photo.on.rectangle",
                                    color: Color.accentPurple,
                                    onClick: {
                                        mediaPickerSource = .photoLibrary
                                        showMediaPicker = true
                                    }
                                )
                            }

                            if !mediaList.isEmpty {
                                ScrollView(.horizontal, showsIndicators: false) {
                                    HStack(spacing: 10) {
                                        ForEach(mediaList) { item in
                                            ZStack(alignment: .topTrailing) {
                                                if item.type == .photo, let uiImage = UIImage(contentsOfFile: item.filePath) {
                                                    Image(uiImage: uiImage)
                                                        .resizable()
                                                        .scaledToFill()
                                                        .frame(width: 80, height: 80)
                                                        .clipShape(RoundedRectangle(cornerRadius: 12))
                                                } else {
                                                    RoundedRectangle(cornerRadius: 12)
                                                        .fill(Color.black.opacity(0.8))
                                                        .frame(width: 80, height: 80)
                                                        .overlay(
                                                            Image(systemName: "video.fill")
                                                                .foregroundColor(.white)
                                                        )
                                                }

                                                Button(action: {
                                                    mediaList.removeAll { $0.id == item.id }
                                                }) {
                                                    Image(systemName: "xmark.circle.fill")
                                                        .foregroundColor(.white)
                                                        .background(Circle().fill(Color.red))
                                                }
                                                .padding(4)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        .padding(16)
                        .background(Color.appCardBackgroundLight)
                        .overlay(
                            RoundedRectangle(cornerRadius: 18)
                                .stroke(Color.appCardBorderLight, lineWidth: 1)
                        )
                        .cornerRadius(18)

                        // 5. Voice Note Recorder Card
                        VStack(alignment: .leading, spacing: 12) {
                            Text(AppStrings.voiceNoteHeader(lang))
                                .font(.system(size: 14, weight: .bold))
                                .foregroundColor(Color.primary)

                            if audioRecorderPlayer.isRecording {
                                HStack {
                                    Circle()
                                        .fill(Color.red)
                                        .frame(width: 12, height: 12)
                                    Text("Enregistrement : \(audioRecorderPlayer.recordingDuration)s")
                                        .font(.system(size: 14, weight: .bold))
                                        .foregroundColor(Color.red)

                                    Spacer()

                                    Button(action: {
                                        if let path = audioRecorderPlayer.stopRecording() {
                                            recordedAudioPath = path
                                        }
                                    }) {
                                        Text(AppStrings.stopBtn(lang))
                                            .font(.system(size: 13, weight: .bold))
                                            .foregroundColor(.white)
                                            .padding(.horizontal, 14)
                                            .padding(.vertical, 8)
                                            .background(Color.red)
                                            .cornerRadius(12)
                                    }
                                }
                                .padding(12)
                                .background(Color.statusRedContainer)
                                .cornerRadius(14)
                            } else if let audioPath = recordedAudioPath {
                                HStack {
                                    AudioPlayerCardView(audioPath: audioPath)

                                    Button(action: {
                                        recordedAudioPath = nil
                                    }) {
                                        Image(systemName: "trash.fill")
                                            .foregroundColor(Color.red)
                                            .padding(10)
                                    }
                                }
                            } else {
                                Button(action: {
                                    _ = audioRecorderPlayer.startRecording()
                                }) {
                                    HStack {
                                        Image(systemName: "mic.fill")
                                            .foregroundColor(Color.primaryIndigo)
                                        Text(AppStrings.recordBtn(lang))
                                            .font(.system(size: 14, weight: .bold))
                                            .foregroundColor(Color.primaryIndigo)
                                    }
                                    .frame(maxWidth: .infinity)
                                    .padding(.vertical, 12)
                                    .background(Color.primaryIndigoContainerLight)
                                    .cornerRadius(14)
                                }
                            }
                        }
                        .padding(16)
                        .background(Color.appCardBackgroundLight)
                        .overlay(
                            RoundedRectangle(cornerRadius: 18)
                                .stroke(Color.appCardBorderLight, lineWidth: 1)
                        )
                        .cornerRadius(18)

                        // 6. Return Due Date Reminder
                        VStack(alignment: .leading, spacing: 12) {
                            HStack {
                                Text(AppStrings.reminderHeader(lang))
                                    .font(.system(size: 14, weight: .bold))
                                    .foregroundColor(Color.primary)
                                Spacer()
                                if dueDate != nil {
                                    Button(action: { dueDate = nil }) {
                                        Text("Effacer")
                                            .font(.system(size: 12, weight: .semibold))
                                            .foregroundColor(Color.red)
                                    }
                                }
                            }

                            if let due = dueDate {
                                HStack {
                                    Image(systemName: "calendar")
                                        .foregroundColor(Color.primaryIndigo)
                                    DatePicker("", selection: Binding(
                                        get: { due },
                                        set: { dueDate = $0 }
                                    ), displayedComponents: [.date])
                                    .labelsHidden()
                                }
                                .padding(10)
                                .background(Color.primaryIndigoContainerLight.opacity(0.4))
                                .cornerRadius(12)
                            } else {
                                Button(action: {
                                    dueDate = Calendar.current.date(byAdding: .day, value: 7, to: Date())
                                }) {
                                    HStack {
                                        Image(systemName: "calendar.badge.plus")
                                        Text(AppStrings.setDateBtn(lang))
                                    }
                                    .font(.system(size: 13, weight: .semibold))
                                    .foregroundColor(Color.primaryIndigo)
                                    .frame(maxWidth: .infinity)
                                    .padding(.vertical, 10)
                                    .background(Color.primaryIndigoContainerLight)
                                    .cornerRadius(12)
                                }
                            }
                        }
                        .padding(16)
                        .background(Color.appCardBackgroundLight)
                        .overlay(
                            RoundedRectangle(cornerRadius: 18)
                                .stroke(Color.appCardBorderLight, lineWidth: 1)
                        )
                        .cornerRadius(18)

                        // 7. Notes Field
                        VStack(alignment: .leading, spacing: 6) {
                            Text(AppStrings.notesLabel(lang))
                                .font(.system(size: 13, weight: .semibold))
                                .foregroundColor(Color.primary)
                            TextEditor(text: $notes)
                                .frame(height: 80)
                                .padding(8)
                                .background(Color.appCardBackgroundLight)
                                .overlay(
                                    RoundedRectangle(cornerRadius: 14)
                                        .stroke(Color.appBorderLight, lineWidth: 1)
                                )
                                .cornerRadius(14)
                        }
                        .padding(16)
                        .background(Color.appCardBackgroundLight)
                        .overlay(
                            RoundedRectangle(cornerRadius: 18)
                                .stroke(Color.appCardBorderLight, lineWidth: 1)
                        )
                        .cornerRadius(18)

                        if let error = validationError {
                            Text(error)
                                .font(.system(size: 13, weight: .semibold))
                                .foregroundColor(Color.red)
                        }

                        // 8. Save Button
                        Button(action: saveLoanAction) {
                            Text(AppStrings.saveBtn(lang))
                                .font(.system(size: 16, weight: .bold))
                                .foregroundColor(.white)
                                .frame(maxWidth: .infinity)
                                .padding(.vertical, 15)
                                .background(Color.primaryIndigo)
                                .cornerRadius(18)
                                .shadow(color: Color.primaryIndigo.opacity(0.4), radius: 8, x: 0, y: 4)
                        }
                        .padding(.top, 8)
                        .padding(.bottom, 30)
                    }
                    .padding(16)
                }
            }
            .navigationTitle(isEditing ? AppStrings.editTitle(lang) : AppStrings.createTitle(lang))
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarItems(
                leading: Button(AppStrings.cancelBtn(lang)) {
                    presentationMode.wrappedValue.dismiss()
                }
            )
            .sheet(isPresented: $showContactPicker) {
                ContactPickerView { name, phone in
                    contactName = name
                    if let phone = phone {
                        contactPhone = phone
                    }
                }
            }
            .sheet(isPresented: $showMediaPicker) {
                MediaPicker(source: mediaPickerSource) { mediaItem in
                    mediaList.append(mediaItem)
                }
            }
            .onAppear {
                if let loan = existingLoan {
                    type = loan.type
                    title = loan.title
                    valueOrCategory = loan.valueOrCategory ?? ""
                    contactName = loan.contactName
                    contactPhone = loan.contactPhone ?? ""
                    notes = loan.notes ?? ""
                    mediaList = loan.mediaList
                    recordedAudioPath = loan.audioPath
                    if let due = loan.dueDate {
                        dueDate = Date(timeIntervalSince1970: Double(due) / 1000.0)
                    }
                }
            }
        }
    }

    private func saveLoanAction() {
        let trimmedTitle = title.trimmingCharacters(in: .whitespacesAndNewlines)
        let trimmedContact = contactName.trimmingCharacters(in: .whitespacesAndNewlines)

        guard !trimmedTitle.isEmpty else {
            validationError = "Veuillez saisir l'objet ou motif du prêt."
            return
        }
        guard !trimmedContact.isEmpty else {
            validationError = "Veuillez saisir le nom du contact."
            return
        }

        let dueMillis = dueDate.map { Int64($0.timeIntervalSince1970 * 1000) }

        viewModel.saveLoan(
            id: existingLoan?.id ?? 0,
            title: trimmedTitle,
            type: type,
            contactName: trimmedContact,
            contactPhone: contactPhone.isEmpty ? nil : contactPhone,
            contactEmail: nil,
            valueOrCategory: valueOrCategory.isEmpty ? nil : valueOrCategory,
            photoPath: nil,
            audioPath: recordedAudioPath,
            audioDurationMs: 0,
            mediaList: mediaList,
            dueDate: dueMillis,
            notes: notes.isEmpty ? nil : notes,
            onSuccess: { _ in
                presentationMode.wrappedValue.dismiss()
            }
        )
    }
}

private struct TypeChoiceCard: View {
    let title: String
    let subtitle: String
    let icon: String
    let color: Color
    let isSelected: Bool
    let onClick: () -> Void

    var body: some View {
        Button(action: onClick) {
            HStack(spacing: 10) {
                ZStack {
                    Circle()
                        .fill(isSelected ? color : color.opacity(0.15))
                        .frame(width: 36, height: 36)
                    Image(systemName: icon)
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(isSelected ? .white : color)
                }

                VStack(alignment: .leading, spacing: 2) {
                    Text(title)
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(isSelected ? color : Color.primary)
                    Text(subtitle)
                        .font(.system(size: 11))
                        .foregroundColor(isSelected ? color.opacity(0.8) : Color.appTextSecondaryLight)
                }

                Spacer()
            }
            .padding(12)
            .frame(maxWidth: .infinity)
            .background(isSelected ? color.opacity(0.08) : Color.appCardBackgroundLight)
            .overlay(
                RoundedRectangle(cornerRadius: 16)
                    .stroke(isSelected ? color : Color.appBorderLight, lineWidth: isSelected ? 2 : 1)
            )
            .cornerRadius(16)
        }
        .buttonStyle(PlainButtonStyle())
    }
}

private struct MediaActionButton: View {
    let title: String
    let icon: String
    let color: Color
    let onClick: () -> Void

    var body: some View {
        Button(action: onClick) {
            VStack(spacing: 6) {
                Image(systemName: icon)
                    .font(.system(size: 16, weight: .semibold))
                    .foregroundColor(color)
                Text(title)
                    .font(.system(size: 11, weight: .semibold))
                    .foregroundColor(Color.primary)
            }
            .frame(maxWidth: .infinity)
            .padding(.vertical, 10)
            .background(Color.appCardBackgroundLight)
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(Color.appBorderLight, lineWidth: 1)
            )
            .cornerRadius(12)
        }
        .buttonStyle(PlainButtonStyle())
    }
}
