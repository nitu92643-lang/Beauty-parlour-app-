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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import com.example.data.model.AppointmentEntity
import com.example.data.model.BeautyServiceEntity
import com.example.ui.components.StatusBadge
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.S
import com.example.ui.theme.DulhanGold
import com.example.ui.theme.DulhanGoldPale
import com.example.ui.theme.DulhanMaroon
import com.example.ui.theme.DulhanPink
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.ParlourViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    viewModel: ParlourViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val allServices by viewModel.allServices.collectAsState()
    val allStaff by viewModel.allStaff.collectAsState()
    val allAppointments by viewModel.allAppointments.collectAsState()
    val preselectedService by viewModel.selectedServiceForBooking.collectAsState()
    val context = LocalContext.current

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // Form States
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var selectedService by remember(preselectedService, allServices) {
        mutableStateOf(preselectedService ?: allServices.firstOrNull())
    }
    var serviceExpanded by remember { mutableStateOf(false) }

    // Date Chips
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val todayDate = dateFormat.format(calendar.time)
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    val tomorrowDate = dateFormat.format(calendar.time)
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    val dayAfterDate = dateFormat.format(calendar.time)

    var selectedDate by remember { mutableStateOf(todayDate) }

    // Time Slots
    val timeSlots = listOf(
        "10:00 AM", "11:30 AM", "01:00 PM",
        "02:30 PM", "04:00 PM", "05:30 PM", "07:00 PM"
    )
    var selectedTimeSlot by remember { mutableStateOf(timeSlots[0]) }

    // Staff
    var selectedStaffName by remember { mutableStateOf(allStaff.firstOrNull()?.name ?: "Pooja Sharma") }
    var isBridalTrial by remember { mutableStateOf(false) }
    var weddingDate by remember { mutableStateOf("") }
    var specialNotes by remember { mutableStateOf("") }

    // Reschedule dialog state
    var appointmentToReschedule by remember { mutableStateOf<AppointmentEntity?>(null) }
    var appointmentToCancel by remember { mutableStateOf<AppointmentEntity?>(null) }

    Column(modifier = modifier.fillMaxSize().testTag("booking_screen")) {
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
                        text = S.newBookingTitle.get(language),
                        fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = {
                    Text(
                        text = "${S.myBookings.get(language)} (${allAppointments.size})",
                        fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }

        if (selectedTabIndex == 0) {
            // New Booking Form
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Customer Name
                item {
                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = { Text(S.customerName.get(language)) },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = DulhanPink)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("booking_name_input"),
                        singleLine = true
                    )
                }

                // Customer Phone
                item {
                    OutlinedTextField(
                        value = customerPhone,
                        onValueChange = { customerPhone = it },
                        label = { Text(S.customerPhone.get(language)) },
                        leadingIcon = {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = DulhanPink)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("booking_phone_input"),
                        singleLine = true
                    )
                }

                // Service Dropdown
                item {
                    ExposedDropdownMenuBox(
                        expanded = serviceExpanded,
                        onExpandedChange = { serviceExpanded = !serviceExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedService?.let {
                                if (language == AppLanguage.HINDI) it.nameHi else it.nameEn
                            } ?: S.selectService.get(language),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(S.selectService.get(language)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = serviceExpanded) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = serviceExpanded,
                            onDismissRequest = { serviceExpanded = false }
                        ) {
                            allServices.forEach { service ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(if (language == AppLanguage.HINDI) service.nameHi else service.nameEn)
                                            Text("₹${service.price.toInt()}", fontWeight = FontWeight.Bold, color = DulhanMaroon)
                                        }
                                    },
                                    onClick = {
                                        selectedService = service
                                        serviceExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Selected Service Price & Duration Summary Box
                if (selectedService != null) {
                    item {
                        val svc = selectedService!!
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = DulhanGoldPale,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${S.duration.get(language)}: ${svc.durationMinutes} ${S.min.get(language)}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = "${S.price.get(language)}: ₹${svc.price.toInt()}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DulhanMaroon
                                    )
                                )
                            }
                        }
                    }
                }

                // Date Selection Chips
                item {
                    Text(
                        text = S.selectDate.get(language),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val dateOptions = listOf(
                            todayDate to if (language == AppLanguage.HINDI) "आज (Today)" else "Today",
                            tomorrowDate to if (language == AppLanguage.HINDI) "कल (Tomorrow)" else "Tomorrow",
                            dayAfterDate to dayAfterDate
                        )
                        dateOptions.forEach { (dateVal, label) ->
                            val isSel = selectedDate == dateVal
                            FilterChip(
                                selected = isSel,
                                onClick = { selectedDate = dateVal },
                                label = { Text(label, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = DulhanMaroon,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Time Slot Selection Chips
                item {
                    Text(
                        text = S.selectTime.get(language),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        timeSlots.forEach { slot ->
                            val isSel = selectedTimeSlot == slot
                            FilterChip(
                                selected = isSel,
                                onClick = { selectedTimeSlot = slot },
                                label = { Text(slot, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = DulhanPink,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Preferred Beautician / Staff
                item {
                    Text(
                        text = S.selectStaff.get(language),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        allStaff.forEach { staff ->
                            val isSel = selectedStaffName == staff.name
                            FilterChip(
                                selected = isSel,
                                onClick = { selectedStaffName = staff.name },
                                label = { Text(staff.name, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = DulhanMaroon,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Bridal Trial & Wedding Date (Optional)
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = isBridalTrial,
                                    onCheckedChange = { isBridalTrial = it },
                                    colors = CheckboxDefaults.colors(checkedColor = DulhanPink)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = S.isBridalTrial.get(language),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }

                            if (isBridalTrial) {
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = weddingDate,
                                    onValueChange = { weddingDate = it },
                                    label = { Text(S.weddingDate.get(language)) },
                                    placeholder = { Text("YYYY-MM-DD") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                            }
                        }
                    }
                }

                // Special Notes
                item {
                    OutlinedTextField(
                        value = specialNotes,
                        onValueChange = { specialNotes = it },
                        label = { Text(S.specialNotes.get(language)) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )
                }

                // Submit Button
                item {
                    Button(
                        onClick = {
                            if (customerName.isBlank() || customerPhone.isBlank()) {
                                Toast.makeText(
                                    context,
                                    if (language == AppLanguage.HINDI) "कृपया नाम और मोबाइल नंबर दर्ज करें" else "Please enter name and mobile number",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }
                            val targetService = selectedService ?: allServices.firstOrNull()
                            if (targetService == null) {
                                Toast.makeText(context, "Please choose a service", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            viewModel.bookAppointment(
                                customerName = customerName,
                                customerPhone = customerPhone,
                                service = targetService,
                                date = selectedDate,
                                timeSlot = selectedTimeSlot,
                                beauticianName = selectedStaffName,
                                isBridalTrial = isBridalTrial,
                                weddingDate = weddingDate.ifBlank { null },
                                notes = specialNotes
                            )

                            Toast.makeText(
                                context,
                                if (language == AppLanguage.HINDI) "अपॉइंटमेंट सफलतापूर्वक बुक हुआ!" else "Appointment booked successfully!",
                                Toast.LENGTH_LONG
                            ).show()

                            // Reset and switch to history
                            customerName = ""
                            customerPhone = ""
                            specialNotes = ""
                            viewModel.clearBookingPreselection()
                            selectedTabIndex = 1
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DulhanMaroon),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("confirm_booking_button")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = S.confirmBooking.get(language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        } else {
            // Bookings History List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentPadding = PaddingValues(bottom = 70.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${allAppointments.size} ${if (language == AppLanguage.HINDI) "अपॉइंटमेंट्स" else "Appointments"}",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Button(
                            onClick = { viewModel.navigateTo(AppScreen.BOOK_APPOINTMENT) },
                            colors = ButtonDefaults.buttonColors(containerColor = DulhanMaroon),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI) "नया बुक करें" else "Book New",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (allAppointments.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (language == AppLanguage.HINDI) "कोई बुकिंग उपलब्ध नहीं है।" else "No bookings recorded yet.",
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }
                }

                items(allAppointments, key = { it.id }) { appointment ->
                    AppointmentCard(
                        appointment = appointment,
                        language = language,
                        onMarkCompleted = {
                            viewModel.updateAppointmentStatus(appointment.id, "COMPLETED")
                        },
                        onReschedule = {
                            appointmentToReschedule = appointment
                        },
                        onCancel = {
                            appointmentToCancel = appointment
                        }
                    )
                }
            }
        }
    }

    // Reschedule Dialog
    if (appointmentToReschedule != null) {
        val appt = appointmentToReschedule!!
        var newDate by remember { mutableStateOf(appt.date) }
        var newTime by remember { mutableStateOf(appt.timeSlot) }

        AlertDialog(
            onDismissRequest = { appointmentToReschedule = null },
            title = {
                Text(S.reschedule.get(language), fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("${appt.customerName} - ${appt.serviceNameEn}")
                    OutlinedTextField(
                        value = newDate,
                        onValueChange = { newDate = it },
                        label = { Text(S.selectDate.get(language)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newTime,
                        onValueChange = { newTime = it },
                        label = { Text(S.selectTime.get(language)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.rescheduleAppointment(appt, newDate, newTime)
                        appointmentToReschedule = null
                        Toast.makeText(context, S.success.get(language), Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DulhanMaroon)
                ) {
                    Text(S.save.get(language))
                }
            },
            dismissButton = {
                TextButton(onClick = { appointmentToReschedule = null }) {
                    Text(S.cancel.get(language))
                }
            }
        )
    }

    // Cancel Confirmation Dialog
    if (appointmentToCancel != null) {
        val appt = appointmentToCancel!!
        AlertDialog(
            onDismissRequest = { appointmentToCancel = null },
            title = {
                Text(S.cancel.get(language), fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    if (language == AppLanguage.HINDI)
                        "क्या आप वाकई '${appt.customerName}' का अपॉइंटमेंट रद्द करना चाहते हैं?"
                    else
                        "Cancel appointment for '${appt.customerName}'?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.cancelAppointment(appt.id)
                        appointmentToCancel = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(S.cancel.get(language))
                }
            },
            dismissButton = {
                TextButton(onClick = { appointmentToCancel = null }) {
                    Text(S.close.get(language))
                }
            }
        )
    }
}

@Composable
private fun AppointmentCard(
    appointment: AppointmentEntity,
    language: AppLanguage,
    onMarkCompleted: () -> Unit,
    onReschedule: () -> Unit,
    onCancel: () -> Unit
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
                Text(
                    text = appointment.customerName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                StatusBadge(status = appointment.status, language = language)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (language == AppLanguage.HINDI) appointment.serviceNameHi else appointment.serviceNameEn,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = DulhanMaroon
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                        tint = DulhanPink,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${appointment.date} • ${appointment.timeSlot}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Text(
                    text = "₹${appointment.price.toInt()}",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = DulhanGold,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${S.selectStaff.get(language)}: ${appointment.beauticianName}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            if (appointment.isBridalTrial) {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = DulhanGoldPale
                ) {
                    Text(
                        text = "★ " + (if (language == AppLanguage.HINDI) "ब्राइडल ट्रायल" else "Bridal Trial"),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = DulhanMaroon,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            if (appointment.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Note: ${appointment.notes}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            if (appointment.status != "COMPLETED" && appointment.status != "CANCELLED") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onCancel) {
                        Text(S.cancel.get(language), color = MaterialTheme.colorScheme.error)
                    }
                    TextButton(onClick = onReschedule) {
                        Text(S.reschedule.get(language), color = DulhanPink)
                    }
                    Button(
                        onClick = onMarkCompleted,
                        colors = ButtonDefaults.buttonColors(containerColor = DulhanMaroon),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "पूर्ण मार्क करें" else "Mark Done",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
