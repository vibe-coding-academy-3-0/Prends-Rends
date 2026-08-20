import SwiftUI

@main
struct PrendsEtRendsApp: App {
    @StateObject private var viewModel = LoanViewModel()

    var body: some Scene {
        WindowGroup {
            DashboardView(viewModel: viewModel)
                .preferredColorScheme(
                    viewModel.currentThemeMode == .dark ? .dark :
                    viewModel.currentThemeMode == .light ? .light : nil
                )
        }
    }
}
