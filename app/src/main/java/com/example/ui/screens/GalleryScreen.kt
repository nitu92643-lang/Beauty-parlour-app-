package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Loyalty
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GalleryItemEntity
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.S
import com.example.ui.theme.DulhanBlush
import com.example.ui.theme.DulhanGold
import com.example.ui.theme.DulhanGoldBright
import com.example.ui.theme.DulhanGoldDark
import com.example.ui.theme.DulhanMaroon
import com.example.ui.theme.DulhanMaroonDeep
import com.example.ui.theme.DulhanPink
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.ParlourViewModel

@Composable
fun GalleryScreen(
    viewModel: ParlourViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val galleryItems by viewModel.galleryItems.collectAsState()
    val isAdminUnlocked by viewModel.isAdminUnlocked.collectAsState()
    val context = LocalContext.current

    var selectedCategory by remember { mutableStateOf("ALL") }
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredItems = galleryItems.filter {
        selectedCategory == "ALL" || it.category.equals(selectedCategory, ignoreCase = true)
    }

    Box(modifier = modifier.fillMaxSize().testTag("gallery_screen")) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Category Filter
            val categories = listOf(
                "ALL" to S.allCategories.get(language),
                "BRIDAL_MAKEUP" to S.catBridal.get(language),
                "HAIRSTYLES" to S.catHair.get(language),
                "MEHNDI" to S.catMehndi.get(language),
                "NAILS" to S.catNails.get(language)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { (catKey, catLabel) ->
                    val isSel = selectedCategory == catKey
                    FilterChip(
                        selected = isSel,
                        onClick = { selectedCategory = catKey },
                        label = { Text(catLabel, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DulhanMaroon,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            // Artistic Showcase Banner
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(DulhanMaroonDeep, DulhanPink, DulhanGoldDark)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = when (item.category.uppercase()) {
                                            "HAIRSTYLES" -> Icons.Default.ContentCut
                                            "MEHNDI" -> Icons.Default.AutoAwesome
                                            "NAILS" -> Icons.Default.Loyalty
                                            else -> Icons.Default.Spa
                                        },
                                        contentDescription = null,
                                        tint = DulhanGoldBright,
                                        modifier = Modifier.size(36.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (language == AppLanguage.HINDI) item.titleHi else item.titleEn,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }

                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.HINDI) item.descriptionHi else item.descriptionEn,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Button(
                                    onClick = {
                                        viewModel.navigateTo(AppScreen.BOOKING)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = DulhanMaroon),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.BookOnline, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (language == AppLanguage.HINDI) "यह लुक बुक करें" else "Book This Look",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
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

        if (isAdminUnlocked) {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = DulhanMaroon,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .testTag("add_gallery_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Look")
            }
        }
    }

    if (showAddDialog) {
        var titleEn by remember { mutableStateOf("") }
        var titleHi by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("BRIDAL_MAKEUP") }
        var desc by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(S.addPhoto.get(language), fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = titleHi,
                        onValueChange = { titleHi = it },
                        label = { Text("Look Title (Hindi)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = titleEn,
                        onValueChange = { titleEn = it },
                        label = { Text("Look Title (English)") },
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
                        if (titleHi.isNotBlank() || titleEn.isNotBlank()) {
                            viewModel.addGalleryItem(
                                GalleryItemEntity(
                                    titleEn = titleEn.ifBlank { "Bridal Style" },
                                    titleHi = titleHi.ifBlank { "ब्राइडल स्टाइल" },
                                    category = category,
                                    descriptionEn = desc,
                                    descriptionHi = desc
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
}
