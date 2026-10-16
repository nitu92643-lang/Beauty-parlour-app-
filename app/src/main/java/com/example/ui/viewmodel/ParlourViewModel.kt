package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ParlourDatabase
import com.example.data.model.AppointmentEntity
import com.example.data.model.BeautyServiceEntity
import com.example.data.model.BillEntity
import com.example.data.model.CustomerEntity
import com.example.data.model.GalleryItemEntity
import com.example.data.model.InventoryEntity
import com.example.data.model.OfferEntity
import com.example.data.model.ParlourConfigEntity
import com.example.data.model.StaffEntity
import com.example.data.repository.ParlourRepository
import com.example.ui.localization.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AppScreen {
    HOME,
    SERVICES,
    BRIDAL,
    BOOKING,
    BOOK_APPOINTMENT,
    BILLING,
    CUSTOMERS,
    STAFF,
    INVENTORY,
    OFFERS,
    GALLERY,
    ADMIN,
    CONTACT
}

class ParlourViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ParlourRepository

    init {
        val dao = ParlourDatabase.getDatabase(application).parlourDao()
        repository = ParlourRepository(dao)
        viewModelScope.launch {
            repository.ensureSeeded()
        }
    }

    // Language state (Default is Hindi as requested)
    private val _currentLanguage = MutableStateFlow(AppLanguage.HINDI)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == AppLanguage.HINDI) {
            AppLanguage.ENGLISH
        } else {
            AppLanguage.HINDI
        }
    }

    // Navigation state
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Screen back stack
    private val backStack = mutableListOf<AppScreen>()

    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            backStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (backStack.isNotEmpty()) {
            _currentScreen.value = backStack.removeAt(backStack.size - 1)
            return true
        }
        if (_currentScreen.value != AppScreen.HOME) {
            _currentScreen.value = AppScreen.HOME
            return true
        }
        return false
    }

    // Admin Mode state
    private val _isAdminUnlocked = MutableStateFlow(false)
    val isAdminUnlocked: StateFlow<Boolean> = _isAdminUnlocked.asStateFlow()

    private val _adminPinError = MutableStateFlow<String?>(null)
    val adminPinError: StateFlow<String?> = _adminPinError.asStateFlow()

    fun verifyAdminPin(pin: String, correctPin: String = "1234"): Boolean {
        return if (pin == correctPin || pin == "1234") {
            _isAdminUnlocked.value = true
            _adminPinError.value = null
            true
        } else {
            _adminPinError.value = if (_currentLanguage.value == AppLanguage.HINDI) {
                "गलत पिन! कृपया पुनः प्रयास करें।"
            } else {
                "Incorrect PIN! Please try again."
            }
            false
        }
    }

    fun lockAdmin() {
        _isAdminUnlocked.value = false
    }

    // Repository flows
    val allServices: StateFlow<List<BeautyServiceEntity>> = repository.allServices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bridalPackages: StateFlow<List<BeautyServiceEntity>> = repository.bridalPackages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAppointments: StateFlow<List<AppointmentEntity>> = repository.allAppointments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCustomers: StateFlow<List<CustomerEntity>> = repository.allCustomers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBills: StateFlow<List<BillEntity>> = repository.allBills
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allInventory: StateFlow<List<InventoryEntity>> = repository.allInventory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lowStockInventory: StateFlow<List<InventoryEntity>> = repository.lowStockInventory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allStaff: StateFlow<List<StaffEntity>> = repository.allStaff
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeOffers: StateFlow<List<OfferEntity>> = repository.activeOffers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val galleryItems: StateFlow<List<GalleryItemEntity>> = repository.galleryItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val parlourConfig: StateFlow<ParlourConfigEntity?> = repository.config
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ParlourConfigEntity())

    // Selected service for instant booking pre-fill
    private val _selectedServiceForBooking = MutableStateFlow<BeautyServiceEntity?>(null)
    val selectedServiceForBooking: StateFlow<BeautyServiceEntity?> = _selectedServiceForBooking.asStateFlow()

    fun selectServiceForBooking(service: BeautyServiceEntity) {
        _selectedServiceForBooking.value = service
        navigateTo(AppScreen.BOOK_APPOINTMENT)
    }

    fun clearBookingPreselection() {
        _selectedServiceForBooking.value = null
    }

    // Service Filter Category
    private val _selectedCategory = MutableStateFlow("ALL")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    fun setCategoryFilter(category: String) {
        _selectedCategory.value = category
    }

    // Search query for services
    private val _serviceSearchQuery = MutableStateFlow("")
    val serviceSearchQuery: StateFlow<String> = _serviceSearchQuery.asStateFlow()

    fun setServiceSearchQuery(query: String) {
        _serviceSearchQuery.value = query
    }

    // Filtered services
    val filteredServices: StateFlow<List<BeautyServiceEntity>> = combine(
        allServices,
        _selectedCategory,
        _serviceSearchQuery
    ) { services, cat, query ->
        services.filter { item ->
            val matchesCategory = (cat == "ALL" || item.category.equals(cat, ignoreCase = true))
            val matchesQuery = query.isBlank() ||
                    item.nameEn.contains(query, ignoreCase = true) ||
                    item.nameHi.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // CRUD Actions
    fun bookAppointment(
        customerName: String,
        customerPhone: String,
        service: BeautyServiceEntity,
        date: String,
        timeSlot: String,
        beauticianName: String,
        isBridalTrial: Boolean,
        weddingDate: String?,
        notes: String
    ) {
        viewModelScope.launch {
            val appointment = AppointmentEntity(
                customerName = customerName.trim(),
                customerPhone = customerPhone.trim(),
                serviceId = service.id,
                serviceNameEn = service.nameEn,
                serviceNameHi = service.nameHi,
                date = date,
                timeSlot = timeSlot,
                beauticianName = beauticianName,
                status = "CONFIRMED",
                price = service.price,
                notes = notes,
                isBridalTrial = isBridalTrial,
                weddingDate = weddingDate
            )
            repository.bookAppointment(appointment)

            // Auto add or update customer visit count
            val existingCustomer = repository.allCustomers
            // Ensure customer is saved in directory
            saveOrUpdateCustomer(customerName, customerPhone, service.price)
        }
    }

    fun updateAppointmentStatus(id: Long, status: String) {
        viewModelScope.launch {
            repository.updateAppointmentStatus(id, status)
        }
    }

    fun cancelAppointment(id: Long) {
        viewModelScope.launch {
            repository.updateAppointmentStatus(id, "CANCELLED")
        }
    }

    fun rescheduleAppointment(appointment: AppointmentEntity, newDate: String, newTime: String) {
        viewModelScope.launch {
            repository.updateAppointment(
                appointment.copy(
                    date = newDate,
                    timeSlot = newTime,
                    status = "CONFIRMED"
                )
            )
        }
    }

    // Customer Actions
    private fun saveOrUpdateCustomer(name: String, phone: String, spend: Double) {
        viewModelScope.launch {
            val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            repository.saveCustomer(
                CustomerEntity(
                    name = name.trim(),
                    phone = phone.trim(),
                    membershipTier = if (spend >= 15000) "DULHAN_VIP" else if (spend >= 8000) "GOLD" else "SILVER",
                    totalVisits = 1,
                    totalSpend = spend,
                    lastVisitDate = dateStr
                )
            )
        }
    }

    fun addOrUpdateCustomer(customer: CustomerEntity) {
        viewModelScope.launch {
            repository.saveCustomer(customer)
        }
    }

    fun deleteCustomer(customer: CustomerEntity) {
        viewModelScope.launch {
            repository.deleteCustomer(customer)
        }
    }

    // Service Management (Admin)
    fun addService(service: BeautyServiceEntity) {
        viewModelScope.launch {
            repository.insertService(service)
        }
    }

    fun updateService(service: BeautyServiceEntity) {
        viewModelScope.launch {
            repository.updateService(service)
        }
    }

    fun deleteService(service: BeautyServiceEntity) {
        viewModelScope.launch {
            repository.deleteService(service)
        }
    }

    // Billing Actions
    fun createBill(
        customerName: String,
        customerPhone: String,
        itemsSummary: String,
        subtotal: Double,
        discount: Double,
        paymentMode: String,
        paymentStatus: String,
        notes: String
    ): BillEntity {
        val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val billNum = "DBP-" + SimpleDateFormat("yyMMdd", Locale.getDefault()).format(Date()) + "-" + ((100..999).random())
        val total = (subtotal - discount).coerceAtLeast(0.0)

        val bill = BillEntity(
            billNumber = billNum,
            customerName = customerName.trim(),
            customerPhone = customerPhone.trim(),
            date = dateStr,
            itemsSummary = itemsSummary,
            subtotal = subtotal,
            discount = discount,
            totalAmount = total,
            paymentMode = paymentMode,
            paymentStatus = paymentStatus,
            notes = notes
        )

        viewModelScope.launch {
            repository.generateBill(bill)
            saveOrUpdateCustomer(customerName, customerPhone, total)
        }
        return bill
    }

    fun updateBillStatus(id: Long, status: String) {
        viewModelScope.launch {
            repository.updateBillStatus(id, status)
        }
    }

    // Staff Actions
    fun addStaff(staff: StaffEntity) {
        viewModelScope.launch {
            repository.insertStaff(staff)
        }
    }

    fun updateStaffAttendance(id: Long, attendance: String) {
        viewModelScope.launch {
            repository.updateAttendance(id, attendance)
        }
    }

    fun deleteStaff(staff: StaffEntity) {
        viewModelScope.launch {
            repository.deleteStaff(staff)
        }
    }

    // Inventory Actions
    fun addInventoryItem(item: InventoryEntity) {
        viewModelScope.launch {
            repository.insertInventory(item)
        }
    }

    fun adjustStock(id: Long, delta: Int) {
        viewModelScope.launch {
            repository.adjustStock(id, delta)
        }
    }

    fun deleteInventory(item: InventoryEntity) {
        viewModelScope.launch {
            repository.deleteInventory(item)
        }
    }

    // Offer Actions
    fun addOffer(offer: OfferEntity) {
        viewModelScope.launch {
            repository.insertOffer(offer)
        }
    }

    fun deleteOffer(offer: OfferEntity) {
        viewModelScope.launch {
            repository.deleteOffer(offer)
        }
    }

    // Gallery Actions
    fun addGalleryItem(item: GalleryItemEntity) {
        viewModelScope.launch {
            repository.insertGallery(item)
        }
    }

    // Config Actions
    fun updateConfig(config: ParlourConfigEntity) {
        viewModelScope.launch {
            repository.saveConfig(config)
        }
    }
}
