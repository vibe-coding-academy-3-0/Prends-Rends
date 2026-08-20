import Foundation

public struct AppStrings {
    public static func appTitle(_ lang: AppLanguage) -> String { "Prends & Rends" }

    public static func appSubtitle(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Suivi des prêts & emprunts"
        case .en: return "Loan & borrow tracker"
        case .ha: return "Maikula da bashi da aro"
        }
    }

    // Tabs
    public static func tabAll(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Tous"
        case .en: return "All"
        case .ha: return "Dukkansu"
        }
    }
    public static func tabLent(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Prêtés"
        case .en: return "Lent"
        case .ha: return "Aka bayar"
        }
    }
    public static func tabBorrowed(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Empruntés"
        case .en: return "Borrowed"
        case .ha: return "Aka aro"
        }
    }

    // Status filters
    public static func filterAll(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Tous les statuts"
        case .en: return "All statuses"
        case .ha: return "Dukan matsayi"
        }
    }
    public static func filterActive(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "En cours"
        case .en: return "Active"
        case .ha: return "Yana gudana"
        }
    }
    public static func filterOverdue(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "En retard"
        case .en: return "Overdue"
        case .ha: return "Ya wuce lokaci"
        }
    }
    public static func filterReturned(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Rendu"
        case .en: return "Returned"
        case .ha: return "An mayar"
        }
    }

    // Dashboard
    public static func newLoanBtn(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Nouveau prêt"
        case .en: return "New loan"
        case .ha: return "Sabon bashi"
        }
    }
    public static func searchPlaceholder(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Rechercher un objet, contact..."
        case .en: return "Search item, contact..."
        case .ha: return "Nemi abu, lamba..."
        }
    }
    public static func statsLent(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Prêtés"
        case .en: return "Lent"
        case .ha: return "Aka bayar"
        }
    }
    public static func statsBorrowed(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Empruntés"
        case .en: return "Borrowed"
        case .ha: return "Aka aro"
        }
    }
    public static func statsOverdue(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "En retard"
        case .en: return "Overdue"
        case .ha: return "Aka makara"
        }
    }
    public static func statsReturned(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Rendus"
        case .en: return "Returned"
        case .ha: return "Aka mayar"
        }
    }
    public static func emptyTitle(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Aucun prêt enregistré"
        case .en: return "No loans recorded"
        case .ha: return "Babu bashi a ajiye"
        }
    }
    public static func emptySubtitle(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Gardez une trace fluide de ce que vous prêtez ou empruntez."
        case .en: return "Keep smooth track of items and money lent or borrowed."
        case .ha: return "Kula da abin da kake bayarwa ko aro cikin sauki."
        }
    }

    // Form
    public static func createTitle(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Nouveau prêt"
        case .en: return "New Loan"
        case .ha: return "Sabon Bashi"
        }
    }
    public static func editTitle(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Modifier le prêt"
        case .en: return "Edit Loan"
        case .ha: return "Gyara Bashi"
        }
    }
    public static func typeHeader(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Type de prêt"
        case .en: return "Loan type"
        case .ha: return "Nau'in bashi"
        }
    }
    public static func typeLent(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "J'ai prêté"
        case .en: return "I lent"
        case .ha: return "Na bayar"
        }
    }
    public static func typeBorrowed(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "J'ai emprunté"
        case .en: return "I borrowed"
        case .ha: return "Na aro"
        }
    }
    public static func typeLentSub(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "À un proche"
        case .en: return "To a contact"
        case .ha: return "Ga wani"
        }
    }
    public static func typeBorrowedSub(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "D'un proche"
        case .en: return "From a contact"
        case .ha: return "Daga wani"
        }
    }
    public static func titleLabel(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Objet ou Motif du prêt *"
        case .en: return "Item or Reason *"
        case .ha: return "Abu ko Dalilin Bashi *"
        }
    }
    public static func titlePlaceholder(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "ex: Perceuse Bosch, 50 €, Livre..."
        case .en: return "e.g., Drill, $50, Book..."
        case .ha: return "mis: Na'ura, $50, Littafi..."
        }
    }
    public static func valueLabel(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Valeur ou Montant (Optionnel)"
        case .en: return "Value or Amount (Optional)"
        case .ha: return "Darajar ko Adadi (Zabi)"
        }
    }
    public static func personHeader(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Personne concernée"
        case .en: return "Person involved"
        case .ha: return "Mutumin da abin ya shafa"
        }
    }
    public static func contactNameLabel(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Nom du contact *"
        case .en: return "Contact name *"
        case .ha: return "Sunan mutum *"
        }
    }
    public static func contactPhoneLabel(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Numéro de téléphone (Optionnel)"
        case .en: return "Phone number (Optional)"
        case .ha: return "Lambar waya (Zabi)"
        }
    }
    public static func pickContactBtn(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Contact"
        case .en: return "Contact"
        case .ha: return "Lambar Waya"
        }
    }
    public static func mediaHeader(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Justificatifs & Médias joints"
        case .en: return "Attachments & Media"
        case .ha: return "Hotuna & Fayilolin Bashi"
        }
    }
    public static func takePhotoBtn(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Photo (Caméra)"
        case .en: return "Photo (Camera)"
        case .ha: return "Hoto (Kamera)"
        }
    }
    public static func recordVideoBtn(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Vidéo (Caméra)"
        case .en: return "Video (Camera)"
        case .ha: return "Bidiyo (Kamera)"
        }
    }
    public static func galleryBtn(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Galerie Médias"
        case .en: return "Media Gallery"
        case .ha: return "Manhajar Hotuna"
        }
    }
    public static func voiceNoteHeader(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Note vocale"
        case .en: return "Voice note"
        case .ha: return "Muryar Bayani"
        }
    }
    public static func recordBtn(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Enregistrer"
        case .en: return "Record"
        case .ha: return "Rikodi"
        }
    }
    public static func stopBtn(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Arrêter"
        case .en: return "Stop"
        case .ha: return "Tsaya"
        }
    }
    public static func reminderHeader(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Rappel de date de retour"
        case .en: return "Return Due Date Reminder"
        case .ha: return "Ranar Tunatarwa ta Mayarwa"
        }
    }
    public static func setDateBtn(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Définir date"
        case .en: return "Set date"
        case .ha: return "Saka rana"
        }
    }
    public static func changeDateBtn(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Changer"
        case .en: return "Change"
        case .ha: return "Canza"
        }
    }
    public static func notesLabel(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Notes & détails complémentaires"
        case .en: return "Notes & additional details"
        case .ha: return "Bayanai na daban"
        }
    }
    public static func saveBtn(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Enregistrer le prêt"
        case .en: return "Save loan"
        case .ha: return "Ajiye bashi"
        }
    }

    // Detail Screen
    public static func detailTitle(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Fiche du prêt"
        case .en: return "Loan Details"
        case .ha: return "Bayanin Bashi"
        }
    }
    public static func lentTo(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Prêté à"
        case .en: return "Lent to"
        case .ha: return "Aka bayar ga"
        }
    }
    public static func borrowedFrom(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Emprunté à"
        case .en: return "Borrowed from"
        case .ha: return "Aka aro daga"
        }
    }
    public static func callBtn(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Appeler"
        case .en: return "Call"
        case .ha: return "Kira"
        }
    }
    public static func smsAction(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "SMS"
        case .en: return "SMS"
        case .ha: return "Tura Saƙo"
        }
    }
    public static func whatsappAction(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "WhatsApp"
        case .en: return "WhatsApp"
        case .ha: return "WhatsApp"
        }
    }
    public static func markReturnedBtn(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Marquer comme RENDU"
        case .en: return "Mark as RETURNED"
        case .ha: return "Shaida cewa AN MAYAR"
        }
    }
    public static func markActiveBtn(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Marquer comme NON rendu"
        case .en: return "Mark as NOT returned"
        case .ha: return "Shaida cewa BAI MAYAR BA"
        }
    }
    public static func deleteConfirmTitle(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Supprimer ce prêt ?"
        case .en: return "Delete this loan?"
        case .ha: return "Goge wannan bashi?"
        }
    }
    public static func deleteConfirmDesc(_ lang: AppLanguage, title: String) -> String {
        switch lang {
        case .fr: return "Êtes-vous sûr de vouloir supprimer la fiche de \"\(title)\" ? Action irréversible."
        case .en: return "Are you sure you want to delete \"\(title)\"? Action cannot be undone."
        case .ha: return "Shin ka tabbata kana son goge \"\(title)\"? Ba za a iya soke shi ba."
        }
    }
    public static func cancelBtn(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Annuler"
        case .en: return "Cancel"
        case .ha: return "Fasa"
        }
    }
    public static func deleteBtn(_ lang: AppLanguage) -> String {
        switch lang {
        case .fr: return "Supprimer"
        case .en: return "Delete"
        case .ha: return "Goge"
        }
    }
}
