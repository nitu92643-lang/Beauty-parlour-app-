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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StaffEntity
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.S
import com.example.ui.theme.DulhanGold
import com.example.ui.theme.DulhanGoldDark
import com.example.ui.theme.DulhanMaroon
import com.example.ui.theme.DulhanPink
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusWarning
import com.example.ui.viewmodel.ParlourViewModel

@Composable
fun StaffScreen(
    viewModel: ParlourViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val allStaff by viewModel.allStaff.collectAsState()
    val context = LocalContext.current

    var showAddStaffDialog by remember { mutableStateOf(false) }
    var staffToDelete by remember { mutableStateOf<StaffEntity?>(null) }

    Box(modifier = modifier.fillMaxSize().testTag("staff_screen")) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = S.staffMgmt.get(language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    val presentCount = allStaff.count { it.todayAttendance == "PRESENT" }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = StatusSuccess.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "$presentCount / ${allStaff.size} ${S.present.get(language)}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StatusSuccess
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            items(allStaff, key = { it.id }) { staff ->
                StaffCard(
                    staff = staff,
                    language = language,
                    onAttendanceChange = { newStatus ->
                        viewModel.updateStaffAttendance(staff.id, newStatus)
                    },
                    onDelete = {
                        staffToDelete = staff
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(75.dp))
            }
        }

        FloatingActionButton(
            onClick = { showAddStaffDialog = true },
            containerColor = DulhanMaroon,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_staff_fab")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Staff")
        }
    }

    // Add Staff Dialog
    if (showAddStaffDialog) {
        var name by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        var role by remember { mutableStateOf("BEAUTICIAN") }
        var specialty by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddStaffDialog = false },
            title = { Text(S.addStaff.get(language), fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = specialty,
                        onValueChange = { specialty = it },
                        label = { Text("Specialty (e.g. Bridal HD, Haircuts)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            viewModel.addStaff(
                                StaffEntity(
                                    name = name.trim(),
                                    phone = phone.trim(),
                                    role = role,
                                    specialty = specialty.ifBlank { "Beauty & Hair" }
                                )
                            )
                            showAddStaffDialog = false
                            Toast.makeText(context, S.success.get(language), Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DulhanMaroon)
                ) {
                    Text(S.save.get(language))
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddStaffDialog = false }) {
                    Text(S.cancel.get(language))
                }
            }
        )
    }

    if (staffToDelete != null) {
        val s = staffToDelete!!
        AlertDialog(
            onDismissRequest = { staffToDelete = null },
            title = { Text(S.delete.get(language), fontWeight = FontWeight.Bold) },
            text = { Text("Remove staff member '${s.name}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteStaff(s)
                        staffToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(S.delete.get(language))
                }
            },
            dismissButton = {
                TextButton(onClick = { staffToDelete = null }) {
                    Text(S.cancel.get(language))
                }
            }
        )
    }
}

@Composable
private fun StaffCard(
    staff: StaffEntity,
    language: AppLanguage,
    onAttendanceChange: (String) -> Unit,
    onDelete: () -> Unit
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(DulhanPink.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = staff.name.take(1).uppercase(),
                            fontWeight = FontWeight.Bold,
                            color = DulhanPink,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = staff.name,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = staff.specialty,
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = DulhanGoldDark,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${staff.rating}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Attendance Status selector
            Text(
                text = "${S.attendance.get(language)}:",
                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "PRESENT" to (if (language == AppLanguage.HINDI) "उपस्थित" else "Present"),
                    "ABSENT" to (if (language == AppLanguage.HINDI) "अनुपस्थित" else "Absent"),
                    "ON_LEAVE" to (if (language == AppLanguage.HINDI) "छुट्टी" else "Leave")
                ).forEach { (status, label) ->
                    val isSel = staff.todayAttendance == status
                    FilterChip(
                        selected = isSel,
                        onClick = { onAttendanceChange(status) },
                        label = { Text(label, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = when (status) {
                                "PRESENT" -> StatusSuccess
                                "ABSENT" -> StatusError
                                else -> StatusWarning
                            },
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }
    }
}
