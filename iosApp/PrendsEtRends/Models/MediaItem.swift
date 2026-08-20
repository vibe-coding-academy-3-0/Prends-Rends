import Foundation

public enum MediaType: String, Codable, CaseIterable {
    case photo = "PHOTO"
    case video = "VIDEO"
    case audio = "AUDIO"
}

public struct MediaItem: Identifiable, Codable, Equatable {
    public var id: String
    public var filePath: String
    public var type: MediaType
    public var durationMs: Int64
    public var fileName: String?

    public init(
        id: String = UUID().uuidString,
        filePath: String,
        type: MediaType,
        durationMs: Int64 = 0,
        fileName: String? = nil
    ) {
        self.id = id
        self.filePath = filePath
        self.type = type
        self.durationMs = durationMs
        self.fileName = fileName
    }
}
