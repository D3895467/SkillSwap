package uk.ac.tees.mad.d3895467.screen

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import uk.ac.tees.mad.d3895467.Constants
import uk.ac.tees.mad.d3895467.Constants.mAuthName
import uk.ac.tees.mad.d3895467.R
import uk.ac.tees.mad.d3895467.SkillSwapAppBar
import uk.ac.tees.mad.d3895467.SkillSwapAppScreen


@Composable
fun MainScreen(
    onLogin: () -> Unit = {},
    modifier: Modifier = Modifier.background(color = Color.White)
){


    var showBottomBar by rememberSaveable { mutableStateOf(true) }
    val navController = rememberNavController()//rememberNavController.withSentryObservableEffect()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = SkillSwapAppScreen.valueOf(
        backStackEntry?.destination?.route ?: SkillSwapAppScreen.Home.name
    )

    showBottomBar = when (backStackEntry?.destination?.route) {
        SkillSwapAppScreen.AddSkill.name -> false
        else -> true
    }
    showBottomBar = when (backStackEntry?.destination?.route) {
        SkillSwapAppScreen.ChangePassword.name -> false
        else -> true
    }
    showBottomBar = when (backStackEntry?.destination?.route) {
        SkillSwapAppScreen.ProfileEdit.name -> false
        else -> true
    }
    showBottomBar = when (backStackEntry?.destination?.route) {
        SkillSwapAppScreen.Feedback.name -> false
        else -> true
    }
    showBottomBar = when (backStackEntry?.destination?.route) {
        SkillSwapAppScreen.SkillDetailScreen.name -> false
        else -> true
    }
    showBottomBar = when (backStackEntry?.destination?.route) {
        SkillSwapAppScreen.AllSkillScreen.name -> false
        else -> true
    }

    val mAuth = FirebaseAuth.getInstance()
    val currentUserUid = mAuth.currentUser?.uid

    val timeStamp = Constants.skillId

    Scaffold(
        topBar = {
            if (SkillSwapAppScreen.Chat.name  == currentScreen.name || SkillSwapAppScreen.Search.name == currentScreen.name) {
                SkillSwapAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = false,//navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }
                )
            }
            else if (SkillSwapAppScreen.Home.name == currentScreen.name ||SkillSwapAppScreen.Profile.name == currentScreen.name  ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    //.fillMaxSize()
                    //.background(color = Color.Blue)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.headlineLarge,
                        text = "Good Morning $mAuthName" ,
                        textAlign = TextAlign.Start,
                        color = Color.Black // Change text color here
                    )
                }
            }
            else {
                SkillSwapAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }
                )
            }
        },
        floatingActionButton = {
            if (SkillSwapAppScreen.Home.name == currentScreen.name) {
                FloatingActionButton(onClick = {
                    navController.navigate("addSkill")
                    /*showBottomBar = !showBottomBar*/
                }) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "add"
                    )
                }
            } else if (SkillSwapAppScreen.SkillDetailScreen.name == currentScreen.name || SkillSwapAppScreen.AllSkillScreen.name == currentScreen.name) {
                ExtendedFloatingActionButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "add"
                    )
                    Text(text = "Back")
                }
            }
        },

        bottomBar = {
            if (showBottomBar) {
                NavigationBar(modifier = Modifier.padding(start = 20.dp, end = 20.dp).background(shape = RoundedCornerShape(20.dp), color = Color.Transparent ), containerColor = MaterialTheme.colorScheme.primaryContainer) {
                    NavigationBarItem(
                        selected = backStackEntry?.destination?.route == "home",
                        onClick = { navController.navigate("home") },
                        label = {
                            Text(
                                "Home"
                            )
                        },
                        icon = {
                            Icon(
                                Icons.Filled.Home,
                                contentDescription = "Home"
                            )
                        }
                    )
                    NavigationBarItem(
                        selected = backStackEntry?.destination?.route == "search",
                        onClick = { navController.navigate("search") },
                        label = {
                            Text("Search")
                        },
                        icon = {
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = "Search"
                            )
                        }
                    )
                    NavigationBarItem(
                        selected = backStackEntry?.destination?.route == "Chat",
                        onClick = { navController.navigate("Chat") },
                        label = {
                            Text("Chat")
                        },
                        icon = {
                            Icon(
                                painterResource(id = R.drawable.ic_chat),
                                contentDescription = "chat"
                            )
                        }
                    )
                    NavigationBarItem(
                        selected = backStackEntry?.destination?.route?.startsWith("profile")
                            ?: false,
                        onClick = { navController.navigate("profile") },
                        label = {
                            Text("Profile")
                        },
                        icon = {
                            Icon(
                                Icons.Filled.AccountCircle,
                                contentDescription = "Profile"
                            )
                        }
                    )
                }
            }
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable(SkillSwapAppScreen.Home.name) {

                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = Color.White // Change background color here
                    ) {
                        HomeScreen(
                            userId = currentUserUid.toString(),
                            navController = navController
                        )

                    }
                }
                composable(SkillSwapAppScreen.Search.name) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = Color.White // Change background color here
                    ) {
                        SearchScreen(
                            userId = currentUserUid.toString(),
                            navController
                        )
                    }
                }
            }
        }
    )
}