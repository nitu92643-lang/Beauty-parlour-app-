package com.example.ui.screens

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BeautyServiceEntity
import com.example.data.model.BillEntity
import com.example.ui.components.StatCard
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.S
import com.example.ui.theme.DulhanGold
import com.example.ui.theme.DulhanGoldDark
import com.example.ui.theme.DulhanGoldPale
import com.example.ui.theme.DulhanMaroon
import com.example.ui.theme.DulhanPink
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusWarning
import com.example.ui.viewmodel.ParlourViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BillingScreen(
    viewModel: ParlourViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val allServices by viewModel.allServices.collectAsState()
    val allBills by viewModel.allBills.collectAsState()
    val context = LocalContext.current

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // New Bill State
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    val selectedServiceCart = remember { mutableStateListOf<BeautyServiceEntity>() }
    var discountInput by remember { mutableStateOf("0") }
    var paymentMode by remember { mutableStateOf("UPI") } // CASH, UPI, CARD
    var paymentStatus by remember { mutableStateOf("PAID") } // PAID, PENDING
    var notes by remember { mutableStateOf("") }

    var billForReceiptModal by remember { mutableStateOf<BillEntity?>(null) }
    var showServicePickerModal by remember { mutableStateOf(false) }

    // Revenue calculations
    val todayDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val todayBills = allBills.filter { it.date == todayDateStr }
    val todaySales = todayBills.filter { it.paymentStatus == "PAID" }.sumOf { it.totalAmount }
    val totalRevenue = allBills.filter { it.paymentStatus == "PAID" }.sumOf { it.totalAmount }
    val pendingAmount = allBills.filter { it.paymentStatus == "PENDING" }.sumOf { it.totalAmount }

    val subtotal = selectedServiceCart.sumOf { it.price }
    val discountVal = discountInput.toDoubleOrNull() ?: 0.0
    val total = (subtotal - discountVal).coerceAtLeast(0.0)

    Column(modifier = modifier.fillMaxSize().testTag("billing_screen")) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = DulhanMaroon
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = {
                    Text(
                        text = S.newBill.get(language),
                        fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = {
                    Text(
                        text = "${if (language == AppLanguage.HINDI) "बिक्री इतिहास" else "Sales History"} (${allBills.size})",
                        fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }

        if (selectedTabIndex == 0) {
            // New Bill Creation Form
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Customer Details
                item {
                    Text(
                        text = S.customerMgmt.get(language),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = { Text(S.customerName.get(language)) },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = DulhanPink) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("bill_customer_name"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = customerPhone,
                        onValueChange = { customerPhone = it },
                        label = { Text(S.customerPhone.get(language)) },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = DulhanPink) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("bill_customer_phone"),
                        singleLine = true
                    )
                }

                // Add Services to Bill
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = S.selectServicesForBill.get(language),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        OutlinedButton(
                            onClick = { showServicePickerModal = true },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (language == AppLanguage.HINDI) "सेवा जोड़ें" else "Add Service", fontSize = 12.sp)
                        }
                    }

                    if (selectedServiceCart.isEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth().padding(top = 6.dp)
                        ) {
                            Text(
                                text = if (language == AppLanguage.HINDI) "कोई सेवा नहीं चुनी गई। ऊपर 'सेवा जोड़ें' पर टैप करें।" else "No services added. Tap 'Add Service' above.",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier.padding(top = 6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            selectedServiceCart.forEachIndexed { idx, svc ->
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    shadowElevation = 1.dp,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (language == AppLanguage.HINDI) svc.nameHi else svc.nameEn,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = "₹${svc.price.toInt()}",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = DulhanMaroon
                                            )
                                        )
                                        IconButton(
                                            onClick = { selectedServiceCart.removeAt(idx) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(Icons.Default.Clear, contentDescription = "Remove", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Discount Input
                item {
                    OutlinedTextField(
                        value = discountInput,
                        onValueChange = { discountInput = it },
                        label = { Text("${S.discount.get(language)} (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                // Payment Mode Chips
                item {
                    Text(
                        text = S.paymentMode.get(language),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("UPI" to S.upi.get(language), "CASH" to S.cash.get(language), "CARD" to S.card.get(language)).forEach { (mode, label) ->
                            val isSel = paymentMode == mode
                            FilterChip(
                                selected = isSel,
                                onClick = { paymentMode = mode },
                                label = { Text(label, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = DulhanMaroon,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Payment Status Chips
                item {
                    Text(
                        text = S.paymentStatus.get(language),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("PAID" to S.paid.get(language), "PENDING" to S.pending.get(language)).forEach { (status, label) ->
                            val isSel = paymentStatus == status
                            FilterChip(
                                selected = isSel,
                                onClick = { paymentStatus = status },
                                label = { Text(label, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = if (status == "PAID") StatusSuccess else StatusWarning,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Bill Calculation Breakdown Card
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = DulhanGoldPale),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(S.subtotal.get(language))
                                Text("₹${subtotal.toInt()}")
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(S.discount.get(language), color = MaterialTheme.colorScheme.error)
                                Text("-₹${discountVal.toInt()}", color = MaterialTheme.colorScheme.error)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Divider(color = DulhanGoldDark.copy(alpha = 0.3f))
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = S.totalAmount.get(language),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "₹${total.toInt()}",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DulhanMaroon
                                    )
                                )
                            }
                        }
                    }
                }

                // Generate & Save Bill Button
                item {
                    Button(
                        onClick = {
                            if (customerName.isBlank() || customerPhone.isBlank()) {
                                Toast.makeText(
                                    context,
                                    if (language == AppLanguage.HINDI) "कृपया नाम और मोबाइल नंबर दर्ज करें" else "Please enter name and phone number",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }
                            if (selectedServiceCart.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    if (language == AppLanguage.HINDI) "कृपया कम से कम एक सेवा जोड़ें" else "Please add at least one service",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            val summary = selectedServiceCart.joinToString(", ") {
                                "${it.nameEn} (₹${it.price.toInt()})"
                            }

                            val newBill = viewModel.createBill(
                                customerName = customerName,
                                customerPhone = customerPhone,
                                itemsSummary = summary,
                                subtotal = subtotal,
                                discount = discountVal,
                                paymentMode = paymentMode,
                                paymentStatus = paymentStatus,
                                notes = notes
                            )

                            // Show shareable receipt modal
                            billForReceiptModal = newBill

                            // Reset form
                            customerName = ""
                            customerPhone = ""
                            selectedServiceCart.clear()
                            discountInput = "0"
                            notes = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DulhanMaroon),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("generate_bill_button")
                    ) {
                        Icon(Icons.Default.Receipt, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = S.generateBill.get(language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(70.dp))
                }
            }
        } else {
            // Sales History & Receipts
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 14.dp, horizontal = 0.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Revenue Stat Cards
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            title = S.todaySales.get(language),
                            value = "₹${todaySales.toInt()}",
                            icon = Icons.Default.Payments,
                            iconTint = StatusSuccess,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = S.monthlySales.get(language),
                            value = "₹${totalRevenue.toInt()}",
                            icon = Icons.Default.Receipt,
                            iconTint = DulhanMaroon,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                if (pendingAmount > 0) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = StatusWarning.copy(alpha = 0.15f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (language == AppLanguage.HINDI) "कुल बकाया राशि (Pending):" else "Total Pending Dues:",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = "₹${pendingAmount.toInt()}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = StatusWarning
                                    )
                                )
                            }
                        }
                    }
                }

                // Invoices List
                items(allBills, key = { it.id }) { bill ->
                    BillHistoryCard(
                        bill = bill,
                        language = language,
                        onShare = {
                            shareReceipt(context, bill)
                        },
                        onTogglePaid = {
                            val newStatus = if (bill.paymentStatus == "PAID") "PENDING" else "PAID"
                            viewModel.updateBillStatus(bill.id, newStatus)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(70.dp))
                }
            }
        }
    }

    // Modal to pick services for the bill
    if (showServicePickerModal) {
        AlertDialog(
            onDismissRequest = { showServicePickerModal = false },
            title = { Text(S.selectServicesForBill.get(language), fontWeight = FontWeight.Bold) },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(allServices) { svc ->
                        val isChecked = selectedServiceCart.any { it.id == svc.id }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isChecked) DulhanGoldPale else MaterialTheme.colorScheme.surface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (isChecked) {
                                        selectedServiceCart.removeAll { it.id == svc.id }
                                    } else {
                                        selectedServiceCart.add(svc)
                                    }
                                }
                                .padding(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = { checked ->
                                            if (checked) selectedServiceCart.add(svc)
                                            else selectedServiceCart.removeAll { it.id == svc.id }
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = DulhanPink)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (language == AppLanguage.HINDI) svc.nameHi else svc.nameEn,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Text(
                                    text = "₹${svc.price.toInt()}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DulhanMaroon
                                    )
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showServicePickerModal = false },
                    colors = ButtonDefaults.buttonColors(containerColor = DulhanMaroon)
                ) {
                    Text(S.confirm.get(language))
                }
            }
        )
    }

    // Receipt Generated Modal
    if (billForReceiptModal != null) {
        val bill = billForReceiptModal!!
        AlertDialog(
            onDismissRequest = { billForReceiptModal = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusSuccess)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (language == AppLanguage.HINDI) "बिल सफलतापूर्वक जनरेट हुआ!" else "Bill Generated Successfully!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DulhanGoldPale, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Text("दुल्हन ब्यूटी पार्लर", fontWeight = FontWeight.Bold, color = DulhanMaroon)
                    Text("रसीद क्र./Bill No: ${bill.billNumber}", fontSize = 12.sp)
                    Text("दिनांक/Date: ${bill.date}", fontSize = 12.sp)
                    Divider(modifier = Modifier.padding(vertical = 6.dp))
                    Text("ग्राहक/Client: ${bill.customerName} (${bill.customerPhone})", fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("सेवाएं/Items: ${bill.itemsSummary}", fontSize = 12.sp)
                    Divider(modifier = Modifier.padding(vertical = 6.dp))
                    Text("कुल भुगतान/Total: ₹${bill.totalAmount.toInt()}", fontWeight = FontWeight.ExtraBold, color = DulhanMaroon, fontSize = 18.sp)
                    Text("माध्यम/Mode: ${bill.paymentMode} • ${bill.paymentStatus}", fontSize = 12.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        shareReceipt(context, bill)
                        billForReceiptModal = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(S.shareReceipt.get(language), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { billForReceiptModal = null }) {
                    Text(S.close.get(language))
                }
            }
        )
    }
}

@Composable
private fun BillHistoryCard(
    bill: BillEntity,
    language: AppLanguage,
    onShare: () -> Unit,
    onTogglePaid: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = bill.billNumber,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${bill.customerName} • ${bill.customerPhone}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (bill.paymentStatus == "PAID") StatusSuccess.copy(alpha = 0.15f) else StatusWarning.copy(alpha = 0.15f),
                    modifier = Modifier.clickable { onTogglePaid() }
                ) {
                    Text(
                        text = if (bill.paymentStatus == "PAID") "✓ PAID" else "⚠ PENDING",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (bill.paymentStatus == "PAID") StatusSuccess else StatusWarning
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = bill.itemsSummary,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${bill.date} • ${bill.paymentMode}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "₹${bill.totalAmount.toInt()}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = DulhanMaroon
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = DulhanPink,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun shareReceipt(context: Context, bill: BillEntity) {
    try {
        val receiptText = """
🌸 *दुल्हन ब्यूटी पार्लर | DULHAN BEAUTY PARLOUR* 🌸
━━━━━━━━━━━━━━━━━━━━
📄 *बिल क्र. / Invoice:* ${bill.billNumber}
📅 *दिनांक / Date:* ${bill.date}
👤 *ग्राहक / Client:* ${bill.customerName} (${bill.customerPhone})
━━━━━━━━━━━━━━━━━━━━
💄 *सेवाएं / Services:*
${bill.itemsSummary}

💰 *उप-योग / Subtotal:* ₹${bill.subtotal.toInt()}
🎁 *छूट / Discount:* ₹${bill.discount.toInt()}
💵 *कुल देय / Total:* ₹${bill.totalAmount.toInt()}
💳 *माध्यम / Mode:* ${bill.paymentMode} (${bill.paymentStatus})
━━━━━━━━━━━━━━━━━━━━
धन्यवाद! पुनः पधारें।
Contact: +91 98765 43210
        """.trimIndent()

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, receiptText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Receipt")
        context.startActivity(shareIntent)
    } catch (_: Exception) {}
}
