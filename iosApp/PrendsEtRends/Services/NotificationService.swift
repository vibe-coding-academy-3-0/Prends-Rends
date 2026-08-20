import Foundation
import UserNotifications

public final class NotificationService {
    public static let shared = NotificationService()

    private init() {}

    public func requestAuthorization() {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { granted, error in
            if let error = error {
                print("NotificationService: Error requesting authorization: \(error)")
            }
        }
    }

    public func scheduleReminder(
        loanId: Int64,
        title: String,
        contactName: String,
        isLent: Bool,
        triggerAtMillis: Int64
    ) {
        let triggerDate = Date(timeIntervalSince1970: Double(triggerAtMillis) / 1000.0)
        guard triggerDate > Date() else { return }

        let content = UNMutableNotificationContent()
        content.title = isLent ? "Rappel : Objet/argent prêté !" : "Rappel : Objet/argent emprunté !"
        content.body = isLent
            ? "\"\(title)\" prêté à \(contactName) arrive à échéance aujourd'hui !"
            : "N'oubliez pas de rendre \"\(title)\" à \(contactName) aujourd'hui !"
        content.sound = .default

        let timeInterval = triggerDate.timeIntervalSinceNow
        guard timeInterval > 0 else { return }

        let trigger = UNTimeIntervalNotificationTrigger(timeInterval: timeInterval, repeats: false)
        let request = UNNotificationRequest(
            identifier: "loan_reminder_\(loanId)",
            content: content,
            trigger: trigger
        )

        UNUserNotificationCenter.current().add(request) { error in
            if let error = error {
                print("NotificationService: Error scheduling notification: \(error)")
            }
        }
    }

    public func cancelReminder(loanId: Int64) {
        let identifier = "loan_reminder_\(loanId)"
        UNUserNotificationCenter.current().removePendingNotificationRequests(withIdentifiers: [identifier])
        UNUserNotificationCenter.current().removeDeliveredNotifications(withIdentifiers: [identifier])
    }
}
