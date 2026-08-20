import SwiftUI

public extension Color {
    init(hex: UInt, alpha: Double = 1.0) {
        self.init(
            .sRGB,
            red: Double((hex >> 16) & 0xff) / 255,
            green: Double((hex >> 08) & 0xff) / 255,
            blue: Double((hex >> 00) & 0xff) / 255,
            opacity: alpha
        )
    }

    // Brand Primary Palette
    static let primaryIndigo = Color(hex: 0x4F46E5)
    static let primaryIndigoHover = Color(hex: 0x4338CA)
    static let primaryIndigoContainerLight = Color(hex: 0xEEF2FF)
    static let onPrimaryIndigoContainerLight = Color(hex: 0x312E81)

    static let accentCyan = Color(hex: 0x0EA5E9)
    static let accentPurple = Color(hex: 0x8B5CF6)

    // Light Theme Surfaces
    static let appSurfaceLight = Color(hex: 0xF4F6FC)
    static let appCardBackgroundLight = Color(hex: 0xFFFFFF)
    static let appTextPrimaryLight = Color(hex: 0x0F172A)
    static let appTextSecondaryLight = Color(hex: 0x64748B)
    static let appCardBorderLight = Color(hex: 0xCBD5E1)
    static let appBorderLight = Color(hex: 0x94A3B8)

    // Dark Theme Surfaces
    static let appSurfaceDark = Color(hex: 0x0B0F17)
    static let appCardBackgroundDark = Color(hex: 0x151D2A)
    static let appTextPrimaryDark = Color(hex: 0xF8FAFC)
    static let appTextSecondaryDark = Color(hex: 0x94A3B8)
    static let appCardBorderDark = Color(hex: 0x2E3A4E)

    // Status Colors - Light
    static let statusGreen = Color(hex: 0x059669)
    static let statusGreenContainer = Color(hex: 0xECFDF5)
    static let statusGreenBorder = Color(hex: 0xA7F3D0)

    static let statusBlue = Color(hex: 0x0284C7)
    static let statusBlueContainer = Color(hex: 0xF0F9FF)
    static let statusBlueBorder = Color(hex: 0xBAE6FD)

    static let statusOrange = Color(hex: 0xD97706)
    static let statusOrangeContainer = Color(hex: 0xFFFBEB)
    static let statusOrangeBorder = Color(hex: 0xFDE68A)

    static let statusRed = Color(hex: 0xE11D48)
    static let statusRedContainer = Color(hex: 0xFFF1F2)
    static let statusRedBorder = Color(hex: 0xFECDD3)
}

public enum AppThemeMode: String, CaseIterable, Identifiable {
    case light = "LIGHT"
    case dark = "DARK"
    case system = "SYSTEM"

    public var id: String { rawValue }

    public var displayName: String {
        switch self {
        case .light: return "Clair"
        case .dark: return "Sombre"
        case .system: return "Système"
        }
    }
}
