package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BeautyServiceEntity
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.S
import com.example.ui.theme.DulhanBlush
import com.example.ui.theme.DulhanGold
import com.example.ui.theme.DulhanMaroon
import com.example.ui.theme.DulhanPink
import com.example.ui.viewmodel.ParlourViewModel

@Composable
fun ServicesScreen(
    viewModel: ParlourViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val filteredServices by viewModel.filteredServices.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.serviceSearchQuery.collectAsState()
    val isAdminUnlocked by viewModel.isAdminUnlocked.collectAsState()

    var showAddEditDialog by remember { mutableStateOf(false) }
    var serviceToEdit by remember { mutableStateOf<BeautyServiceEntity?>(null) }
    var serviceToDelete by remember { mutableStateOf<BeautyServiceEntity?>(null) }

    Box(modifier = modifier.fillMaxSize().testTag("services_screen")) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setServiceSearchQuery(it) },
                placeholder = { Text(S.searchServices.get(language)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = DulhanPink
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setServiceSearchQuery("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("service_search_input"),
                singleLine = true
            )

            // Category Filter Chips
            val categories = listOf(
                "ALL" to S.allCategories.get(language),
                "BRIDAL" to S.catBridal.get(language),
                "MAKEUP" to S.catMakeup.get(language),
                "HAIR" to S.catHair.get(language),
                "SKIN" to S.catSkin.get(language),
                "SPA" to S.catSpa.get(language),
                "MEHNDI" to S.catMehndi.get(language),
                "NAILS" to S.catNails.get(language)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { (catKey, catLabel) ->
                    val isSelected = selectedCategory == catKey
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setCategoryFilter(catKey) },
                        label = {
                            Text(
                                text = catLabel,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DulhanMaroon,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // Total services count label
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredServices.size} ${if (language == AppLanguage.HINDI) "सेवाएं उपलब्ध" else "Services Available"}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                if (isAdminUnlocked) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "★ एडमिन मोड सक्रिय" else "★ Admin Mode Active",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = DulhanGold,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            // Services List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredServices, key = { it.id }) { service ->
                    ServiceItemCard(
                        service = service,
                        language = language,
                        isAdmin = isAdminUnlocked,
                        onBook = {
                            viewModel.selectServiceForBooking(service)
                        },
                        onEdit = {
                            serviceToEdit = service
                            showAddEditDialog = true
                        },
                        onDelete = {
                            serviceToDelete = service
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(70.dp))
                }
            }
        }

        // Floating Action Button for Admin to Add New Service
        if (isAdminUnlocked) {
            FloatingActionButton(
                onClick = {
                    serviceToEdit = null
                    showAddEditDialog = true
                },
                containerColor = DulhanPink,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .testTag("add_service_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Service"
                )
            }
        }
    }

    // Add / Edit Service Dialog
    if (showAddEditDialog) {
        AddEditServiceDialog(
            language = language,
            existing = serviceToEdit,
            onDismiss = {
                showAddEditDialog = false
                serviceToEdit = null
            },
            onSave = { updated ->
                if (serviceToEdit == null) {
                    viewModel.addService(updated)
                } else {
                    viewModel.updateService(updated)
                }
                showAddEditDialog = false
                serviceToEdit = null
            }
        )
    }

    // Delete Confirmation Dialog
    if (serviceToDelete != null) {
        val target = serviceToDelete!!
        AlertDialog(
            onDismissRequest = { serviceToDelete = null },
            title = {
                Text(
                    if (language == AppLanguage.HINDI) "सेवा हटाएं?" else "Delete Service?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    if (language == AppLanguage.HINDI)
                        "क्या आप वाकई '${target.nameHi}' को हटाना चाहते हैं?"
                    else
                        "Are you sure you want to delete '${target.nameEn}'?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteService(target)
                        serviceToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(S.delete.get(language))
                }
            },
            dismissButton = {
                TextButton(onClick = { serviceToDelete = null }) {
                    Text(S.cancel.get(language))
                }
            }
        )
    }
}

@Composable
private fun ServiceItemCard(
    service: BeautyServiceEntity,
    language: AppLanguage,
    isAdmin: Boolean,
    onBook: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
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
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "₹${service.price.toInt()}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DulhanMaroon
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "• ${service.durationMinutes} ${S.min.get(language)}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }

                if (service.isPopular) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = DulhanGold.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "★ " + (if (language == AppLanguage.HINDI) "लोकप्रिय" else "Popular"),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = DulhanMaroon
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (language == AppLanguage.HINDI) service.descriptionHi else service.descriptionEn,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isAdmin) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = DulhanMaroon,
                                modifier = Modifier.size(18.dp)
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
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Button(
                    onClick = onBook,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DulhanMaroon,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = S.bookNow.get(language),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
private fun AddEditServiceDialog(
    language: AppLanguage,
    existing: BeautyServiceEntity?,
    onDismiss: () -> Unit,
    onSave: (BeautyServiceEntity) -> Unit
) {
    var nameEn by remember { mutableStateOf(existing?.nameEn ?: "") }
    var nameHi by remember { mutableStateOf(existing?.nameHi ?: "") }
    var category by remember { mutableStateOf(existing?.category ?: "MAKEUP") }
    var priceStr by remember { mutableStateOf(existing?.price?.toInt()?.toString() ?: "1000") }
    var durationStr by remember { mutableStateOf(existing?.durationMinutes?.toString() ?: "45") }
    var descEn by remember { mutableStateOf(existing?.descriptionEn ?: "") }
    var descHi by remember { mutableStateOf(existing?.descriptionHi ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (existing == null)
                    (if (language == AppLanguage.HINDI) "नई सेवा जोड़ें" else "Add New Service")
                else
                    (if (language == AppLanguage.HINDI) "सेवा संपादित करें" else "Edit Service"),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nameHi,
                    onValueChange = { nameHi = it },
                    label = { Text("हिंदी नाम (Hindi Name)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = nameEn,
                    onValueChange = { nameEn = it },
                    label = { Text("English Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = priceStr,
                        onValueChange = { priceStr = it },
                        label = { Text("Price (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = durationStr,
                        onValueChange = { durationStr = it },
                        label = { Text("Mins") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = descHi,
                    onValueChange = { descHi = it },
                    label = { Text("विवरण (Hindi Description)") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val price = priceStr.toDoubleOrNull() ?: 500.0
                    val duration = durationStr.toIntOrNull() ?: 30
                    val updated = (existing ?: BeautyServiceEntity(
                        nameEn = nameEn.ifBlank { "Beauty Service" },
                        nameHi = nameHi.ifBlank { "ब्यूटी सेवा" },
                        category = category,
                        descriptionEn = descEn,
                        descriptionHi = descHi,
                        price = price,
                        durationMinutes = duration
                    )).copy(
                        nameEn = nameEn.ifBlank { existing?.nameEn ?: "Beauty Service" },
                        nameHi = nameHi.ifBlank { existing?.nameHi ?: "ब्यूटी सेवा" },
                        price = price,
                        durationMinutes = duration,
                        descriptionEn = descEn,
                        descriptionHi = descHi
                    )
                    onSave(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = DulhanPink)
            ) {
                Text(S.save.get(language))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(S.cancel.get(language))
            }
        }
    )
}
