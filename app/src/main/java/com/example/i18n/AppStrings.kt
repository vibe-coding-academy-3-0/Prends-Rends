package com.example.i18n

object AppStrings {
    fun appTitle(lang: AppLanguage) = "Prends & Rends"
    fun appSubtitle(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Suivi des prêts & emprunts"
        AppLanguage.EN -> "Loan & borrow tracker"
        AppLanguage.HA -> "Maikula da bashi da aro"
    }

    // Tabs
    fun tabAll(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Tous"
        AppLanguage.EN -> "All"
        AppLanguage.HA -> "Dukkansu"
    }
    fun tabLent(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Prêtés"
        AppLanguage.EN -> "Lent"
        AppLanguage.HA -> "Aka bayar"
    }
    fun tabBorrowed(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Empruntés"
        AppLanguage.EN -> "Borrowed"
        AppLanguage.HA -> "Aka aro"
    }

    // Status filters
    fun filterAll(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Tous les statuts"
        AppLanguage.EN -> "All statuses"
        AppLanguage.HA -> "Dukan matsayi"
    }
    fun filterActive(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "En cours"
        AppLanguage.EN -> "Active"
        AppLanguage.HA -> "Yana gudana"
    }
    fun filterOverdue(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "En retard"
        AppLanguage.EN -> "Overdue"
        AppLanguage.HA -> "Ya wuce lokaci"
    }
    fun filterReturned(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Rendu"
        AppLanguage.EN -> "Returned"
        AppLanguage.HA -> "An mayar"
    }

    // Dashboard
    fun newLoanBtn(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Nouveau prêt"
        AppLanguage.EN -> "New loan"
        AppLanguage.HA -> "Sabon bashi"
    }
    fun searchPlaceholder(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Rechercher un objet, contact..."
        AppLanguage.EN -> "Search item, contact..."
        AppLanguage.HA -> "Nemi abu, lamba..."
    }
    fun statsLent(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Prêtés"
        AppLanguage.EN -> "Lent"
        AppLanguage.HA -> "Aka bayar"
    }
    fun statsBorrowed(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Empruntés"
        AppLanguage.EN -> "Borrowed"
        AppLanguage.HA -> "Aka aro"
    }
    fun statsOverdue(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "En retard"
        AppLanguage.EN -> "Overdue"
        AppLanguage.HA -> "Aka makara"
    }
    fun statsReturned(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Rendus"
        AppLanguage.EN -> "Returned"
        AppLanguage.HA -> "Aka mayar"
    }
    fun emptyTitle(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Aucun prêt enregistré"
        AppLanguage.EN -> "No loans recorded"
        AppLanguage.HA -> "Babu bashi a ajiye"
    }
    fun emptySubtitle(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Gardez une trace fluide de ce que vous prêtez ou empruntez."
        AppLanguage.EN -> "Keep smooth track of items and money lent or borrowed."
        AppLanguage.HA -> "Kula da abin da kake bayarwa ko aro cikin sauki."
    }

