import SwiftUI

public struct DashboardView: View {
    @ObservedObject public var viewModel: LoanViewModel
    @State private var showingCreateSheet = false
    @State private var selectedLoanForDetail: LoanItem?

    public init(viewModel: LoanViewModel) {
        self.viewModel = viewModel
    }

    private var lang: AppLanguage { viewModel.currentLanguage }

    public var body: some View {
        NavigationView {
            ZStack(alignment: .bottomTrailing) {
                Color.appSurfaceLight
                    .ignoresSafeArea()

                ScrollView {
                    VStack(spacing: 16) {
                        // Top Branding Header
                        HStack(spacing: 12) {
                            ZStack {
                                RoundedRectangle(cornerRadius: 12)
                                    .fill(
                                        LinearGradient(
                                            colors: [Color.primaryIndigo, Color.accentPurple],
                                            startPoint: .topLeading,
                                            endPoint: .bottomTrailing
                                        )
                                    )
                                    .frame(width: 40, height: 40)
                                Image(systemName: "hand.raised.fill")
                                    .font(.system(size: 20))
                                    .foregroundColor(.white)
                            }

                            VStack(alignment: .leading, spacing: 2) {
                                Text(AppStrings.appTitle(lang))
                                    .font(.system(size: 18, weight: .heavy, design: .rounded))
                                    .foregroundColor(Color.primary)
                                Text(AppStrings.appSubtitle(lang))
                                    .font(.system(size: 11, weight: .medium))
                                    .foregroundColor(Color.appTextSecondaryLight)
                            }

                            Spacer()

                            ThemeSelectorView(currentTheme: $viewModel.currentThemeMode)
                            LanguageSelectorView(selectedLang: $viewModel.currentLanguage)
                        }
                        .padding(.horizontal, 16)
                        .padding(.top, 8)

                        // Search Bar
                        HStack(spacing: 8) {
                            Image(systemName: "magnifyingglass")
                                .font(.system(size: 15, weight: .semibold))
                                .foregroundColor(Color.appTextSecondaryLight)

                            TextField(AppStrings.searchPlaceholder(lang), text: $viewModel.searchQuery)
                                .font(.system(size: 14))

                            if !viewModel.searchQuery.isEmpty {
                                Button(action: { viewModel.searchQuery = "" }) {
                                    Image(systemName: "xmark.circle.fill")
                                        .foregroundColor(Color.appTextSecondaryLight)
                                }
                            }
                        }
                        .padding(.horizontal, 14)
                        .padding(.vertical, 10)
                        .background(Color.appCardBackgroundLight)
                        .overlay(
                            RoundedRectangle(cornerRadius: 24)
                                .stroke(Color.appBorderLight, lineWidth: 1)
                        )
                        .cornerRadius(24)
                        .padding(.horizontal, 16)

                        // Summary Statistics Header
                        SummaryHeaderView(
                            summary: viewModel.dashboardSummary,
                            lang: lang,
                            selectedTab: $viewModel.selectedTab,
                            selectedStatus: $viewModel.selectedStatusFilter
                        )

                        // Segmented Tab Pill Control (Tous / Prêtés / Empruntés)
                        HStack(spacing: 4) {
                            TabButton(
                                title: AppStrings.tabAll(lang),
                                isSelected: viewModel.selectedTab == .all,
                                onClick: { viewModel.selectedTab = .all }
                            )
                            TabButton(
                                title: AppStrings.tabLent(lang),
                                isSelected: viewModel.selectedTab == .lent,
                                onClick: { viewModel.selectedTab = .lent }
                            )
                            TabButton(
                                title: AppStrings.tabBorrowed(lang),
                                isSelected: viewModel.selectedTab == .borrowed,
                                onClick: { viewModel.selectedTab = .borrowed }
                            )
                        }
                        .padding(4)
                        .background(Color.appCardBorderLight.opacity(0.3))
                        .overlay(
                            RoundedRectangle(cornerRadius: 26)
                                .stroke(Color.appCardBorderLight, lineWidth: 1)
                        )
                        .cornerRadius(26)
                        .padding(.horizontal, 16)

                        // Status Filter Chips
                        ScrollView(.horizontal, showsIndicators: false) {
                            HStack(spacing: 8) {
                                StatusChip(
                                    title: AppStrings.filterAll(lang),
                                    isSelected: viewModel.selectedStatusFilter == .all,
                                    onClick: { viewModel.selectedStatusFilter = .all }
                                )
                                StatusChip(
                                    title: AppStrings.filterActive(lang),
                                    isSelected: viewModel.selectedStatusFilter == .active,
                                    onClick: { viewModel.selectedStatusFilter = .active }
                                )
                                StatusChip(
                                    title: AppStrings.filterOverdue(lang),
                                    isSelected: viewModel.selectedStatusFilter == .overdue,
                                    onClick: { viewModel.selectedStatusFilter = .overdue }
                                )
                                StatusChip(
                                    title: AppStrings.filterReturned(lang),
                                    isSelected: viewModel.selectedStatusFilter == .returned,
                                    onClick: { viewModel.selectedStatusFilter = .returned }
                                )
                            }
                            .padding(.horizontal, 16)
                        }

                        // Loan List or Empty State
                        if viewModel.filteredLoans.isEmpty {
                            VStack(spacing: 16) {
                                Spacer(minLength: 30)

                                ZStack {
                                    Circle()
                                        .fill(
                                            LinearGradient(
                                                colors: [Color.primaryIndigoContainerLight, Color.accentCyan.opacity(0.2)],
                                                startPoint: .topLeading,
                                                endPoint: .bottomTrailing
                                            )
                                        )
                                        .frame(width: 80, height: 80)
                                    Image(systemName: "tray.fill")
                                        .font(.system(size: 36))
                                        .foregroundColor(Color.primaryIndigo)
                                }

                                Text(viewModel.searchQuery.isEmpty && viewModel.selectedTab == .all && viewModel.selectedStatusFilter == .all ? AppStrings.emptyTitle(lang) : "Aucun prêt dans cette catégorie")
                                    .font(.system(size: 16, weight: .bold))
                                    .foregroundColor(Color.primary)

                                Text(AppStrings.emptySubtitle(lang))
                                    .font(.system(size: 13))
                                    .foregroundColor(Color.appTextSecondaryLight)
                                    .multilineTextAlignment(.center)
                                    .padding(.horizontal, 32)

                                Button(action: { showingCreateSheet = true }) {
                                    HStack(spacing: 6) {
                                        Image(systemName: "plus")
                                            .font(.system(size: 14, weight: .bold))
                                        Text(AppStrings.newLoanBtn(lang))
                                            .font(.system(size: 14, weight: .bold))
                                    }
                                    .foregroundColor(.white)
                                    .padding(.horizontal, 20)
                                    .padding(.vertical, 12)
                                    .background(Color.primaryIndigo)
                                    .cornerRadius(16)
                                    .shadow(color: Color.primaryIndigo.opacity(0.3), radius: 8, x: 0, y: 4)
                                }

                                Spacer(minLength: 60)
                            }
                            .frame(maxWidth: .infinity)
                        } else {
                            LazyVStack(spacing: 12) {
                                ForEach(viewModel.filteredLoans) { loan in
                                    LoanCardView(
                                        loan: loan,
                                        lang: lang,
                                        onClick: { selectedLoanForDetail = loan },
                                        onToggleReturned: { viewModel.toggleReturnedStatus(loan: loan) }
                                    )
                                }
                            }
                            .padding(.horizontal, 16)
                            .padding(.bottom, 80)
                        }
                    }
                }

                // Floating Action Button
                Button(action: { showingCreateSheet = true }) {
                    HStack(spacing: 8) {
                        Image(systemName: "plus")
                            .font(.system(size: 16, weight: .bold))
                        Text(AppStrings.newLoanBtn(lang))
                            .font(.system(size: 15, weight: .bold))
                    }
                    .foregroundColor(.white)
                    .padding(.horizontal, 20)
                    .padding(.vertical, 14)
                    .background(Color.primaryIndigo)
                    .cornerRadius(28)
                    .shadow(color: Color.primaryIndigo.opacity(0.4), radius: 10, x: 0, y: 6)
                }
                .padding(.trailing, 20)
                .padding(.bottom, 24)
            }
            .navigationBarHidden(true)
            .sheet(isPresented: $showingCreateSheet) {
                CreateEditLoanView(viewModel: viewModel, existingLoan: nil)
            }
            .sheet(item: $selectedLoanForDetail) { loan in
                DetailLoanView(viewModel: viewModel, loan: loan)
            }
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
}

private struct TabButton: View {
    let title: String
    let isSelected: Bool
    let onClick: () -> Void

    var body: some View {
        Button(action: onClick) {
            Text(title)
                .font(.system(size: 13, weight: isSelected ? .bold : .semibold))
                .foregroundColor(isSelected ? .white : Color.appTextSecondaryLight)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 9)
                .background(isSelected ? Color.primaryIndigo : Color.transparent)
                .cornerRadius(22)
        }
    }
}

private struct StatusChip: View {
    let title: String
    let isSelected: Bool
    let onClick: () -> Void

    var body: some View {
        Button(action: onClick) {
            Text(title)
                .font(.system(size: 12, weight: isSelected ? .bold : .medium))
                .foregroundColor(isSelected ? Color.primaryIndigo : Color.appTextSecondaryLight)
                .padding(.horizontal, 14)
                .padding(.vertical, 7)
                .background(isSelected ? Color.primaryIndigoContainerLight : Color.appCardBackgroundLight)
                .overlay(
                    RoundedRectangle(cornerRadius: 18)
                        .stroke(isSelected ? Color.primaryIndigo : Color.appBorderLight, lineWidth: 1)
                )
                .cornerRadius(18)
        }
    }
}
