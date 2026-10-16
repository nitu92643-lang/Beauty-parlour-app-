package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppointmentEntity
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
import com.example.ui.theme.StatusSuccess
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.ParlourViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookBeautyAppointmentScreen(
    viewModel: ParlourViewModel,
    onNavigateBack: () -> Unit = { viewModel.navigateBack() },
    onNavigateToBookings: () -> Unit = { viewModel.navigateTo(AppScreen.BOOKING) },
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val allServices by viewModel.allServices.collectAsState()
    val allStaff by viewModel.allStaff.collectAsState()
    val preselectedService by viewModel.selectedServiceForBooking.collectAsState()
    val context = LocalContext.current

    // Required Field 1: Client Name
    var clientName by remember { mutableStateOf("") }
    var clientNameError by remember { mutableStateOf<String?>(null) }

    // Client Mobile Number
    var clientPhone by remember { mutableStateOf("") }
    var clientPhoneError by remember { mutableStateOf<String?>(null) }

    // Required Field 2: Service Type
    var selectedService by remember(preselectedService, allServices) {
        mutableStateOf(preselectedService ?: allServices.firstOrNull())
    }
    var serviceDropdownExpanded by remember { mutableStateOf(false) }
    var selectedServiceCategory by remember { mutableStateOf("ALL") }

    // Required Field 3: Preferred Date
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val displayDateFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())

    val todayDateStr = dateFormat.format(calendar.time)
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    val tomorrowDateStr = dateFormat.format(calendar.time)
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    val dayAfterDateStr = dateFormat.format(calendar.time)

    var preferredDate by remember { mutableStateOf(todayDateStr) }
    var showDatePickerDialog by remember { mutableStateOf(false) }

    // Time Slot
    val timeSlots = listOf(
        "10:00 AM", "11:30 AM", "01:00 PM",
        "02:30 PM", "04:00 PM", "05:30 PM", "07:00 PM"
    )
    var preferredTimeSlot by remember { mutableStateOf(timeSlots[0]) }

    // Preferred Beautician
    var preferredBeautician by remember {
        mutableStateOf(allStaff.firstOrNull()?.name ?: "Pooja Sharma")
    }

    // Bridal trial & wedding date
    var isBridalTrial by remember { mutableStateOf(selectedService?.isBridalPackage == true) }
    var weddingDate by remember { mutableStateOf("") }
    var specialRequests by remember { mutableStateOf("") }

    // Confirmation Dialog state
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var bookedAppointmentSummary by remember { mutableStateOf<AppointmentEntity?>(null) }

    // M3 DatePicker State
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val chosen = dateFormat.format(Date(millis))
                            preferredDate = chosen
                        }
                        showDatePickerDialog = false
                    }
                ) {
                    Text(S.confirm.get(language), color = DulhanMaroon, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog = false }) {
                    Text(S.cancel.get(language))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("book_beauty_appointment_screen")
    ) {
        // Navigation Header Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("appointment_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DulhanMaroon
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "ब्यूटी अपॉइंटमेंट बुक करें" else "Book Beauty Appointment",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DulhanMaroon
                        )
                    )
                    Text(
                        text = if (language == AppLanguage.HINDI) "अपनी मनपसंद सेवा, समय व ब्यूटीशियन चुनें" else "Choose service type, preferred date & artist",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DulhanGoldPale,
                    modifier = Modifier
                        .clickable { onNavigateToBookings() }
                        .padding(end = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = DulhanMaroon,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = S.myBookings.get(language),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = DulhanMaroon,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // STEP 1: CLIENT DETAILS (Client Name & Phone)
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(DulhanPink),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("1", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI) "ग्राहक विवरण (Client Details)" else "Client Details",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Field 1: Client Name
                        OutlinedTextField(
                            value = clientName,
                            onValueChange = {
                                clientName = it
                                if (it.isNotBlank()) clientNameError = null
                            },
                            label = {
                                Text((if (language == AppLanguage.HINDI) "ग्राहक का नाम *" else "Client Name *"))
                            },
                            placeholder = {
                                Text(if (language == AppLanguage.HINDI) "उदा. प्रिया शर्मा" else "e.g. Priya Sharma")
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = DulhanPink)
                            },
                            isError = clientNameError != null,
                            supportingText = {
                                if (clientNameError != null) {
                                    Text(clientNameError!!, color = MaterialTheme.colorScheme.error)
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("appointment_client_name_input")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Client Phone
                        OutlinedTextField(
                            value = clientPhone,
                            onValueChange = {
                                clientPhone = it
                                if (it.isNotBlank()) clientPhoneError = null
                            },
                            label = {
                                Text(if (language == AppLanguage.HINDI) "मोबाइल नंबर *" else "Mobile Number *")
                            },
                            placeholder = { Text("9876543210") },
                            leadingIcon = {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = DulhanPink)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            isError = clientPhoneError != null,
                            supportingText = {
                                if (clientPhoneError != null) {
                                    Text(clientPhoneError!!, color = MaterialTheme.colorScheme.error)
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("appointment_client_phone_input")
                        )
                    }
                }
            }

            // STEP 2: SERVICE TYPE (Service Type Selection)
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(DulhanMaroon),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("2", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI) "ब्यूटी सेवा का प्रकार (Service Type) *" else "Select Beauty Service Type *",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Category Chips for quick service filtering
                        val serviceCategories = listOf(
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
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            serviceCategories.forEach { (catKey, catLabel) ->
                                val isSelected = selectedServiceCategory == catKey
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedServiceCategory = catKey },
                                    label = { Text(catLabel, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = DulhanMaroon,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Exposed Service Dropdown
                        ExposedDropdownMenuBox(
                            expanded = serviceDropdownExpanded,
                            onExpandedChange = { serviceDropdownExpanded = !serviceDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedService?.let {
                                    if (language == AppLanguage.HINDI) it.nameHi else it.nameEn
                                } ?: S.selectService.get(language),
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(if (language == AppLanguage.HINDI) "चुनी गई सेवा (Service Type)" else "Service Type") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = serviceDropdownExpanded)
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .testTag("service_type_dropdown")
                            )

                            val categoryServices = if (selectedServiceCategory == "ALL") {
                                allServices
                            } else {
                                allServices.filter { it.category.equals(selectedServiceCategory, ignoreCase = true) }
                            }

                            ExposedDropdownMenu(
                                expanded = serviceDropdownExpanded,
                                onDismissRequest = { serviceDropdownExpanded = false }
                            ) {
                                categoryServices.forEach { service ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = if (language == AppLanguage.HINDI) service.nameHi else service.nameEn,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                    Text(
                                                        text = "${service.durationMinutes} mins • ${service.category}",
                                                        style = MaterialTheme.typography.labelSmall.copy(
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    )
                                                }
                                                Text(
                                                    text = "₹${service.price.toInt()}",
                                                    fontWeight = FontWeight.Bold,
                                                    color = DulhanMaroon
                                                )
                                            }
                                        },
                                        onClick = {
                                            selectedService = service
                                            serviceDropdownExpanded = false
                                            if (service.isBridalPackage) isBridalTrial = true
                                        }
                                    )
                                }
                            }
                        }

                        // Selected Service Details Banner
                        if (selectedService != null) {
                            val svc = selectedService!!
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = DulhanGoldPale,
                                border = borderStroke(),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (language == AppLanguage.HINDI) svc.nameHi else svc.nameEn,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = DulhanMaroonDeep
                                            )
                                        )

                                        Text(
                                            text = "₹${svc.price.toInt()}",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                color = DulhanMaroon
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = if (language == AppLanguage.HINDI) svc.descriptionHi else svc.descriptionEn,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 12.sp
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = DulhanGoldDark,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${S.duration.get(language)}: ${svc.durationMinutes} ${S.min.get(language)}",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                color = DulhanMaroon
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // STEP 3: PREFERRED DATE & TIME (Preferred Date Selection)
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(DulhanGoldDark),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("3", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI) "पसंदीदा तारीख और समय (Preferred Date & Time) *" else "Preferred Date & Time *",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Preferred Date Display & Selection
                        Text(
                            text = S.selectDate.get(language),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val dateQuickOptions = listOf(
                                todayDateStr to (if (language == AppLanguage.HINDI) "आज" else "Today"),
                                tomorrowDateStr to (if (language == AppLanguage.HINDI) "कल" else "Tomorrow"),
                                dayAfterDateStr to (if (language == AppLanguage.HINDI) "परसों" else "Day After")
                            )

                            dateQuickOptions.forEach { (dateVal, label) ->
                                val isSelected = preferredDate == dateVal
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { preferredDate = dateVal },
                                    label = { Text(label, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = DulhanMaroon,
                                        selectedLabelColor = Color.White
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Custom Calendar Picker Action
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { showDatePickerDialog = true }
                                .testTag("pick_custom_date_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = null,
                                        tint = DulhanPink,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = if (language == AppLanguage.HINDI) "चुनी गई तारीख:" else "Selected Date:",
                                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        )
                                        Text(
                                            text = preferredDate,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = DulhanMaroon)
                                        )
                                    }
                                }

                                Text(
                                    text = if (language == AppLanguage.HINDI) "कैलेंडर बदलें" else "Change Date",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = DulhanPink)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Preferred Time Slot Chips
                        Text(
                            text = S.selectTime.get(language),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            timeSlots.forEach { slot ->
                                val isSelected = preferredTimeSlot == slot
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { preferredTimeSlot = slot },
                                    label = { Text(slot, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = DulhanPink,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // STEP 4: BEAUTICIAN & BRIDAL OPTIONS
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = S.selectStaff.get(language),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            allStaff.forEach { staff ->
                                val isSelected = preferredBeautician == staff.name
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { preferredBeautician = staff.name },
                                    label = {
                                        Text("${staff.name} (${staff.specialty.take(16)}...)", fontSize = 12.sp)
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = DulhanMaroon,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Bridal Trial Toggle
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = DulhanBlush.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
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
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Special Requests
                        OutlinedTextField(
                            value = specialRequests,
                            onValueChange = { specialRequests = it },
                            label = { Text(S.specialNotes.get(language)) },
                            placeholder = {
                                Text(if (language == AppLanguage.HINDI) "उदा. संवेदनशील त्वचा, विशिष्ट हेयर स्टाइलिंग" else "e.g. Sensitive skin, organic wax preference")
                            },
                            maxLines = 3,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // SUMMARY & CONFIRMATION ACTION
            item {
                val currentService = selectedService
                val totalPrice = currentService?.price ?: 0.0

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DulhanGoldPale),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (language == AppLanguage.HINDI) "अपॉइंटमेंट सारांश" else "Appointment Summary",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DulhanMaroonDeep
                                )
                            )

                            Text(
                                text = "₹${totalPrice.toInt()}",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DulhanMaroon
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "• ${S.customerName.get(language)}: ${clientName.ifBlank { if (language == AppLanguage.HINDI) "दर्ज नहीं किया गया" else "Not entered" }}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "• ${if (language == AppLanguage.HINDI) "सेवा का प्रकार" else "Service Type"}: ${currentService?.let { if (language == AppLanguage.HINDI) it.nameHi else it.nameEn } ?: "N/A"}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "• ${S.selectDate.get(language)}: $preferredDate ($preferredTimeSlot)",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "• ${S.selectStaff.get(language)}: $preferredBeautician",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                // Validation
                                if (clientName.isBlank()) {
                                    clientNameError = if (language == AppLanguage.HINDI) "कृपया ग्राहक का नाम दर्ज करें" else "Please enter client name"
                                    Toast.makeText(context, clientNameError, Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                if (clientPhone.isBlank()) {
                                    clientPhoneError = if (language == AppLanguage.HINDI) "कृपया मोबाइल नंबर दर्ज करें" else "Please enter mobile number"
                                    Toast.makeText(context, clientPhoneError, Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                val serviceToBook = selectedService
                                if (serviceToBook == null) {
                                    Toast.makeText(context, "Please choose a service type", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                // Book through ViewModel into Room Database
                                viewModel.bookAppointment(
                                    customerName = clientName,
                                    customerPhone = clientPhone,
                                    service = serviceToBook,
                                    date = preferredDate,
                                    timeSlot = preferredTimeSlot,
                                    beauticianName = preferredBeautician,
                                    isBridalTrial = isBridalTrial,
                                    weddingDate = weddingDate.ifBlank { null },
                                    notes = specialRequests
                                )

                                bookedAppointmentSummary = AppointmentEntity(
                                    customerName = clientName.trim(),
                                    customerPhone = clientPhone.trim(),
                                    serviceId = serviceToBook.id,
                                    serviceNameEn = serviceToBook.nameEn,
                                    serviceNameHi = serviceToBook.nameHi,
                                    date = preferredDate,
                                    timeSlot = preferredTimeSlot,
                                    beauticianName = preferredBeautician,
                                    price = serviceToBook.price,
                                    notes = specialRequests,
                                    isBridalTrial = isBridalTrial,
                                    weddingDate = weddingDate.ifBlank { null }
                                )
                                showConfirmationDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DulhanMaroon),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("submit_beauty_appointment_button")
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DulhanGoldBright)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI) "अपॉइंटमेंट कन्फर्म करें (Book Now)" else "Confirm Beauty Appointment",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }

    // Booking Success Confirmation Dialog
    if (showConfirmationDialog && bookedAppointmentSummary != null) {
        val appt = bookedAppointmentSummary!!
        AlertDialog(
            onDismissRequest = {
                showConfirmationDialog = false
                onNavigateToBookings()
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = StatusSuccess,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.HINDI) "अपॉइंटमेंट बुक हो गया!" else "Appointment Confirmed!",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DulhanGoldPale, RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = S.appName.get(language),
                        fontWeight = FontWeight.Bold,
                        color = DulhanMaroon
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("👤 ${appt.customerName} (${appt.customerPhone})", fontWeight = FontWeight.SemiBold)
                    Text("💄 ${if (language == AppLanguage.HINDI) appt.serviceNameHi else appt.serviceNameEn}", fontWeight = FontWeight.SemiBold, color = DulhanPink)
                    Text("📅 ${appt.date} • ${appt.timeSlot}")
                    Text("⭐ ${S.selectStaff.get(language)}: ${appt.beauticianName}")
                    Text("💰 ${S.totalAmount.get(language)}: ₹${appt.price.toInt()}", fontWeight = FontWeight.ExtraBold, color = DulhanMaroon)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmationDialog = false
                        viewModel.clearBookingPreselection()
                        onNavigateToBookings()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DulhanMaroon)
                ) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "बुकिंग इतिहास देखें" else "View All Bookings",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showConfirmationDialog = false
                        viewModel.clearBookingPreselection()
                        onNavigateBack()
                    }
                ) {
                    Text(if (language == AppLanguage.HINDI) "होम पर जाएं" else "Go to Home")
                }
            }
        )
    }
}

@Composable
private fun borderStroke() = androidx.compose.foundation.BorderStroke(1.dp, DulhanGold.copy(alpha = 0.4f))
