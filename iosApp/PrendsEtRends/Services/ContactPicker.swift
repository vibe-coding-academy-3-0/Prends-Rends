import SwiftUI
import ContactsUI

public struct ContactPickerView: UIViewControllerRepresentable {
    public var onSelect: (String, String?) -> Void

    public init(onSelect: @escaping (String, String?) -> Void) {
        self.onSelect = onSelect
    }

    public func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    public func makeUIViewController(context: Context) -> CNContactPickerViewController {
        let picker = CNContactPickerViewController()
        picker.delegate = context.coordinator
        return picker
    }

    public func updateUIViewController(_ uiViewController: CNContactPickerViewController, context: Context) {}

    public class Coordinator: NSObject, CNContactPickerDelegate {
        var parent: ContactPickerView

        init(_ parent: ContactPickerView) {
            self.parent = parent
        }

        public func contactPicker(_ picker: CNContactPickerViewController, didSelect contact: CNContact) {
            let fullName = "\(contact.givenName) \(contact.familyName)".trimmingCharacters(in: .whitespacesAndNewlines)
            let name = fullName.isEmpty ? "Contact" : fullName

            var phoneNumber: String? = nil
            if let firstPhone = contact.phoneNumbers.first?.value.stringValue {
                phoneNumber = firstPhone
            }

            parent.onSelect(name, phoneNumber)
        }
    }
}
