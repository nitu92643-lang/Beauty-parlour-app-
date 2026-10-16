package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

@Dao
interface ParlourDao {
    // --- Services ---
    @Query("SELECT * FROM services ORDER BY isPopular DESC, nameEn ASC")
    fun getAllServices(): Flow<List<BeautyServiceEntity>>

    @Query("SELECT * FROM services WHERE isBridalPackage = 1 ORDER BY price DESC")
    fun getBridalPackages(): Flow<List<BeautyServiceEntity>>

    @Query("SELECT * FROM services WHERE id = :id LIMIT 1")
    suspend fun getServiceById(id: Long): BeautyServiceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: BeautyServiceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<BeautyServiceEntity>)

    @Update
    suspend fun updateService(service: BeautyServiceEntity)

    @Delete
    suspend fun deleteService(service: BeautyServiceEntity)

    // --- Appointments ---
    @Query("SELECT * FROM appointments ORDER BY date ASC, timeSlot ASC")
    fun getAllAppointments(): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE date = :date ORDER BY timeSlot ASC")
    fun getAppointmentsForDate(date: String): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE customerPhone = :phone ORDER BY date DESC")
    fun getAppointmentsForCustomer(phone: String): Flow<List<AppointmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: AppointmentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointments(appointments: List<AppointmentEntity>)

    @Update
    suspend fun updateAppointment(appointment: AppointmentEntity)

    @Query("UPDATE appointments SET status = :status WHERE id = :id")
    suspend fun updateAppointmentStatus(id: Long, status: String)

    @Query("DELETE FROM appointments WHERE id = :id")
    suspend fun deleteAppointment(id: Long)

    // --- Customers ---
    @Query("SELECT * FROM customers ORDER BY totalSpend DESC, name ASC")
    fun getAllCustomers(): Flow<List<CustomerEntity>>

    @Query("SELECT * FROM customers WHERE phone = :phone LIMIT 1")
    suspend fun getCustomerByPhone(phone: String): CustomerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomers(customers: List<CustomerEntity>)

    @Update
    suspend fun updateCustomer(customer: CustomerEntity)

    @Delete
    suspend fun deleteCustomer(customer: CustomerEntity)

    // --- Bills ---
    @Query("SELECT * FROM bills ORDER BY timestamp DESC")
    fun getAllBills(): Flow<List<BillEntity>>

    @Query("SELECT * FROM bills WHERE customerPhone = :phone ORDER BY timestamp DESC")
    fun getBillsForCustomer(phone: String): Flow<List<BillEntity>>

    @Query("SELECT * FROM bills WHERE date = :date ORDER BY timestamp DESC")
    fun getBillsForDate(date: String): Flow<List<BillEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: BillEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBills(bills: List<BillEntity>)

    @Query("UPDATE bills SET paymentStatus = :status WHERE id = :id")
    suspend fun updateBillPaymentStatus(id: Long, status: String)

    // --- Inventory ---
    @Query("SELECT * FROM inventory ORDER BY productNameEn ASC")
    fun getAllInventory(): Flow<List<InventoryEntity>>

    @Query("SELECT * FROM inventory WHERE currentStock <= minThreshold")
    fun getLowStockInventory(): Flow<List<InventoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryItem(item: InventoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventory(items: List<InventoryEntity>)

    @Update
    suspend fun updateInventoryItem(item: InventoryEntity)

    @Query("UPDATE inventory SET currentStock = currentStock + :delta WHERE id = :id")
    suspend fun adjustInventoryStock(id: Long, delta: Int)

    @Delete
    suspend fun deleteInventoryItem(item: InventoryEntity)

    // --- Staff ---
    @Query("SELECT * FROM staff ORDER BY name ASC")
    fun getAllStaff(): Flow<List<StaffEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStaff(staff: StaffEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStaffList(staffList: List<StaffEntity>)

    @Update
    suspend fun updateStaff(staff: StaffEntity)

    @Query("UPDATE staff SET todayAttendance = :attendance WHERE id = :id")
    suspend fun updateStaffAttendance(id: Long, attendance: String)

    @Delete
    suspend fun deleteStaff(staff: StaffEntity)

    // --- Offers ---
    @Query("SELECT * FROM offers WHERE isActive = 1 ORDER BY discountPercent DESC")
    fun getActiveOffers(): Flow<List<OfferEntity>>

    @Query("SELECT * FROM offers ORDER BY id DESC")
    fun getAllOffers(): Flow<List<OfferEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOffer(offer: OfferEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOffers(offers: List<OfferEntity>)

    @Update
    suspend fun updateOffer(offer: OfferEntity)

    @Delete
    suspend fun deleteOffer(offer: OfferEntity)

    // --- Gallery ---
    @Query("SELECT * FROM gallery ORDER BY id DESC")
    fun getAllGalleryItems(): Flow<List<GalleryItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGalleryItem(item: GalleryItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGalleryItems(items: List<GalleryItemEntity>)

    // --- Config ---
    @Query("SELECT * FROM parlour_config WHERE configKey = 'MAIN_CONFIG' LIMIT 1")
    fun getParlourConfig(): Flow<ParlourConfigEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveParlourConfig(config: ParlourConfigEntity)
}
