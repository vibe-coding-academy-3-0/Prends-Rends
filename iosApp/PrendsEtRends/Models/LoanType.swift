import Foundation

public enum LoanType: String, Codable, CaseIterable {
    case lent = "LENT"           // J'ai prêté (à quelqu'un)
    case borrowed = "BORROWED"   // J'ai emprunté (à quelqu'un)
}
