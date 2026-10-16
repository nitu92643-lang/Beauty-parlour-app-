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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.InventoryEntity
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.S
import com.example.ui.theme.DulhanMaroon
import com.example.ui.theme.DulhanPink
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusWarning
import com.example.ui.viewmodel.ParlourViewModel

@Composable
fun InventoryScreen(
    viewModel: ParlourViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val allInventory by viewModel.allInventory.collectAsState()
    val lowStockItems by viewModel.lowStockInventory.collectAsState()
    val context = LocalContext.current

    var showAddDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<InventoryEntity?>(null) }

    Box(modifier = modifier.fillMaxSize().testTag("inventory_screen")) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Low Stock Alert Banner if items are low
            if (lowStockItems.isNotEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = StatusWarning.copy(alpha = 0.15f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = StatusWarning)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "${lowStockItems.size} ${S.lowStockAlert.get(language)}",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = StatusWarning
                                    )
                                )
                                Text(
                                    text = if (language == AppLanguage.HINDI)
                                        "कृपया जल्द ही कॉस्मेटिक स्टॉक री-स्टॉक करें।"
                                    else
                                        "Please replenish cosmetics stock soon.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = S.inventoryTitle.get(language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${allInventory.size} Products",
                        style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }

            items(allInventory, key = { it.id }) { item ->
                InventoryItemCard(
                    item = item,
                    language = language,
                    onIncreaseStock = { viewModel.adjustStock(item.id, 1) },
                    onDecreaseStock = { viewModel.adjustStock(item.id, -1) },
                    onDelete = { itemToDelete = item }
                )
            }

            item {
                Spacer(modifier = Modifier.height(75.dp))
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = DulhanMaroon,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_product_fab")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Product")
        }
    }

    if (showAddDialog) {
        var nameEn by remember { mutableStateOf("") }
        var nameHi by remember { mutableStateOf("") }
        var brand by remember { mutableStateOf("") }
        var stockStr by remember { mutableStateOf("10") }
        var minStr by remember { mutableStateOf("3") }
        var expiry by remember { mutableStateOf("2027-12") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(S.addProduct.get(language), fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = nameHi,
                        onValueChange = { nameHi = it },
                        label = { Text("Product Name (Hindi)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = nameEn,
                        onValueChange = { nameEn = it },
                        label = { Text("Product Name (English)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = brand,
                        onValueChange = { brand = it },
                        label = { Text("Brand (e.g. MAC, L'Oréal, O3+)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = stockStr,
                            onValueChange = { stockStr = it },
                            label = { Text("Stock") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = minStr,
                            onValueChange = { minStr = it },
                            label = { Text("Alert Limit") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    OutlinedTextField(
                        value = expiry,
                        onValueChange = { expiry = it },
                        label = { Text("Expiry (YYYY-MM)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val stock = stockStr.toIntOrNull() ?: 5
                        val min = minStr.toIntOrNull() ?: 2
                        viewModel.addInventoryItem(
                            InventoryEntity(
                                productNameEn = nameEn.ifBlank { "Beauty Cosmetic" },
                                productNameHi = nameHi.ifBlank { "ब्यूटी प्रोडक्ट" },
                                brand = brand.ifBlank { "Salon Professional" },
                                category = "MAKEUP",
                                currentStock = stock,
                                minThreshold = min,
                                expiryDate = expiry
                            )
                        )
                        showAddDialog = false
                        Toast.makeText(context, S.success.get(language), Toast.LENGTH_SHORT).show()
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

    if (itemToDelete != null) {
        val target = itemToDelete!!
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = { Text(S.delete.get(language), fontWeight = FontWeight.Bold) },
            text = { Text("Delete '${target.productNameEn}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteInventory(target)
                        itemToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(S.delete.get(language))
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) {
                    Text(S.cancel.get(language))
                }
            }
        )
    }
}

@Composable
private fun InventoryItemCard(
    item: InventoryEntity,
    language: AppLanguage,
    onIncreaseStock: () -> Unit,
    onDecreaseStock: () -> Unit,
    onDelete: () -> Unit
) {
    val isLowStock = item.currentStock <= item.minThreshold

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
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.HINDI) item.productNameHi else item.productNameEn,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${item.brand} • ${item.category}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                if (isLowStock) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = StatusError.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "⚠ LOW",
                            color = StatusError,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${S.expiryDate.get(language)}: ${item.expiryDate}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    )
                    Text(
                        text = "${S.currentStock.get(language)}: ${item.currentStock} ${item.unit}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isLowStock) StatusError else DulhanMaroon
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDecreaseStock, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.RemoveCircle, contentDescription = "Decrease", tint = DulhanPink)
                    }
                    Text(
                        text = "${item.currentStock}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    IconButton(onClick = onIncreaseStock, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Increase", tint = DulhanMaroon)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(onClick = onDelete, modifier = Modifier.size(30.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}
