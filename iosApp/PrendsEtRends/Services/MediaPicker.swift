import SwiftUI
import UIKit
import UniformTypeIdentifiers

public enum MediaPickerSource {
    case photoCamera
    case videoCamera
    case photoLibrary
}

public struct MediaPicker: UIViewControllerRepresentable {
    public var source: MediaPickerSource
    public var onSelect: (MediaItem) -> Void
    @Environment(\.presentationMode) private var presentationMode

    public init(source: MediaPickerSource, onSelect: @escaping (MediaItem) -> Void) {
        self.source = source
        self.onSelect = onSelect
    }

    public func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    public func makeUIViewController(context: Context) -> UIImagePickerController {
        let picker = UIImagePickerController()
        picker.delegate = context.coordinator

        switch source {
        case .photoCamera:
            if UIImagePickerController.isSourceTypeAvailable(.camera) {
                picker.sourceType = .camera
                picker.cameraCaptureMode = .photo
            } else {
                picker.sourceType = .photoLibrary
            }
        case .videoCamera:
            if UIImagePickerController.isSourceTypeAvailable(.camera) {
                picker.sourceType = .camera
                picker.mediaTypes = [UTType.movie.identifier]
                picker.cameraCaptureMode = .video
            } else {
                picker.sourceType = .photoLibrary
            }
        case .photoLibrary:
            picker.sourceType = .photoLibrary
            picker.mediaTypes = [UTType.image.identifier, UTType.movie.identifier]
        }

        return picker
    }

    public func updateUIViewController(_ uiViewController: UIImagePickerController, context: Context) {}

    public class Coordinator: NSObject, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
        var parent: MediaPicker

        init(_ parent: MediaPicker) {
            self.parent = parent
        }

        public func imagePickerController(
            _ picker: UIImagePickerController,
            didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]
        ) {
            let mediaDir = StorageService.shared.getMediaDirectoryURL()
            let timestamp = Int64(Date().timeIntervalSince1970 * 1000)

            if let image = info[.originalImage] as? UIImage {
                let fileName = "photo_\(timestamp).jpg"
                let fileURL = mediaDir.appendingPathComponent(fileName)
                if let data = image.jpegData(compressionQuality: 0.8) {
                    try? data.write(to: fileURL)
                    let mediaItem = MediaItem(
                        filePath: fileURL.path,
                        type: .photo,
                        fileName: fileName
                    )
                    parent.onSelect(mediaItem)
                }
            } else if let videoURL = info[.mediaURL] as? URL {
                let fileName = "video_\(timestamp).mp4"
                let fileURL = mediaDir.appendingPathComponent(fileName)
                try? FileManager.default.copyItem(at: videoURL, to: fileURL)
                let mediaItem = MediaItem(
                    filePath: fileURL.path,
                    type: .video,
                    fileName: fileName
                )
                parent.onSelect(mediaItem)
            }

            parent.presentationMode.wrappedValue.dismiss()
        }

        public func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
            parent.presentationMode.wrappedValue.dismiss()
        }
    }
}
