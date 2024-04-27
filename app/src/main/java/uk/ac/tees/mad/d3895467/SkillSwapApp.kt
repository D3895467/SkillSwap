package uk.ac.tees.mad.d3895467

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import uk.ac.tees.mad.d3895467.screen.ForgotPasswordScreen
import uk.ac.tees.mad.d3895467.screen.LoginScreen
import uk.ac.tees.mad.d3895467.screen.MainScreen
import uk.ac.tees.mad.d3895467.screen.RegistrationScreen
import uk.ac.tees.mad.d3895467.screen.SplashScreen


enum class SkillSwapAppScreen(@StringRes val title: Int) {
    Splash(title = R.string.Splash),
    Login(title = R.string.login),
    Registration(title = R.string.registered),
    ForgotPassword(title = R.string.ForgotPassword),
    MainScreen(title = R.string.mainScreen),
    Home(title = R.string.home),
    Profile(title = R.string.profile),
    Chat(title = R.string.chat),
    Search(title = R.string.search),
    AddSkill(title = R.string.addSkill),
    SkillDetailScreen(title = R.string.SkillDetailScreen),
    Feedback(title = R.string.Feedback),
    ProfileEdit(title = R.string.ProfileEdit),
    ChangePassword(title = R.string.changePassword),
    AllSkillScreen(title = R.string.AllSkillScreen),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillSwapAppBar(
    currentScreen: SkillSwapAppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            if (canNavigateBack)
                IconButton(onClick = navigateUp) {

                    Icon(imageVector = Icons.Outlined.KeyboardArrowLeft, contentDescription = null)
                }
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillSwapApp(
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = SkillSwapAppScreen.valueOf(
        backStackEntry?.destination?.route ?: SkillSwapAppScreen.Login.name
    )

    Scaffold(

    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = SkillSwapAppScreen.Splash.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = SkillSwapAppScreen.Splash.name) {
                val context = LocalContext.current
                SplashScreen(
                    onLogin = {
                        navController.navigate(SkillSwapAppScreen.Login.name)
                    },
                    onHome = {
                        navController.navigate(SkillSwapAppScreen.MainScreen.name)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = SkillSwapAppScreen.Login.name) {
                val context = LocalContext.current
                LoginScreen(
                    onLoginButtonClicked = {
                        navController.navigate(SkillSwapAppScreen.MainScreen.name)
                    },
                    onRegistered = {
                        navController.navigate(SkillSwapAppScreen.Registration.name)
                    },
                    onForgotPassword = {
                        navController.navigate(SkillSwapAppScreen.ForgotPassword.name)
                    },
                    )
            }
            composable(route = SkillSwapAppScreen.Registration.name) {
                val context = LocalContext.current
                RegistrationScreen(
                    onLoginButtonClicked = {
                        navController.navigate(SkillSwapAppScreen.Login.name)
                    },
                )
            }
            composable(route = SkillSwapAppScreen.ForgotPassword.name) {
                val context = LocalContext.current
                ForgotPasswordScreen(
                    onLogin = {
                        navController.navigate(SkillSwapAppScreen.Login.name)
                    },
                    onRegistered = {
                        navController.navigate(SkillSwapAppScreen.Registration.name)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = SkillSwapAppScreen.MainScreen.name) {
                val context = LocalContext.current
                MainScreen(
                    onLogin = {
                        navController.navigate(SkillSwapAppScreen.Login.name)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}