    // Form
    fun createTitle(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Nouveau prêt"
        AppLanguage.EN -> "New Loan"
        AppLanguage.HA -> "Sabon Bashi"
    }
    fun editTitle(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Modifier le prêt"
        AppLanguage.EN -> "Edit Loan"
        AppLanguage.HA -> "Gyara Bashi"
    }
    fun typeHeader(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Type de prêt"
        AppLanguage.EN -> "Loan type"
        AppLanguage.HA -> "Nau'in bashi"
    }
    fun typeLent(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "J'ai prêté"
        AppLanguage.EN -> "I lent"
        AppLanguage.HA -> "Na bayar"
    }
    fun typeBorrowed(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "J'ai emprunté"
        AppLanguage.EN -> "I borrowed"
        AppLanguage.HA -> "Na aro"
    }
    fun typeLentSub(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "À un proche"
        AppLanguage.EN -> "To a contact"
        AppLanguage.HA -> "Ga wani"
    }
    fun typeBorrowedSub(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "D'un proche"
        AppLanguage.EN -> "From a contact"
        AppLanguage.HA -> "Daga wani"
    }
    fun titleLabel(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Objet ou Motif du prêt *"
        AppLanguage.EN -> "Item or Reason *"
        AppLanguage.HA -> "Abu ko Dalilin Bashi *"
    }
    fun titlePlaceholder(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "ex: Perceuse Bosch, 50 €, Livre..."
        AppLanguage.EN -> "e.g., Drill, $50, Book..."
        AppLanguage.HA -> "mis: Na'ura, $50, Littafi..."
    }
    fun valueLabel(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Valeur ou Montant (Optionnel)"
        AppLanguage.EN -> "Value or Amount (Optional)"
        AppLanguage.HA -> "Darajar ko Adadi (Zabi)"
    }
    fun personHeader(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Personne concernée"
        AppLanguage.EN -> "Person involved"
        AppLanguage.HA -> "Mutumin da abin ya shafa"
    }
    fun contactNameLabel(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Nom du contact *"
        AppLanguage.EN -> "Contact name *"
        AppLanguage.HA -> "Sunan mutum *"
    }
    fun contactPhoneLabel(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Numéro de téléphone (Optionnel)"
        AppLanguage.EN -> "Phone number (Optional)"
        AppLanguage.HA -> "Lambar waya (Zabi)"
    }
    fun pickContactBtn(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Contact"
        AppLanguage.EN -> "Contact"
        AppLanguage.HA -> "Lambar Waya"
    }
    fun mediaHeader(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Justificatifs & Médias joints"
        AppLanguage.EN -> "Attachments & Media"
        AppLanguage.HA -> "Hotuna & Fayilolin Bashi"
    }
    fun takePhotoBtn(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Photo (Caméra)"
        AppLanguage.EN -> "Photo (Camera)"
        AppLanguage.HA -> "Hoto (Kamera)"
    }
    fun recordVideoBtn(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Vidéo (Caméra)"
        AppLanguage.EN -> "Video (Camera)"
        AppLanguage.HA -> "Bidiyo (Kamera)"
    }
    fun galleryBtn(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Galerie Médias"
        AppLanguage.EN -> "Media Gallery"
        AppLanguage.HA -> "Manhajar Hotuna"
    }
    fun voiceNoteHeader(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Note vocale"
        AppLanguage.EN -> "Voice note"
        AppLanguage.HA -> "Muryar Bayani"
    }
    fun recordBtn(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Enregistrer"
        AppLanguage.EN -> "Record"
        AppLanguage.HA -> "Rikodi"
    }
    fun stopBtn(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Arrêter"
        AppLanguage.EN -> "Stop"
        AppLanguage.HA -> "Tsaya"
    }
    fun reminderHeader(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Rappel de date de retour"
        AppLanguage.EN -> "Return Due Date Reminder"
        AppLanguage.HA -> "Ranar Tunatarwa ta Mayarwa"
    }
    fun setDateBtn(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Définir date"
        AppLanguage.EN -> "Set date"
        AppLanguage.HA -> "Saka rana"
    }
    fun changeDateBtn(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Changer"
        AppLanguage.EN -> "Change"
        AppLanguage.HA -> "Canza"
    }
    fun notesLabel(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Notes & détails complémentaires"
        AppLanguage.EN -> "Notes & additional details"
        AppLanguage.HA -> "Bayanai na daban"
    }
    fun saveBtn(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Enregistrer le prêt"
        AppLanguage.EN -> "Save loan"
        AppLanguage.HA -> "Ajiye bashi"
    }

    // Detail Screen
    fun detailTitle(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Fiche du prêt"
        AppLanguage.EN -> "Loan Details"
        AppLanguage.HA -> "Bayanin Bashi"
    }
    fun lentTo(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Prêté à"
        AppLanguage.EN -> "Lent to"
        AppLanguage.HA -> "Aka bayar ga"
    }
    fun borrowedFrom(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Emprunté à"
        AppLanguage.EN -> "Borrowed from"
        AppLanguage.HA -> "Aka aro daga"
    }
    fun callBtn(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Appeler"
        AppLanguage.EN -> "Call"
        AppLanguage.HA -> "Kira"
    }
    fun smsAction(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "SMS"
        AppLanguage.EN -> "SMS"
        AppLanguage.HA -> "Tura Saƙo"
    }
    fun whatsappAction(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "WhatsApp"
        AppLanguage.EN -> "WhatsApp"
        AppLanguage.HA -> "WhatsApp"
    }
    fun markReturnedBtn(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Marquer comme RENDU"
        AppLanguage.EN -> "Mark as RETURNED"
        AppLanguage.HA -> "Shaida cewa AN MAYAR"
    }
    fun markActiveBtn(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Marquer comme NON rendu"
        AppLanguage.EN -> "Mark as NOT returned"
        AppLanguage.HA -> "Shaida cewa BAI MAYAR BA"
    }
    fun deleteConfirmTitle(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Supprimer ce prêt ?"
        AppLanguage.EN -> "Delete this loan?"
        AppLanguage.HA -> "Goge wannan bashi?"
    }
    fun deleteConfirmDesc(lang: AppLanguage, title: String) = when(lang) {
        AppLanguage.FR -> "Êtes-vous sûr de vouloir supprimer la fiche de \"$title\" ? Action irréversible."
        AppLanguage.EN -> "Are you sure you want to delete \"$title\"? Action cannot be undone."
        AppLanguage.HA -> "Shin ka tabbata kana son goge \"$title\"? Ba za a iya soke shi ba."
    }
    fun cancelBtn(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Annuler"
        AppLanguage.EN -> "Cancel"
        AppLanguage.HA -> "Fasa"
    }
    fun deleteBtn(lang: AppLanguage) = when(lang) {
        AppLanguage.FR -> "Supprimer"
        AppLanguage.EN -> "Delete"
        AppLanguage.HA -> "Goge"
    }
}
