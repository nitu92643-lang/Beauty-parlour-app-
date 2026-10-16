package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BeautyServiceEntity
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
import com.example.ui.theme.DulhanPinkLight
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.ParlourViewModel

@Composable
fun HomeScreen(
    viewModel: ParlourViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val allServices by viewModel.allServices.collectAsState()
    val bridalPackages by viewModel.bridalPackages.collectAsState()
    val activeOffers by viewModel.activeOffers.collectAsState()
    val config by viewModel.parlourConfig.collectAsState()
    val context = LocalContext.current

    val popularServices = allServices.filter { it.isPopular }.take(6)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. Hero Welcome Banner
        item {
            HeroBanner(
                language = language,
                onBookClick = { viewModel.navigateTo(AppScreen.BOOK_APPOINTMENT) },
                onBridalClick = { viewModel.navigateTo(AppScreen.BRIDAL) }
            )
        }

        // 2. Quick Action Grid
        item {
            QuickActionsGrid(
                language = language,
                onNavigate = { viewModel.navigateTo(it) },
                onCall = { openDialer(context, config?.phone ?: S.defaultPhone) },
                onWhatsApp = { openWhatsApp(context, config?.whatsapp ?: S.defaultWhatsApp) }
            )
        }

        // 3. Special Offers & Festival Discounts
        if (activeOffers.isNotEmpty()) {
            item {
                SectionHeader(
                    title = S.specialOffers.get(language),
                    actionText = S.tabMore.get(language),
                    onActionClick = { viewModel.navigateTo(AppScreen.OFFERS) }
                )
                OffersCarousel(
                    offers = activeOffers,
                    language = language,
                    onOfferClick = { viewModel.navigateTo(AppScreen.BOOKING) }
                )
            }
        }

        // 4. Popular Bridal Makeup Packages
        item {
            Spacer(modifier = Modifier.height(12.dp))
            SectionHeader(
                title = S.popularBridal.get(language),
                actionText = S.bridalPackages.get(language),
                onActionClick = { viewModel.navigateTo(AppScreen.BRIDAL) }
            )
            BridalPackagesShowcase(
                packages = bridalPackages.take(3),
                language = language,
                onBookPackage = { service ->
                    viewModel.selectServiceForBooking(service)
                }
            )
        }

        // 5. Featured Beauty Services
        item {
            Spacer(modifier = Modifier.height(12.dp))
            SectionHeader(
                title = S.featuredServices.get(language),
                actionText = S.viewServices.get(language),
                onActionClick = { viewModel.navigateTo(AppScreen.SERVICES) }
            )
        }

        items(popularServices) { service ->
            FeaturedServiceItem(
                service = service,
                language = language,
                onBook = {
                    viewModel.selectServiceForBooking(service)
                }
            )
        }

        // 6. Why Choose Us Highlights
        item {
            Spacer(modifier = Modifier.height(16.dp))
            WhyChooseUsCard(language = language)
        }

        // 7. Contact & Opening Hours summary
        item {
            Spacer(modifier = Modifier.height(12.dp))
            ParlourInfoSummaryCard(
                language = language,
                phone = config?.phone ?: S.defaultPhone,
                address = if (language == AppLanguage.HINDI) config?.addressHi ?: S.defaultAddress.hi else config?.addressEn ?: S.defaultAddress.en,
                hours = if (language == AppLanguage.HINDI) config?.openingHoursHi ?: S.defaultHours.hi else config?.openingHoursEn ?: S.defaultHours.en,
                onViewContact = { viewModel.navigateTo(AppScreen.CONTACT) }
            )
        }
    }
}

@Composable
private fun HeroBanner(
    language: AppLanguage,
    onBookClick: () -> Unit,
    onBridalClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            DulhanMaroonDeep,
                            DulhanMaroon,
                            Color(0xFF6B0E3C)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = DulhanGold.copy(alpha = 0.25f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Diamond,
                                    contentDescription = null,
                                    tint = DulhanGoldBright,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (language == AppLanguage.HINDI) "रॉयल ब्राइडल स्टूडियो" else "Royal Bridal Studio",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = DulhanGoldBright,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (language == AppLanguage.HINDI) "दुल्हन ब्यूटी पार्लर" else "Dulhan Beauty Parlour",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI)
                                "आपकी शादी और हर खास मौके का सर्वोत्तम ब्राइडल श्रृंगार और लग्जरी सैलून सेवाएं"
                            else
                                "Exquisite bridal makeovers, luxury hair styling & skin radiance for your special day",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.9f),
                                lineHeight = 18.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Decorative Bridal Emblem
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(DulhanGold.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Spa,
                            contentDescription = null,
                            tint = DulhanGoldBright,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onBookClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DulhanGold,
                            contentColor = DulhanMaroonDeep
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("hero_book_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookOnline,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = S.bookAppointment.get(language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    OutlinedButton(
                        onClick = onBridalClick,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.horizontalGradient(listOf(DulhanGold, Color.White))
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("hero_bridal_button")
                    ) {
                        Text(
                            text = S.bridalPackages.get(language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionsGrid(
    language: AppLanguage,
    onNavigate: (AppScreen) -> Unit,
    onCall: () -> Unit,
    onWhatsApp: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionItem(
                title = S.bookAppointment.get(language),
                icon = Icons.Default.BookOnline,
                color = DulhanPink,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(AppScreen.BOOK_APPOINTMENT) },
                testTag = "quick_book"
            )
            QuickActionItem(
                title = S.bridalPackages.get(language),
                icon = Icons.Default.Diamond,
                color = DulhanMaroon,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(AppScreen.BRIDAL) },
                testTag = "quick_bridal"
            )
            QuickActionItem(
                title = S.servicesTitle.get(language),
                icon = Icons.Default.Spa,
                color = DulhanGoldDark,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(AppScreen.SERVICES) },
                testTag = "quick_services"
            )
            QuickActionItem(
                title = S.whatsAppUs.get(language),
                icon = Icons.Default.Chat,
                color = Color(0xFF25D366),
                modifier = Modifier.weight(1f),
                onClick = onWhatsApp,
                testTag = "quick_whatsapp"
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionItem(
                title = S.billingTitle.get(language),
                icon = Icons.Default.Receipt,
                color = Color(0xFF00796B),
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(AppScreen.BILLING) },
                testTag = "quick_billing"
            )
            QuickActionItem(
                title = S.customerMgmt.get(language),
                icon = Icons.Default.People,
                color = Color(0xFF5E35B1),
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(AppScreen.CUSTOMERS) },
                testTag = "quick_customers"
            )
            QuickActionItem(
                title = S.galleryTitle.get(language),
                icon = Icons.Default.PhotoLibrary,
                color = Color(0xFFD81B60),
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(AppScreen.GALLERY) },
                testTag = "quick_gallery"
            )
            QuickActionItem(
                title = S.callUs.get(language),
                icon = Icons.Default.Call,
                color = DulhanMaroon,
                modifier = Modifier.weight(1f),
                onClick = onCall,
                testTag = "quick_call"
            )
        }
    }
}

