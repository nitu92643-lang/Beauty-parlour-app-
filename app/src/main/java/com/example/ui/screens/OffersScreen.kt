package com.example.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.LocalOffer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OfferEntity
import com.example.ui.components.SectionHeader
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.S
import com.example.ui.theme.DulhanBlush
import com.example.ui.theme.DulhanGold
import com.example.ui.theme.DulhanGoldBright
import com.example.ui.theme.DulhanGoldDark
import com.example.ui.theme.DulhanGoldPale
import com.example.ui.theme.DulhanMaroon
import com.example.ui.theme.DulhanMaroonDeep
import com.example.ui.theme.DulhanPink
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.ParlourViewModel

@Composable
fun OffersScreen(
    viewModel: ParlourViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val activeOffers by viewModel.activeOffers.collectAsState()
    val isAdminUnlocked by viewModel.isAdminUnlocked.collectAsState()
    val context = LocalContext.current

    var showAddCouponDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize().testTag("offers_screen")) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            item {
                Text(
                    text = S.offersTitle.get(language),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = DulhanMaroon
                    )
                )
            }

            // Membership Tiers Section
            item {
                SectionHeader(title = S.membershipPlans.get(language))
                MembershipPlansCard(language = language)
            }

            // Active Discount Coupons
            item {
                Spacer(modifier = Modifier.height(4.dp))
                SectionHeader(title = S.specialOffers.get(language))
            }

            items(activeOffers, key = { it.id }) { offer ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = DulhanMaroon
                            ) {
                                Text(
                                    text = offer.code,
                                    color = DulhanGoldBright,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            Text(
                                text = if (offer.discountPercent > 0) "${offer.discountPercent}% OFF" else "₹${offer.flatDiscount.toInt()} OFF",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DulhanPink
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (language == AppLanguage.HINDI) offer.titleHi else offer.titleEn,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI) offer.descriptionHi else offer.descriptionEn,
                            style = MaterialTheme.typography.bodySmall.copy(
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
                                text = "${S.validTill.get(language)}: ${offer.validUntil}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )

                            Button(
                                onClick = {
                                    viewModel.navigateTo(AppScreen.BOOKING)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = DulhanPink),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(S.bookAppointment.get(language), fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(75.dp))
            }
        }

        if (isAdminUnlocked) {
            FloatingActionButton(
                onClick = { showAddCouponDialog = true },
                containerColor = DulhanMaroon,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .testTag("add_coupon_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Coupon")
            }
        }
    }

    if (showAddCouponDialog) {
        var code by remember { mutableStateOf("") }
        var title by remember { mutableStateOf("") }
        var desc by remember { mutableStateOf("") }
        var discountPercent by remember { mutableStateOf("15") }

        AlertDialog(
            onDismissRequest = { showAddCouponDialog = false },
            title = { Text(S.addCoupon.get(language), fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it.uppercase() },
                        label = { Text(S.couponCode.get(language)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Offer Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = discountPercent,
                        onValueChange = { discountPercent = it },
                        label = { Text("Discount %") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Description") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (code.isNotBlank()) {
                            viewModel.addOffer(
                                OfferEntity(
                                    code = code.trim(),
                                    titleEn = title.ifBlank { "Special Discount" },
                                    titleHi = title.ifBlank { "विशेष छूट" },
                                    descriptionEn = desc,
                                    descriptionHi = desc,
                                    discountPercent = discountPercent.toIntOrNull() ?: 10
                                )
                            )
                            showAddCouponDialog = false
                            Toast.makeText(context, S.success.get(language), Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DulhanMaroon)
                ) {
                    Text(S.save.get(language))
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCouponDialog = false }) {
                    Text(S.cancel.get(language))
                }
            }
        )
    }
}

@Composable
private fun MembershipPlansCard(language: AppLanguage) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DulhanGoldPale),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val tiers = if (language == AppLanguage.HINDI) listOf(
                "★ दुल्हन वीआईपी (Dulhan VIP)" to "₹15,000+ खर्च पर हर सर्विस पर 15% छूट + फ्री टच-अप किट + प्रायोरिटी बुकिंग",
                "★ गोल्डन दिवा (Golden Diva)" to "5+ विज़िट्स पर 10% छूट + कॉम्प्लिमेंट्री हेयर स्पा",
                "★ सिल्वर ब्राइड (Silver Bride)" to "3+ विज़िट्स पर 5% फ्लैट डिस्काउंट"
            ) else listOf(
                "★ Dulhan VIP Member" to "15% OFF on every visit + Free Bridal touch-up kit + Priority slots (₹15,000+ spend)",
                "★ Golden Diva Tier" to "10% OFF on all services + Complimentary hair spa on 5th visit",
                "★ Silver Bride Tier" to "5% flat discount on repeat visits (3+ bookings)"
            )

            tiers.forEachIndexed { idx, (tierName, benefits) ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = tierName,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DulhanMaroonDeep
                        )
                    )
                    Text(
                        text = benefits,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = DulhanMaroon,
                            fontSize = 12.sp
                        )
                    )
                }
                if (idx < tiers.size - 1) {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}
