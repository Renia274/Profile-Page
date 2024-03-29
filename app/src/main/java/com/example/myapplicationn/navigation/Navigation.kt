package com.example.myapplicationn.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplicationn.PostDetail
import com.example.myapplicationn.screens.page.ProfilePage
import com.example.myapplicationn.screens.postlist.PostList
import com.example.myapplicationn.viewModel.SearchViewModel

sealed class Screen(val route: String, val arguments: List<NamedNavArgument> = emptyList()) {
    object Profile : Screen("profile")
    object PostList : Screen("post_list")
    object PostDetail : Screen("post_detail/{postId}", listOf(navArgument("postId") { type = NavType.IntType }))
}

@Composable
fun SetupNavigation(navController: NavHostController, viewModel: SearchViewModel) {
    NavHost(navController = navController, startDestination = Screen.Profile.route) {
        composable(route = Screen.Profile.route) {
            ProfilePage(
                viewModel = viewModel,
                onNavigate = { id ->
                    navController.navigate(Screen.PostDetail.route.replace("{postId}", id.toString()))
                },
                postListNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable(route = Screen.PostList.route) {
            PostList(viewModel = viewModel, onNavigateToProfile = {
                // Use the navController to navigate to the Profile page
                navController.navigate(Screen.Profile.route)
            }, onNavigateToPostDetail = { postId ->
                // Use the navController to navigate to the PostDetail page
                navController.navigate(Screen.PostDetail.route.replace("{postId}", postId.toString()))
            })
        }

        composable(
            route = Screen.PostDetail.route,
            arguments = Screen.PostDetail.arguments
        ) { navBackStackEntry ->
            val postId = navBackStackEntry.arguments?.getInt("postId")
            if (postId != null) {
                PostDetail(postId = postId, onNavigate = { route ->
                    navController.navigate(route)
                })
            }
        }
    }
}