@Composable
private fun QuickActionItem(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    testTag: String
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun OffersCarousel(
    offers: List<OfferEntity>,
    language: AppLanguage,
    onOfferClick: () -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(offers) { offer ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DulhanBlush
                ),
                modifier = Modifier
                    .width(260.dp)
                    .clickable { onOfferClick() }
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
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
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = if (offer.discountPercent > 0) "${offer.discountPercent}% OFF" else "₹${offer.flatDiscount.toInt()} OFF",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = DulhanPink
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (language == AppLanguage.HINDI) offer.titleHi else offer.titleEn,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DulhanMaroonDeep
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (language == AppLanguage.HINDI) offer.descriptionHi else offer.descriptionEn,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun BridalPackagesShowcase(
    packages: List<BeautyServiceEntity>,
    language: AppLanguage,
    onBookPackage: (BeautyServiceEntity) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(packages) { pkg ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.width(280.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(DulhanGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Diamond,
                                contentDescription = null,
                                tint = DulhanGoldDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Text(
                            text = "₹${pkg.price.toInt()}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = DulhanMaroon
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (language == AppLanguage.HINDI) pkg.nameHi else pkg.nameEn,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (language == AppLanguage.HINDI) pkg.descriptionHi else pkg.descriptionEn,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onBookPackage(pkg) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DulhanMaroon,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = S.bookNow.get(language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FeaturedServiceItem(
    service: BeautyServiceEntity,
    language: AppLanguage,
    onBook: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(DulhanBlush),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (service.category.uppercase()) {
                            "HAIR" -> Icons.Default.ContentCut
                            "SPA" -> Icons.Default.Spa
                            "MEHNDI" -> Icons.Default.AutoAwesome
                            "NAILS" -> Icons.Default.Loyalty
                            else -> Icons.Default.Face
                        },
                        contentDescription = null,
                        tint = DulhanPink,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = if (language == AppLanguage.HINDI) service.nameHi else service.nameEn,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "₹${service.price.toInt()} • ${service.durationMinutes} ${S.min.get(language)}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = DulhanMaroon,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Button(
                onClick = onBook,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DulhanPink,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = S.bookNow.get(language),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun WhyChooseUsCard(language: AppLanguage) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = DulhanGoldPale
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = S.whyChooseUs.get(language),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DulhanMaroonDeep
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            val points = if (language == AppLanguage.HINDI) listOf(
                "100% इंटरनेशनल ओरिजिनल ब्रांड्स (MAC, Huda, Forever52)",
                "पूर्णतः स्वच्छ व सैनिटाइज्ड टूल्स व किट्स",
                "10+ वर्षों के अनुभवी सर्टिफाइड ब्राइडल मेकअप आर्टिस्ट",
                "फ्री ब्राइडल कंसल्टेशन और स्किन एनालिसिस"
            ) else listOf(
                "100% International authentic brands (MAC, Huda, Forever52)",
                "Strictly sterilized & single-use disposable tools",
                "10+ years experienced certified bridal artists",
                "Complimentary bridal consultation & skin analysis"
            )

            points.forEach { point ->
                Row(
                    modifier = Modifier.padding(vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = DulhanGoldDark,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = point,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = DulhanMaroonDeep,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ParlourInfoSummaryCard(
    language: AppLanguage,
    phone: String,
    address: String,
    hours: String,
    onViewContact: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onViewContact() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = S.contactTitle.get(language),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = S.tabMore.get(language),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = DulhanPink,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = DulhanPink,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = address,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = null,
                    tint = DulhanMaroon,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$phone • $hours",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
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
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$cleanNumber&text=Hello%20Dulhan%20Beauty%20Parlour"))
        context.startActivity(intent)
    } catch (_: Exception) {}
}
