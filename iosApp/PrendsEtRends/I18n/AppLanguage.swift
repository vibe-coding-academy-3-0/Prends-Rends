import Foundation

public enum AppLanguage: String, CaseIterable, Identifiable {
    case fr = "fr"
    case en = "en"
    case ha = "ha"

    public var id: String { rawValue }

    public var code: String { rawValue }

    public var displayName: String {
        switch self {
        case .fr: return "Français"
        case .en: return "English"
        case .ha: return "Hausa"
        }
    }

    public var flag: String {
        switch self {
        case .fr: return "🇫🇷"
        case .en: return "🇬🇧"
        case .ha: return "🇳🇬"
        }
    }
}
