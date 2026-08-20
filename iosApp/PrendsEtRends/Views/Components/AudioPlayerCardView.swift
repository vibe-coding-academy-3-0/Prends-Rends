import SwiftUI
import AVKit

public struct AudioPlayerCardView: View {
    public let audioPath: String
    public let durationMs: Int64
    @ObservedObject private var player = AudioRecorderPlayer.shared

    public init(audioPath: String, durationMs: Int64 = 0) {
        self.audioPath = audioPath
        self.durationMs = durationMs
    }

    private var isCurrentPlaying: Bool {
        player.isPlaying && player.currentPlayingPath == audioPath
    }

    public var body: some View {
        HStack(spacing: 12) {
            Button(action: {
                player.playAudio(from: audioPath)
            }) {
                ZStack {
                    Circle()
                        .fill(Color.primaryIndigo)
                        .frame(width: 42, height: 42)
                    Image(systemName: isCurrentPlaying ? "pause.fill" : "play.fill")
                        .font(.system(size: 16, weight: .bold))
                        .foregroundColor(.white)
                }
            }

            VStack(alignment: .leading, spacing: 4) {
                HStack {
                    Text("Note vocale")
                        .font(.system(size: 13, weight: .semibold))
                        .foregroundColor(Color.primary)
                    Spacer()
                    Text(isCurrentPlaying ? "Lecture en cours" : "Prête à l'écoute")
                        .font(.system(size: 11))
                        .foregroundColor(Color.appTextSecondaryLight)
                }

                GeometryReader { geo in
                    ZStack(alignment: .leading) {
                        Capsule()
                            .fill(Color.appCardBorderLight.opacity(0.6))
                            .frame(height: 6)
                        Capsule()
                            .fill(Color.primaryIndigo)
                            .frame(width: isCurrentPlaying ? geo.size.width * CGFloat(player.playbackProgress) : 0, height: 6)
                    }
                }
                .frame(height: 6)
            }
        }
        .padding(12)
        .background(Color.primaryIndigoContainerLight.opacity(0.4))
        .overlay(
            RoundedRectangle(cornerRadius: 16)
                .stroke(Color.primaryIndigo.opacity(0.2), lineWidth: 1)
        )
        .cornerRadius(16)
    }
}

public struct MediaGalleryCarouselView: View {
    public let mediaList: [MediaItem]
    @State private var selectedMedia: MediaItem?

    public init(mediaList: [MediaItem]) {
        self.mediaList = mediaList
    }

    public var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 10) {
                ForEach(mediaList) { item in
                    if item.type == .photo {
                        if let uiImage = UIImage(contentsOfFile: item.filePath) {
                            Image(uiImage: uiImage)
                                .resizable()
                                .scaledToFill()
                                .frame(width: 90, height: 90)
                                .clipShape(RoundedRectangle(cornerRadius: 14))
                                .overlay(
                                    RoundedRectangle(cornerRadius: 14)
                                        .stroke(Color.appCardBorderLight, lineWidth: 1)
                                )
                        }
                    } else if item.type == .video {
                        ZStack {
                            RoundedRectangle(cornerRadius: 14)
                                .fill(Color.black.opacity(0.8))
                                .frame(width: 90, height: 90)
                            VStack(spacing: 4) {
                                Image(systemName: "video.fill")
                                    .font(.system(size: 20))
                                    .foregroundColor(.white)
                                Text("Vidéo")
                                    .font(.system(size: 10, weight: .bold))
                                    .foregroundColor(.white)
                            }
                        }
                    }
                }
            }
            .padding(.vertical, 4)
        }
    }
}
