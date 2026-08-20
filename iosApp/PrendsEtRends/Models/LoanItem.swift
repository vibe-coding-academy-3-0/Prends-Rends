import Foundation

public struct LoanItem: Identifiable, Codable, Equatable {
    public var id: Int64
    public var title: String
    public var type: LoanType
    public var contactName: String
    public var contactPhone: String?
    public var contactEmail: String?
    public var valueOrCategory: String?
    public var photoPath: String?
    public var audioPath: String?
    public var audioDurationMs: Int64
    public var mediaList: [MediaItem]
    public var dueDate: Int64? // Epoch in milliseconds
    public var createdDate: Int64
    public var isReturned: BooleanLiteralType
    public var returnedDate: Int64?
    public var notes: String?

    public init(
        id: Int64 = Int64(Date().timeIntervalSince1970 * 1000) + Int64.random(in: 0...999),
        title: String,
        type: LoanType,
        contactName: String,
        contactPhone: String? = nil,
        contactEmail: String? = nil,
        valueOrCategory: String? = nil,
        photoPath: String? = nil,
        audioPath: String? = nil,
        audioDurationMs: Int64 = 0,
        mediaList: [MediaItem] = [],
        dueDate: Int64? = nil,
        createdDate: Int64 = Int64(Date().timeIntervalSince1970 * 1000),
        isReturned: Bool = false,
        returnedDate: Int64? = nil,
        notes: String? = nil
    ) {
        self.id = id
        self.title = title
        self.type = type
        self.contactName = contactName
        self.contactPhone = contactPhone
        self.contactEmail = contactEmail
        self.valueOrCategory = valueOrCategory
        self.photoPath = photoPath
        self.audioPath = audioPath
        self.audioDurationMs = audioDurationMs
        self.mediaList = mediaList
        self.dueDate = dueDate
        self.createdDate = createdDate
        self.isReturned = isReturned
        self.returnedDate = returnedDate
        self.notes = notes
    }

    public var isOverdue: Bool {
        guard !isReturned, let due = dueDate else { return false }
        return due < Int64(Date().timeIntervalSince1970 * 1000)
    }
}
