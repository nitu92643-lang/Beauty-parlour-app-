package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "services")
data class BeautyServiceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nameEn: String,
    val nameHi: String,
    val category: String, // BRIDAL, MAKEUP, HAIR, SKIN, SPA, MEHNDI, NAILS, GROOM
    val descriptionEn: String,
    val descriptionHi: String,
    val price: Double,
    val durationMinutes: Int,
    val isPopular: Boolean = false,
    val isBridalPackage: Boolean = false,
    val includesSummaryEn: String = "",
    val includesSummaryHi: String = ""
)

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val customerName: String,
    val customerPhone: String,
    val serviceId: Long,
    val serviceNameEn: String,
    val serviceNameHi: String,
    val date: String, // YYYY-MM-DD
    val timeSlot: String, // e.g. "11:00 AM"
    val beauticianName: String,
    val status: String = "CONFIRMED", // PENDING, CONFIRMED, COMPLETED, CANCELLED
    val price: Double,
    val notes: String = "",
    val isBridalTrial: Boolean = false,
    val weddingDate: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String,
    val email: String? = null,
    val preferences: String = "", // e.g., "Sensitive skin, allergic to harsh bleach"
    val birthday: String? = null, // DD-MM
    val anniversaryDate: String? = null, // DD-MM
    val membershipTier: String = "STANDARD", // STANDARD, SILVER, GOLD, DULHAN_VIP
    val totalVisits: Int = 1,
    val totalSpend: Double = 0.0,
    val lastVisitDate: String = ""
)

@Entity(tableName = "bills")
data class BillEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val billNumber: String, // e.g. DBP-2026-1001
    val customerName: String,
    val customerPhone: String,
    val date: String, // YYYY-MM-DD
    val itemsSummary: String, // e.g. "Bridal HD Makeup (₹12,000), Hair Spa (₹1,500)"
    val subtotal: Double,
    val discount: Double = 0.0,
    val taxAmount: Double = 0.0,
    val totalAmount: Double,
    val paymentMode: String = "UPI", // CASH, UPI, CARD
    val paymentStatus: String = "PAID", // PAID, PENDING
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "inventory")
data class InventoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productNameEn: String,
    val productNameHi: String,
    val brand: String,
    val category: String, // MAKEUP, SKINCARE, HAIR, WAX, MEHNDI, NAILS
    val currentStock: Int,
    val minThreshold: Int = 5,
    val unit: String = "pcs",
    val purchasePrice: Double = 0.0,
    val expiryDate: String = "", // YYYY-MM
    val lastRestocked: String = ""
)

@Entity(tableName = "staff")
data class StaffEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String,
    val role: String, // SENIOR_ARTIST, BEAUTICIAN, HAIRSTYLIST, MEHNDI_EXPERT
    val specialty: String,
    val rating: Float = 4.8f,
    val isAvailable: Boolean = true,
    val todayAttendance: String = "PRESENT", // PRESENT, ABSENT, ON_LEAVE
    val completedAppointmentsCount: Int = 0
)

@Entity(tableName = "offers")
data class OfferEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val code: String, // e.g. DULHAN20
    val titleEn: String,
    val titleHi: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val discountPercent: Int = 15,
    val flatDiscount: Double = 0.0,
    val minSpend: Double = 1000.0,
    val validUntil: String = "2026-12-31",
    val isActive: Boolean = true
)

@Entity(tableName = "gallery")
data class GalleryItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val titleEn: String,
    val titleHi: String,
    val category: String, // BRIDAL_MAKEUP, HAIRSTYLES, MEHNDI, FACIAL, NAILS
    val descriptionEn: String,
    val descriptionHi: String,
    val imageType: String = "VECTOR_LUXE"
)

@Entity(tableName = "parlour_config")
data class ParlourConfigEntity(
    @PrimaryKey
    val configKey: String = "MAIN_CONFIG",
    val parlourNameEn: String = "Dulhan Beauty Parlour",
    val parlourNameHi: String = "दुल्हन ब्यूटी पार्लर",
    val adminPin: String = "1234",
    val phone: String = "+91 98765 43210",
    val whatsapp: String = "+919876543210",
    val addressEn: String = "Shop 14, Dulhan Palace, Main Market, Gandhi Chowk",
    val addressHi: String = "दुकान नं. 14, दुल्हन पैलेस, मेन मार्केट, गांधी चौक",
    val openingHoursEn: String = "10:00 AM - 08:30 PM (All 7 Days)",
    val openingHoursHi: String = "सुबह 10:00 से रात 08:30 (सातों दिन)",
    val mapsLocationUrl: String = "https://maps.google.com"
)
