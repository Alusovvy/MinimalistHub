package com.example.minimalistinfohub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.minimalistinfohub.location.LocationUtils
import com.example.minimalistinfohub.news.NewsScreen
import com.example.minimalistinfohub.stock.StockScreen
import com.example.minimalistinfohub.ui.theme.MinimalistInfoHubTheme
import com.example.minimalistinfohub.weather.WeatherScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MinimalistInfoHubTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavStartupWrapper(navController)
                }
            }
        }
    }
}

@Composable
fun NavStartup(navController: NavHostController, pd: PaddingValues) {
    val context = LocalContext.current
    val locUtils = LocationUtils(context)


    NavHost(navController = navController, startDestination = "stockWatch", modifier = Modifier.padding(pd)) {

        //bottom menu
        composable(Screen.BottomScreen.Stock.bRoute) {
            StockScreen()
        }

        composable(Screen.BottomScreen.Finance.bRoute) {
            NewsScreen()
        }

        composable(Screen.BottomScreen.Weather.bRoute) {
            WeatherScreen(
                locUtils,
                context
            )
        }

        //drawer menu
        composable(Screen.DrawerScreen.Weather.route) {
            WeatherScreen(
                locUtils,
                context
            )
        }

        composable(Screen.DrawerScreen.Finance.route) {
            NewsScreen()
        }

        composable(Screen.DrawerScreen.Stock.route) {
            StockScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavStartupWrapper(
    navController: NavHostController
) {

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope: CoroutineScope = rememberCoroutineScope()
    val title = remember {
        mutableStateOf("Title")
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val viewModel : ScreenViewModel = viewModel()

    val currentScreen = remember {
        viewModel.currentScreen.value
    }

    val bottomBar: @Composable () -> Unit = {
        if (currentScreen is Screen.DrawerScreen) {
            BottomNavigation(modifier = Modifier.wrapContentSize()) {
                screensInBottom.forEach { item ->
                    BottomNavigationItem(
                        selected = currentRoute == item.bRoute,
                        onClick = { navController.navigate(item.bRoute) },
                        icon = {
                            Icon(
                                contentDescription = item.bTitle,
                                painter = painterResource(id = item.icon)
                            )
                        },
                        label = { Text(text = item.bTitle) },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.Black
                    )
                }
            }
        }
    }


    Scaffold(
        bottomBar = bottomBar,
        topBar = {
            TopAppBar(title = { Text(title.value) },
                navigationIcon = { IconButton(onClick = {
                    //open the drawer
                    scope.launch {
                        title.value = "Stock Watch"
                        scaffoldState.drawerState.open()
                    }
                }) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                }
                })
        },
        scaffoldState = scaffoldState,
        drawerContent = {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(screensInDrawer) {
                    DrawerItem(
                        scaffoldState = scaffoldState,
                        navController = navController,
                        scope = scope,
                        title = title,
                        screen =  it
                    )
                }
            }
        }
    ) {
        NavStartup(navController, pd = it)
    }
}

@Composable
fun DrawerItem(scaffoldState: ScaffoldState, navController: NavHostController,scope: CoroutineScope, title : MutableState<String>, screen : Screen) {
    Button(onClick = {
        navController.navigate(screen.route)
        title.value = screen.title
        scope.launch {
            scaffoldState.drawerState.close()
        }
    }) {
        Text(screen.title)
    }
}