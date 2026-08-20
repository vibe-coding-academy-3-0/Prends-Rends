import Foundation

public final class StorageService {
    public static let shared = StorageService()

    private let fileManager = FileManager.default
    private let fileName = "prends_et_rends_loans.json"

    private var documentsURL: URL {
        fileManager.urls(for: .documentDirectory, in: .userDomainMask)[0]
    }

    private var storageFileURL: URL {
        documentsURL.appendingPathComponent(fileName)
    }

    private init() {}

    public func loadLoans() -> [LoanItem] {
        guard fileManager.fileExists(atPath: storageFileURL.path) else {
            return []
        }
        do {
            let data = try Data(contentsOf: storageFileURL)
            let loans = try JSONDecoder().decode([LoanItem].self, from: data)
            return loans
        } catch {
            print("StorageService: Error loading loans: \(error)")
            return []
        }
    }

    public func saveLoans(_ loans: [LoanItem]) {
        do {
            let data = try JSONEncoder().encode(loans)
            try data.write(to: storageFileURL, options: .atomic)
        } catch {
            print("StorageService: Error saving loans: \(error)")
        }
    }

    public func deleteMediaFile(path: String?) {
        guard let path = path, !path.isEmpty else { return }
        let url = URL(fileURLWithPath: path)
        try? fileManager.removeItem(at: url)
    }

    public func getMediaDirectoryURL() -> URL {
        let mediaDir = documentsURL.appendingPathComponent("Media", isDirectory: true)
        if !fileManager.fileExists(atPath: mediaDir.path) {
            try? fileManager.createDirectory(at: mediaDir, withIntermediateDirectories: true)
        }
        return mediaDir
    }
}
