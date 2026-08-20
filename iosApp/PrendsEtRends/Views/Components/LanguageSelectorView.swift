import SwiftUI

public struct LanguageSelectorView: View {
    @Binding public var selectedLang: AppLanguage

    public init(selectedLang: Binding<AppLanguage>) {
        self._selectedLang = selectedLang
    }

    public var body: some View {
        Menu {
            ForEach(AppLanguage.allCases) { lang in
                Button(action: {
                    selectedLang = lang
                }) {
                    HStack {
                        Text("\(lang.flag) \(lang.displayName)")
                        if selectedLang == lang {
                            Image(systemName: "checkmark")
                        }
                    }
                }
            }
        } label: {
            HStack(spacing: 4) {
                Text(selectedLang.flag)
                    .font(.system(size: 14))
                Text(selectedLang.code.uppercased())
                    .font(.system(size: 12, weight: .bold))
                    .foregroundColor(Color.primary)
                Image(systemName: "chevron.down")
                    .font(.system(size: 9, weight: .bold))
                    .foregroundColor(Color.appTextSecondaryLight)
            }
            .padding(.horizontal, 8)
            .padding(.vertical, 6)
            .background(Color.appCardBackgroundLight.opacity(0.8))
            .overlay(
                RoundedRectangle(cornerRadius: 16)
                    .stroke(Color.appBorderLight.opacity(0.5), lineWidth: 1)
            )
            .cornerRadius(16)
        }
    }
}

public struct ThemeSelectorView: View {
    @Binding public var currentTheme: AppThemeMode

    public init(currentTheme: Binding<AppThemeMode>) {
        self._currentTheme = currentTheme
    }

    public var body: some View {
        Button(action: {
            switch currentTheme {
            case .light: currentTheme = .dark
            case .dark: currentTheme = .light
            case .system: currentTheme = .dark
            }
        }) {
            Image(systemName: currentTheme == .dark ? "moon.fill" : "sun.max.fill")
                .font(.system(size: 14, weight: .semibold))
                .foregroundColor(currentTheme == .dark ? Color.statusOrange : Color.primaryIndigo)
                .padding(8)
                .background(Color.appCardBackgroundLight.opacity(0.8))
                .overlay(
                    Circle()
                        .stroke(Color.appBorderLight.opacity(0.5), lineWidth: 1)
                )
                .clipShape(Circle())
        }
    }
}
