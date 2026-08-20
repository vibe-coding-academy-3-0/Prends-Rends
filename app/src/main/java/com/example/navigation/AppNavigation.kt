package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.CreateEditLoanScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DetailLoanScreen
import com.example.viewmodel.LoanViewModel

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object CreateLoan : Screen("create_loan")
    object EditLoan : Screen("edit_loan/{loanId}") {
        fun createRoute(loanId: Long) = "edit_loan/$loanId"
    }
    object DetailLoan : Screen("detail_loan/{loanId}") {
        fun createRoute(loanId: Long) = "detail_loan/$loanId"
    }
}

@Composable
fun AppNavigation(
    viewModel: LoanViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                viewModel = viewModel,
                onAddNewLoan = { navController.navigate(Screen.CreateLoan.route) },
                onLoanClick = { loanId -> navController.navigate(Screen.DetailLoan.createRoute(loanId)) }
            )
        }

        composable(Screen.CreateLoan.route) {
            CreateEditLoanScreen(
                loanId = 0L,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditLoan.route,
            arguments = listOf(navArgument("loanId") { type = NavType.LongType })
        ) { backStackEntry ->
            val loanId = backStackEntry.arguments?.getLong("loanId") ?: 0L
            CreateEditLoanScreen(
                loanId = loanId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.DetailLoan.route,
            arguments = listOf(navArgument("loanId") { type = NavType.LongType })
        ) { backStackEntry ->
            val loanId = backStackEntry.arguments?.getLong("loanId") ?: 0L
            DetailLoanScreen(
                loanId = loanId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onEditLoan = { id -> navController.navigate(Screen.EditLoan.createRoute(id)) }
            )
        }
    }
}
