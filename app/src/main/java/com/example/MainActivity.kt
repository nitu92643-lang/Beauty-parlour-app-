package com.example

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.TopHeaderBar
import com.example.ui.localization.S
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.BillingScreen
import com.example.ui.screens.BookBeautyAppointmentScreen
import com.example.ui.screens.BookingScreen
import com.example.ui.screens.BridalScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.CustomerScreen
import com.example.ui.screens.GalleryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InventoryScreen
import com.example.ui.screens.OffersScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.screens.StaffScreen
import com.example.ui.theme.DulhanGold
import com.example.ui.theme.DulhanMaroon
import com.example.ui.theme.DulhanPink
import com.example.ui.theme.DulhanTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.ParlourViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DulhanTheme {
                DulhanParlourApp()
            }
        }
    }
}

@Composable
fun DulhanParlourApp(
    viewModel: ParlourViewModel = viewModel()
) {
    val language by viewModel.currentLanguage.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isAdminUnlocked by viewModel.isAdminUnlocked.collectAsState()
    val config by viewModel.parlourConfig.collectAsState()
    val context = LocalContext.current

    // Handle Android system back button properly
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.navigateBack()
    }

    Scaffold(
        topBar = {
            TopHeaderBar(
                language = language,
                isAdminUnlocked = isAdminUnlocked,
                onToggleLanguage = { viewModel.toggleLanguage() },
                onCallClick = { openDialer(context, config?.phone ?: S.defaultPhone) },
                onWhatsAppClick = { openWhatsApp(context, config?.whatsapp ?: S.defaultWhatsApp) },
                onAdminClick = { viewModel.navigateTo(AppScreen.ADMIN) }
            )
        },
        bottomBar = {
            val isMainScreen = currentScreen in listOf(
                AppScreen.HOME,
                AppScreen.SERVICES,
                AppScreen.BRIDAL,
                AppScreen.BOOKING,
                AppScreen.BILLING
            )

            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                // 1. Home
                NavigationBarItem(
                    selected = currentScreen == AppScreen.HOME,
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    icon = { Icon(Icons.Default.Home, contentDescription = S.tabHome.get(language)) },
                    label = {
                        Text(
                            text = S.tabHome.get(language),
                            fontSize = 11.sp,
                            fontWeight = if (currentScreen == AppScreen.HOME) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DulhanMaroon,
                        selectedTextColor = DulhanMaroon,
                        indicatorColor = DulhanPink.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_home")
                )

                // 2. Services
                NavigationBarItem(
                    selected = currentScreen == AppScreen.SERVICES,
                    onClick = { viewModel.navigateTo(AppScreen.SERVICES) },
                    icon = { Icon(Icons.Default.Spa, contentDescription = S.tabServices.get(language)) },
                    label = {
                        Text(
                            text = S.tabServices.get(language),
                            fontSize = 11.sp,
                            fontWeight = if (currentScreen == AppScreen.SERVICES) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DulhanMaroon,
                        selectedTextColor = DulhanMaroon,
                        indicatorColor = DulhanPink.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_services")
                )

                // 3. Bridal
                NavigationBarItem(
                    selected = currentScreen == AppScreen.BRIDAL,
                    onClick = { viewModel.navigateTo(AppScreen.BRIDAL) },
                    icon = { Icon(Icons.Default.Diamond, contentDescription = S.tabBridal.get(language)) },
                    label = {
                        Text(
                            text = S.tabBridal.get(language),
                            fontSize = 11.sp,
                            fontWeight = if (currentScreen == AppScreen.BRIDAL) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DulhanMaroon,
                        selectedTextColor = DulhanMaroon,
                        indicatorColor = DulhanPink.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_bridal")
                )

                // 4. Booking
                NavigationBarItem(
                    selected = currentScreen == AppScreen.BOOKING || currentScreen == AppScreen.BOOK_APPOINTMENT,
                    onClick = { viewModel.navigateTo(AppScreen.BOOK_APPOINTMENT) },
                    icon = { Icon(Icons.Default.BookOnline, contentDescription = S.tabBooking.get(language)) },
                    label = {
                        Text(
                            text = S.tabBooking.get(language),
                            fontSize = 11.sp,
                            fontWeight = if (currentScreen == AppScreen.BOOKING || currentScreen == AppScreen.BOOK_APPOINTMENT) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DulhanMaroon,
                        selectedTextColor = DulhanMaroon,
                        indicatorColor = DulhanPink.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_booking")
                )

                // 5. Billing
                NavigationBarItem(
                    selected = currentScreen == AppScreen.BILLING,
                    onClick = { viewModel.navigateTo(AppScreen.BILLING) },
                    icon = { Icon(Icons.Default.Receipt, contentDescription = S.tabBilling.get(language)) },
                    label = {
                        Text(
                            text = S.tabBilling.get(language),
                            fontSize = 11.sp,
                            fontWeight = if (currentScreen == AppScreen.BILLING) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DulhanMaroon,
                        selectedTextColor = DulhanMaroon,
                        indicatorColor = DulhanPink.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_billing")
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> HomeScreen(viewModel = viewModel)
                AppScreen.SERVICES -> ServicesScreen(viewModel = viewModel)
                AppScreen.BRIDAL -> BridalScreen(viewModel = viewModel)
                AppScreen.BOOKING -> BookingScreen(viewModel = viewModel)
                AppScreen.BOOK_APPOINTMENT -> BookBeautyAppointmentScreen(
                    viewModel = viewModel,
                    onNavigateBack = { viewModel.navigateBack() },
                    onNavigateToBookings = { viewModel.navigateTo(AppScreen.BOOKING) }
                )
                AppScreen.BILLING -> BillingScreen(viewModel = viewModel)
                AppScreen.CUSTOMERS -> CustomerScreen(viewModel = viewModel)
                AppScreen.STAFF -> StaffScreen(viewModel = viewModel)
                AppScreen.INVENTORY -> InventoryScreen(viewModel = viewModel)
                AppScreen.OFFERS -> OffersScreen(viewModel = viewModel)
                AppScreen.GALLERY -> GalleryScreen(viewModel = viewModel)
                AppScreen.ADMIN -> AdminDashboardScreen(viewModel = viewModel)
                AppScreen.CONTACT -> ContactScreen(viewModel = viewModel)
            }
        }
    }
}

private fun openDialer(context: Context, phone: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone.replace(" ", "")}"))
        context.startActivity(intent)
    } catch (_: Exception) {}
}

private fun openWhatsApp(context: Context, phone: String) {
    try {
        val cleanNumber = phone.replace("+", "").replace(" ", "")
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://api.whatsapp.com/send?phone=$cleanNumber&text=Hello%20Dulhan%20Beauty%20Parlour")
        )
        context.startActivity(intent)
    } catch (_: Exception) {}
}
