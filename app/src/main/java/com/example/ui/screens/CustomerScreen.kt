package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CustomerEntity
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.S
import com.example.ui.theme.DulhanBlush
import com.example.ui.theme.DulhanGold
import com.example.ui.theme.DulhanGoldPale
import com.example.ui.theme.DulhanMaroon
import com.example.ui.theme.DulhanPink
import com.example.ui.viewmodel.ParlourViewModel

@Composable
fun CustomerScreen(
    viewModel: ParlourViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val allCustomers by viewModel.allCustomers.collectAsState()
    val allAppointments by viewModel.allAppointments.collectAsState()
    val allBills by viewModel.allBills.collectAsState()
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedCustomerForDetails by remember { mutableStateOf<CustomerEntity?>(null) }
    var customerToDelete by remember { mutableStateOf<CustomerEntity?>(null) }

    val filteredCustomers = allCustomers.filter { customer ->
        searchQuery.isBlank() ||
                customer.name.contains(searchQuery, ignoreCase = true) ||
                customer.phone.contains(searchQuery, ignoreCase = true)
    }

    Box(modifier = modifier.fillMaxSize().testTag("customer_screen")) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(S.searchCustomer.get(language)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = DulhanPink) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("customer_search_input"),
                singleLine = true
            )

            // Header stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${allCustomers.size} ${S.totalCustomers.get(language)}",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )

                val vipCount = allCustomers.count { it.membershipTier == "DULHAN_VIP" || it.membershipTier == "GOLD" }
                Text(
                    text = "★ $vipCount ${if (language == AppLanguage.HINDI) "वीआईपी मेंबर्स" else "VIP Members"}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = DulhanGold,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // Customer List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredCustomers, key = { it.id }) { customer ->
                    CustomerCard(
                        customer = customer,
                        language = language,
                        onClick = { selectedCustomerForDetails = customer },
                        onSendGreeting = {
                            sendGreetingWhatsApp(context, customer, language)
                        },
                        onDelete = {
                            customerToDelete = customer
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(75.dp))
                }
            }
        }

        // FAB to add new customer
        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = DulhanMaroon,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_customer_fab")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Customer")
        }
    }

    // Customer Detailed Profile Dialog
    if (selectedCustomerForDetails != null) {
        val cust = selectedCustomerForDetails!!
        val customerAppts = allAppointments.filter { it.customerPhone == cust.phone }
        val customerBills = allBills.filter { it.customerPhone == cust.phone }

        AlertDialog(
            onDismissRequest = { selectedCustomerForDetails = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(DulhanMaroon),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cust.name.take(1).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(cust.name, fontWeight = FontWeight.Bold)
                        Text(cust.phone, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = DulhanGoldPale,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${S.visits.get(language)}: ${cust.totalVisits}")
                                Text("${S.totalSpent.get(language)}: ₹${cust.totalSpend.toInt()}", fontWeight = FontWeight.Bold, color = DulhanMaroon)
                            }
                        }
                    }

                    if (cust.preferences.isNotBlank()) {
                        item {
                            Text(
                                text = "${S.customerPreferences.get(language)}:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = cust.preferences,
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }

                    if (!cust.birthday.isNullOrBlank()) {
                        item {
                            Text(
                                text = "🎂 ${S.birthday.get(language)}: ${cust.birthday}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${S.myBookings.get(language)} (${customerAppts.size}):",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    if (customerAppts.isEmpty()) {
                        item {
                            Text(
                                text = if (language == AppLanguage.HINDI) "कोई पिछला अपॉइंटमेंट नहीं" else "No appointments found",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    } else {
                        items(customerAppts) { appt ->
                            Text(
                                text = "• ${appt.date} - ${appt.serviceNameEn} (${appt.status})",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${S.billingTitle.get(language)} (${customerBills.size}):",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    items(customerBills) { bill ->
                        Text(
                            text = "• ${bill.date} - ₹${bill.totalAmount.toInt()} (${bill.paymentMode})",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        sendGreetingWhatsApp(context, cust, language)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(S.sendGreeting.get(language))
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedCustomerForDetails = null }) {
                    Text(S.close.get(language))
                }
            }
        )
    }

    // Add New Customer Dialog
    if (showAddDialog) {
        var name by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        var preferences by remember { mutableStateOf("") }
        var bday by remember { mutableStateOf("") }
        var anniv by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(S.addCustomer.get(language), fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(S.customerName.get(language)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text(S.customerPhone.get(language)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = preferences,
                        onValueChange = { preferences = it },
                        label = { Text(S.customerPreferences.get(language)) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = bday,
                            onValueChange = { bday = it },
                            label = { Text("${S.birthday.get(language)} (DD-MM)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = anniv,
                            onValueChange = { anniv = it },
                            label = { Text("${S.anniversary.get(language)} (DD-MM)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotBlank() && phone.isNotBlank()) {
                            viewModel.addOrUpdateCustomer(
                                CustomerEntity(
                                    name = name.trim(),
                                    phone = phone.trim(),
                                    preferences = preferences,
                                    birthday = bday.ifBlank { null },
                                    anniversaryDate = anniv.ifBlank { null }
                                )
                            )
                            showAddDialog = false
                            Toast.makeText(context, S.success.get(language), Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DulhanMaroon)
                ) {
                    Text(S.save.get(language))
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text(S.cancel.get(language))
                }
            }
        )
    }

    // Delete Customer Confirmation
    if (customerToDelete != null) {
        val cust = customerToDelete!!
        AlertDialog(
            onDismissRequest = { customerToDelete = null },
            title = { Text(S.delete.get(language), fontWeight = FontWeight.Bold) },
            text = { Text("Delete customer record for '${cust.name}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteCustomer(cust)
                        customerToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(S.delete.get(language))
                }
            },
            dismissButton = {
                TextButton(onClick = { customerToDelete = null }) {
                    Text(S.cancel.get(language))
                }
            }
        )
    }
}

@Composable
private fun CustomerCard(
    customer: CustomerEntity,
    language: AppLanguage,
    onClick: () -> Unit,
    onSendGreeting: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            if (customer.membershipTier == "DULHAN_VIP") DulhanGold else DulhanBlush
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = customer.name.take(1).uppercase(),
                        fontWeight = FontWeight.Bold,
                        color = if (customer.membershipTier == "DULHAN_VIP") DulhanMaroon else DulhanPink,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = customer.name,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        if (customer.membershipTier == "DULHAN_VIP") {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = DulhanGoldPale
                            ) {
                                Text(
                                    text = "VIP",
                                    color = DulhanMaroon,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = customer.phone,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    if (customer.preferences.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = customer.preferences,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onSendGreeting, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.Cake,
                        contentDescription = "Send Greeting",
                        tint = DulhanPink,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

private fun sendGreetingWhatsApp(context: Context, customer: CustomerEntity, language: AppLanguage) {
    try {
        val cleanNumber = customer.phone.replace("+", "").replace(" ", "")
        val msg = if (language == AppLanguage.HINDI) {
            "नमस्ते ${customer.name} जी! 🌸\nदुल्हन ब्यूटी पार्लर की ओर से आपको जन्मदिन / सालगिरह की हार्दिक शुभकामनाएं!\nआपके इस खास दिन पर हम आपके लिए लाए हैं विशेष 20% की छूट का कूपन कोड: CELEBRATE20\nआज ही अपना अपॉइंटमेंट बुक करें। धन्यवाद!"
        } else {
            "Hello ${customer.name}! 🌸\nWarm greetings and best wishes from Dulhan Beauty Parlour on your special day!\nEnjoy an exclusive 20% discount on your next beauty session with coupon: CELEBRATE20\nBook your appointment today. Thank you!"
        }
        val encodedMsg = Uri.encode(msg)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$cleanNumber&text=$encodedMsg"))
        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(context, "Could not open WhatsApp", Toast.LENGTH_SHORT).show()
    }
}
