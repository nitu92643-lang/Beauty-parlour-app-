package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatCard
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.S
import com.example.ui.theme.DulhanGold
import com.example.ui.theme.DulhanGoldPale
import com.example.ui.theme.DulhanMaroon
import com.example.ui.theme.DulhanMaroonDeep
import com.example.ui.theme.DulhanPink
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusWarning
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.ParlourViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminDashboardScreen(
    viewModel: ParlourViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val isAdminUnlocked by viewModel.isAdminUnlocked.collectAsState()
    val pinError by viewModel.adminPinError.collectAsState()
    val allCustomers by viewModel.allCustomers.collectAsState()
    val allAppointments by viewModel.allAppointments.collectAsState()
    val allBills by viewModel.allBills.collectAsState()
    val allInventory by viewModel.allInventory.collectAsState()
    val lowStockItems by viewModel.lowStockInventory.collectAsState()
    val allStaff by viewModel.allStaff.collectAsState()
    val allServices by viewModel.allServices.collectAsState()
    val config by viewModel.parlourConfig.collectAsState()
    val context = LocalContext.current

    var pinInput by remember { mutableStateOf("") }
    var showChangePinDialog by remember { mutableStateOf(false) }

    val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val todayAppointments = allAppointments.filter { it.date == todayStr }
    val completedCount = allAppointments.count { it.status == "COMPLETED" }
    val todayRevenue = allBills.filter { it.date == todayStr && it.paymentStatus == "PAID" }.sumOf { it.totalAmount }
    val monthRevenue = allBills.filter { it.paymentStatus == "PAID" }.sumOf { it.totalAmount }
    val pendingTotal = allBills.filter { it.paymentStatus == "PENDING" }.sumOf { it.totalAmount }

    if (!isAdminUnlocked) {
        // PIN Lock Screen
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .testTag("admin_lock_screen"),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(DulhanMaroon),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = DulhanGold,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = S.adminTitle.get(language),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = DulhanMaroon
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = S.enterPin.get(language),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = {
                            if (it.length <= 6) pinInput = it
                        },
                        placeholder = { Text("••••") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        isError = pinError != null,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_pin_input"),
                        singleLine = true
                    )

                    if (pinError != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = pinError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "(${S.defaultPinHint.get(language)})",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            val activePin = config?.adminPin ?: "1234"
                            viewModel.verifyAdminPin(pinInput, activePin)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DulhanMaroon),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("admin_unlock_button")
                    ) {
                        Icon(Icons.Default.LockOpen, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(S.unlock.get(language), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    } else {
        // Admin Dashboard Content
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Admin Top Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = S.adminTitle.get(language),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = DulhanMaroon
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.HINDI) "व्यापार सारांश और नियंत्रण" else "Business Analytics & Controls",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    OutlinedButton(
                        onClick = { viewModel.lockAdmin() },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(S.lockAdmin.get(language), fontSize = 11.sp)
                    }
                }
            }

            // Row 1: Revenue stats
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = S.todaySales.get(language),
                        value = "₹${todayRevenue.toInt()}",
                        subtitle = "${todayAppointments.size} appts today",
                        icon = Icons.Default.Payments,
                        iconTint = StatusSuccess,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = S.monthlySales.get(language),
                        value = "₹${monthRevenue.toInt()}",
                        subtitle = "${allBills.size} total bills",
                        icon = Icons.Default.Receipt,
                        iconTint = DulhanMaroon,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Row 2: Customer & Appointment stats
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = S.totalCustomers.get(language),
                        value = "${allCustomers.size}",
                        subtitle = "${allCustomers.count { it.membershipTier == "DULHAN_VIP" }} VIPs",
                        icon = Icons.Default.People,
                        iconTint = Color(0xFF5E35B1),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = S.completedAppointments.get(language),
                        value = "$completedCount",
                        subtitle = "₹${pendingTotal.toInt()} pending",
                        icon = Icons.Default.CheckCircle,
                        iconTint = DulhanPink,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Low Stock Warning Card
            if (lowStockItems.isNotEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = StatusWarning.copy(alpha = 0.15f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = StatusWarning)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "${lowStockItems.size} ${S.lowStockAlert.get(language)}",
                                    fontWeight = FontWeight.Bold,
                                    color = StatusWarning
                                )
                            }
                            Button(
                                onClick = { viewModel.navigateTo(AppScreen.INVENTORY) },
                                colors = ButtonDefaults.buttonColors(containerColor = StatusWarning),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(S.restock.get(language), fontSize = 11.sp, color = Color.White)
                            }
                        }
                    }
                }
            }

            // Quick Management Links
            item {
                SectionHeader(title = if (language == AppLanguage.HINDI) "त्वरित प्रबंधन शॉर्टकट" else "Quick Management")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.STAFF) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(S.staffMgmt.get(language), fontSize = 12.sp, color = DulhanMaroon, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.INVENTORY) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(S.inventoryTitle.get(language), fontSize = 12.sp, color = DulhanMaroon, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Backup & Export Business Records
            item {
                SectionHeader(title = S.backupData.get(language))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DulhanGoldPale),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (language == AppLanguage.HINDI)
                                "ग्राहकों, बिलों और अपॉइंटमेंट्स का संपूर्ण डाटा सुरक्षित रखें या शेयर करें।"
                            else
                                "Export and secure full snapshot of customers, invoices, and bookings.",
                            style = MaterialTheme.typography.bodySmall.copy(color = DulhanMaroonDeep)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    val summaryReport = buildBackupReport(allCustomers, allBills, allAppointments)
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Dulhan_Backup", summaryReport)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, S.backupSuccess.get(language), Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = DulhanMaroon),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Backup, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (language == AppLanguage.HINDI) "कॉपी करें" else "Copy Backup", fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    val summaryReport = buildBackupReport(allCustomers, allBills, allAppointments)
                                    val intent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, summaryReport)
                                    }
                                    context.startActivity(Intent.createChooser(intent, "Share Business Backup"))
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(if (language == AppLanguage.HINDI) "शेयर करें" else "Share Backup", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(75.dp))
            }
        }
    }
}

private fun buildBackupReport(
    customers: List<com.example.data.model.CustomerEntity>,
    bills: List<com.example.data.model.BillEntity>,
    appointments: List<com.example.data.model.AppointmentEntity>
): String {
    return """
DULHAN BEAUTY PARLOUR - BUSINESS BACKUP REPORT
Generated At: ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())}
----------------------------------------
SUMMARY:
- Total Registered Customers: ${customers.size}
- Total Invoices Generated: ${bills.size}
- Total Appointments Logged: ${appointments.size}
- Total Revenue: ₹${bills.filter { it.paymentStatus == "PAID" }.sumOf { it.totalAmount }.toInt()}
- Total Pending Dues: ₹${bills.filter { it.paymentStatus == "PENDING" }.sumOf { it.totalAmount }.toInt()}

RECENT INVOICES:
${bills.take(10).joinToString("\n") { "${it.billNumber} | ${it.date} | ${it.customerName} | ₹${it.totalAmount.toInt()} | ${it.paymentStatus}" }}

CUSTOMER DIRECTORY:
${customers.joinToString("\n") { "${it.name} (${it.phone}) - Visits: ${it.totalVisits}, Spend: ₹${it.totalSpend.toInt()}" }}
----------------------------------------
    """.trimIndent()
}
