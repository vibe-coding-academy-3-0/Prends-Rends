import Foundation
import AVFoundation

public final class AudioRecorderPlayer: NSObject, ObservableObject, AVAudioPlayerDelegate, AVAudioRecorderDelegate {
    public static let shared = AudioRecorderPlayer()

    private var audioRecorder: AVAudioRecorder?
    private var audioPlayer: AVAudioPlayer?
    private var timer: Timer?

    @Published public var isRecording: Bool = false
    @Published public var recordingDuration: Int = 0
    @Published public var isPlaying: Bool = false
    @Published public var currentPlayingPath: String? = nil
    @Published public var playbackProgress: Double = 0.0

    private var progressTimer: Timer?

    public override init() {
        super.init()
    }

    public func requestPermissions() {
        AVAudioSession.sharedInstance().requestRecordPermission { _ in }
    }

    public func startRecording() -> String? {
        let session = AVAudioSession.sharedInstance()
        do {
            try session.setCategory(.playAndRecord, mode: .default, options: [.defaultToSpeaker, .allowBluetooth])
            try session.setActive(true)

            let mediaDir = StorageService.shared.getMediaDirectoryURL()
            let fileName = "voice_note_\(Int64(Date().timeIntervalSince1970 * 1000)).m4a"
            let fileURL = mediaDir.appendingPathComponent(fileName)

            let settings: [String: Any] = [
                AVFormatIDKey: Int(kAudioFormatMPEG4AAC),
                AVSampleRateKey: 44100.0,
                AVNumberOfChannelsKey: 1,
                AVEncoderAudioQualityKey: AVAudioQuality.high.rawValue
            ]

            audioRecorder = try AVAudioRecorder(url: fileURL, settings: settings)
            audioRecorder?.delegate = self
            audioRecorder?.record()

            isRecording = true
            recordingDuration = 0

            timer?.invalidate()
            timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: true) { [weak self] _ in
                guard let self = self else { return }
                self.recordingDuration += 1
            }

            return fileURL.path
        } catch {
            print("AudioRecorderPlayer: Failed to start recording: \(error)")
            return nil
        }
    }

    public func stopRecording() -> String? {
        timer?.invalidate()
        timer = nil
        isRecording = false

        guard let recorder = audioRecorder else { return nil }
        let path = recorder.url.path
        recorder.stop()
        audioRecorder = nil
        return path
    }

    public func cancelRecording() {
        timer?.invalidate()
        timer = nil
        isRecording = false
        recordingDuration = 0

        if let recorder = audioRecorder {
            let path = recorder.url.path
            recorder.stop()
            audioRecorder = nil
            try? FileManager.default.removeItem(atPath: path)
        }
    }

    public func playAudio(from path: String) {
        if isPlaying && currentPlayingPath == path {
            pauseAudio()
            return
        }

        stopAudio()

        let url = URL(fileURLWithPath: path)
        guard FileManager.default.fileExists(atPath: path) else { return }

        do {
            let session = AVAudioSession.sharedInstance()
            try session.setCategory(.playback, mode: .default)
            try session.setActive(true)

            audioPlayer = try AVAudioPlayer(contentsOf: url)
            audioPlayer?.delegate = self
            audioPlayer?.play()

            isPlaying = true
            currentPlayingPath = path

            progressTimer?.invalidate()
            progressTimer = Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true) { [weak self] _ in
                guard let self = self, let player = self.audioPlayer, player.duration > 0 else { return }
                self.playbackProgress = player.currentTime / player.duration
            }
        } catch {
            print("AudioRecorderPlayer: Error playing audio: \(error)")
        }
    }

    public func pauseAudio() {
        audioPlayer?.pause()
        isPlaying = false
        progressTimer?.invalidate()
        progressTimer = nil
    }

    public func stopAudio() {
        progressTimer?.invalidate()
        progressTimer = nil
        audioPlayer?.stop()
        audioPlayer = nil
        isPlaying = false
        currentPlayingPath = nil
        playbackProgress = 0.0
    }

    public func audioPlayerDidFinishPlaying(_ player: AVAudioPlayer, successfully flag: Bool) {
        stopAudio()
    }
}
