package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BeautyServiceEntity
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

data class CustomBridalOption(
    val nameEn: String,
    val nameHi: String,
    val price: Double,
    val isMandatory: Boolean = false
)

@Composable
fun BridalScreen(
    viewModel: ParlourViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val bridalPackages by viewModel.bridalPackages.collectAsState()
    val context = LocalContext.current

    // Customizable bridal package builder items
    val customizableItems = remember {
        listOf(
            CustomBridalOption("Signature HD Foundation & Contour Base", "सिग्नेचर 4K एचडी मेकअप बेस", 7000.0, true),
            CustomBridalOption("International Bridal Floral Hairdo & Bun", "इंटरनेशनल फ्लोरल जुड़ा व हेयर स्टाइल", 2500.0, true),
            CustomBridalOption("Heavy Saree / Lehenga & Dupatta Setting", "लहंगा और डबल दुपट्टा पिनिंग", 800.0, true),
            CustomBridalOption("3D Feather Mink Eyelashes & Lens", "3D फेदर लैशेज व आई लेंस", 1200.0, false),
            CustomBridalOption("Pre-Bridal Gold Glow Facial", "प्री-ब्राइडल गोल्ड ग्लो फेशियल", 2000.0, false),
            CustomBridalOption("Rica White Chocolate Full Body Wax", "रिका वाइट चॉकलेट फुल बॉडी वैक्स", 2500.0, false),
            CustomBridalOption("Bridal Nail Extensions & 3D Art", "ब्राइडल नेल एक्सटेंशन व 3D आर्ट", 1800.0, false),
            CustomBridalOption("Bridal Rajasthani Mehndi Design", "शाही राजस्थानी दुल्हन मेहंदी", 3500.0, false)
        )
    }

    val selectedCustomItemIndices = remember {
        mutableStateListOf(0, 1, 2, 3) // Defaults
    }

    val customTotal = selectedCustomItemIndices.sumOf { customizableItems[it].price }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("bridal_screen"),
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Bridal Studio Header Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(DulhanMaroonDeep, DulhanMaroon)
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = S.bridalHeader.get(language),
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DulhanGoldBright
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = S.bridalSubtitle.get(language),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(DulhanGold.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Diamond,
                                contentDescription = null,
                                tint = DulhanGoldBright,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.navigateTo(AppScreen.BOOK_APPOINTMENT)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DulhanGold,
                            contentColor = DulhanMaroonDeep
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("book_trial_top_button")
                    ) {
                        Icon(imageVector = Icons.Default.Event, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = S.bookTrial.get(language),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Section: Curated Packages
        item {
            SectionHeader(
                title = if (language == AppLanguage.HINDI) "शाही ब्राइडल पैकेज" else "Curated Bridal Packages"
            )
        }

        items(bridalPackages) { pkg ->
            BridalPackageCard(
                pkg = pkg,
                language = language,
                onBook = {
                    viewModel.selectServiceForBooking(pkg)
                }
            )
        }

        // Section: Customizable Bridal Package Builder
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(
                title = S.customizePackage.get(language)
            )

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (language == AppLanguage.HINDI)
                            "अपनी पसंद के अनुसार ब्राइडल पैकेज बनाएं:"
                        else
                            "Select components to build your bespoke bridal package:",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = DulhanMaroon
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    customizableItems.forEachIndexed { index, item ->
                        val isChecked = selectedCustomItemIndices.contains(index)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    if (!item.isMandatory) {
                                        if (isChecked) {
                                            selectedCustomItemIndices.remove(index)
                                        } else {
                                            selectedCustomItemIndices.add(index)
                                        }
                                    }
                                }
                                .padding(vertical = 6.dp, horizontal = 4.dp),
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
                                        if (!item.isMandatory) {
                                            if (checked) selectedCustomItemIndices.add(index)
                                            else selectedCustomItemIndices.remove(index)
                                        }
                                    },
                                    enabled = !item.isMandatory,
                                    colors = CheckboxDefaults.colors(checkedColor = DulhanPink)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = if (language == AppLanguage.HINDI) item.nameHi else item.nameEn,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                                    )
                                    if (item.isMandatory) {
                                        Text(
                                            text = if (language == AppLanguage.HINDI) "(अनिवार्य बेस)" else "(Core Base)",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontSize = 10.sp
                                            )
                                        )
                                    }
                                }
                            }

                            Text(
                                text = "+₹${item.price.toInt()}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DulhanMaroon
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Custom Total
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "कस्टमाइज्ड कुल कीमत:" else "Customized Total Price:",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "₹${customTotal.toInt()}",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = DulhanPink
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val customService = BeautyServiceEntity(
                                nameEn = "Custom Bridal Bespoke Package",
                                nameHi = "कस्टमाइज्ड शाही ब्राइडल पैकेज",
                                category = "BRIDAL",
                                descriptionEn = "Custom selected bridal suite package",
                                descriptionHi = "ग्राहकानुसार कस्टमाइज्ड दुल्हन पैकेज",
                                price = customTotal,
                                durationMinutes = 240,
                                isBridalPackage = true
                            )
                            viewModel.selectServiceForBooking(customService)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DulhanMaroon),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("book_custom_bridal_button")
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "यह कस्टमाइज़्ड पैकेज बुक करें" else "Book This Custom Package",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BridalPackageCard(
    pkg: BeautyServiceEntity,
    language: AppLanguage,
    onBook: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.HINDI) pkg.nameHi else pkg.nameEn,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DulhanMaroonDeep
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "⏱ ${pkg.durationMinutes / 60} ${if (language == AppLanguage.HINDI) "घंटे" else "hours"} session",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DulhanGoldPale
                ) {
                    Text(
                        text = "₹${pkg.price.toInt()}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = DulhanMaroon
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (language == AppLanguage.HINDI) pkg.descriptionHi else pkg.descriptionEn,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            )

            if (pkg.includesSummaryEn.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = DulhanBlush
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = S.packageIncludes.get(language),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = DulhanMaroon
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI) pkg.includesSummaryHi else pkg.includesSummaryEn,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                color = DulhanMaroonDeep
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onBook,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DulhanMaroon,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
            ) {
                Icon(imageVector = Icons.Default.BookOnline, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = S.bookNow.get(language),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
