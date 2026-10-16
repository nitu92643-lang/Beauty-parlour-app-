package com.example.data.repository

import com.example.data.local.ParlourDao
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class ParlourRepository(private val dao: ParlourDao) {

    val allServices: Flow<List<BeautyServiceEntity>> = dao.getAllServices()
    val bridalPackages: Flow<List<BeautyServiceEntity>> = dao.getBridalPackages()
    val allAppointments: Flow<List<AppointmentEntity>> = dao.getAllAppointments()
    val allCustomers: Flow<List<CustomerEntity>> = dao.getAllCustomers()
    val allBills: Flow<List<BillEntity>> = dao.getAllBills()
    val allInventory: Flow<List<InventoryEntity>> = dao.getAllInventory()
    val lowStockInventory: Flow<List<InventoryEntity>> = dao.getLowStockInventory()
    val allStaff: Flow<List<StaffEntity>> = dao.getAllStaff()
    val activeOffers: Flow<List<OfferEntity>> = dao.getActiveOffers()
    val allOffers: Flow<List<OfferEntity>> = dao.getAllOffers()
    val galleryItems: Flow<List<GalleryItemEntity>> = dao.getAllGalleryItems()
    val config: Flow<ParlourConfigEntity?> = dao.getParlourConfig()

    suspend fun ensureSeeded() {
        val services = dao.getAllServices().firstOrNull()
        if (services.isNullOrEmpty()) {
            ParlourDatabase.seedInitialData(dao)
        }
    }

    // Services
    suspend fun insertService(service: BeautyServiceEntity) = dao.insertService(service)
    suspend fun updateService(service: BeautyServiceEntity) = dao.updateService(service)
    suspend fun deleteService(service: BeautyServiceEntity) = dao.deleteService(service)

    // Appointments
    suspend fun bookAppointment(appointment: AppointmentEntity) = dao.insertAppointment(appointment)
    suspend fun updateAppointmentStatus(id: Long, status: String) = dao.updateAppointmentStatus(id, status)
    suspend fun updateAppointment(appointment: AppointmentEntity) = dao.updateAppointment(appointment)
    suspend fun deleteAppointment(id: Long) = dao.deleteAppointment(id)

    // Customers
    suspend fun saveCustomer(customer: CustomerEntity) = dao.insertCustomer(customer)
    suspend fun updateCustomer(customer: CustomerEntity) = dao.updateCustomer(customer)
    suspend fun deleteCustomer(customer: CustomerEntity) = dao.deleteCustomer(customer)

    // Bills
    suspend fun generateBill(bill: BillEntity) = dao.insertBill(bill)
    suspend fun updateBillStatus(id: Long, status: String) = dao.updateBillPaymentStatus(id, status)

    // Inventory
    suspend fun insertInventory(item: InventoryEntity) = dao.insertInventoryItem(item)
    suspend fun updateInventory(item: InventoryEntity) = dao.updateInventoryItem(item)
    suspend fun adjustStock(id: Long, delta: Int) = dao.adjustInventoryStock(id, delta)
    suspend fun deleteInventory(item: InventoryEntity) = dao.deleteInventoryItem(item)

    // Staff
    suspend fun insertStaff(staff: StaffEntity) = dao.insertStaff(staff)
    suspend fun updateStaff(staff: StaffEntity) = dao.updateStaff(staff)
    suspend fun updateAttendance(id: Long, attendance: String) = dao.updateStaffAttendance(id, attendance)
    suspend fun deleteStaff(staff: StaffEntity) = dao.deleteStaff(staff)

    // Offers
    suspend fun insertOffer(offer: OfferEntity) = dao.insertOffer(offer)
    suspend fun deleteOffer(offer: OfferEntity) = dao.deleteOffer(offer)

    // Gallery
    suspend fun insertGallery(item: GalleryItemEntity) = dao.insertGalleryItem(item)

    // Settings
    suspend fun saveConfig(config: ParlourConfigEntity) = dao.saveParlourConfig(config)
}
