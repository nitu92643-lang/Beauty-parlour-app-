package com.example.ui.localization

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    HINDI("hi", "Hindi", "हिन्दी"),
    ENGLISH("en", "English", "English")
}

data class LocalizedText(
    val hi: String,
    val en: String
) {
    fun get(lang: AppLanguage): String = if (lang == AppLanguage.HINDI) hi else en
}

object S {
    // App Branding
    val appName = LocalizedText("दुल्हन ब्यूटी पार्लर", "Dulhan Beauty Parlour")
    val appTagline = LocalizedText("हर दुल्हन का सबसे ख़ास निखार", "Where Every Bride Shines Brightest")
    val welcomeMessage = LocalizedText("स्वागत है दुल्हन ब्यूटी पार्लर में!", "Welcome to Dulhan Beauty Parlour!")

    // Navigation Tabs
    val tabHome = LocalizedText("होम", "Home")
    val tabServices = LocalizedText("सेवाएं", "Services")
    val tabBridal = LocalizedText("ब्राइडल", "Bridal")
    val tabBooking = LocalizedText("बुकिंग", "Bookings")
    val tabBilling = LocalizedText("बिलिंग", "Billing")
    val tabMore = LocalizedText("और", "More")

    // Quick Actions
    val bookAppointment = LocalizedText("अपॉइंटमेंट बुक करें", "Book Appointment")
    val viewServices = LocalizedText("सभी सेवाएं देखें", "View All Services")
    val bridalPackages = LocalizedText("ब्राइडल पैकेज", "Bridal Packages")
    val callUs = LocalizedText("कॉल करें", "Call Parlour")
    val whatsAppUs = LocalizedText("व्हाट्सएप", "WhatsApp")
    val getDirections = LocalizedText("रास्ता देखें (मैप)", "Get Directions")
    val adminPanel = LocalizedText("एडमिन पैनल", "Admin Panel")

    // Home Sections
    val featuredServices = LocalizedText("लोकप्रिय सेवाएं", "Featured Services")
    val specialOffers = LocalizedText("आज के विशेष ऑफर", "Today's Special Offers")
    val popularBridal = LocalizedText("पॉपुलर ब्राइडल पैकेज", "Popular Bridal Packages")
    val whyChooseUs = LocalizedText("हमारी विशेषताएं", "Why Choose Us")
    val clientLove = LocalizedText("ग्राहकों की राय", "Happy Brides & Clients")

    // Services
    val servicesTitle = LocalizedText("ब्यूटी सेवाएं और मूल्य", "Beauty Services & Pricing")
    val searchServices = LocalizedText("सेवा खोजें...", "Search service...")
    val allCategories = LocalizedText("सभी", "All")
    val catBridal = LocalizedText("ब्राइडल", "Bridal")
    val catMakeup = LocalizedText("मेकअप", "Makeup")
    val catHair = LocalizedText("हेयर", "Hair")
    val catSkin = LocalizedText("स्किन", "Skin")
    val catSpa = LocalizedText("स्पा व फेशियल", "Spa & Facial")
    val catMehndi = LocalizedText("मेहंदी", "Mehndi")
    val catNails = LocalizedText("नेल्स व वैक्सिंग", "Nails & Wax")
    val bookNow = LocalizedText("बुक करें", "Book Now")
    val duration = LocalizedText("समय", "Duration")
    val price = LocalizedText("कीमत", "Price")
    val min = LocalizedText("मिनट", "mins")

    // Booking Screen
    val newBookingTitle = LocalizedText("नया अपॉइंटमेंट बुक करें", "Book an Appointment")
    val selectService = LocalizedText("सेवा चुनें", "Select Service")
    val selectDate = LocalizedText("तारीख चुनें", "Select Date")
    val selectTime = LocalizedText("समय स्लॉट चुनें", "Select Time Slot")
    val customerName = LocalizedText("ग्राहक का नाम", "Customer Name")
    val customerPhone = LocalizedText("मोबाइल नंबर", "Mobile Number")
    val selectStaff = LocalizedText("पसंदीदा ब्यूटीशियन", "Preferred Beautician")
    val anyStaff = LocalizedText("कोई भी उपलब्ध", "Any Available")
    val specialNotes = LocalizedText("विशेष आवश्यकताएं / नोट", "Special Requests / Notes")
    val isBridalTrial = LocalizedText("क्या यह ब्राइडल ट्रायल है?", "Is this a Bridal Trial?")
    val weddingDate = LocalizedText("शादी की तारीख (वैकल्पिक)", "Wedding Date (Optional)")
    val confirmBooking = LocalizedText("बुकिंग कन्फर्म करें", "Confirm Booking")
    val myBookings = LocalizedText("बुकिंग इतिहास", "Booking History")
    val statusPending = LocalizedText("प्रतीक्षारत", "Pending")
    val statusConfirmed = LocalizedText("स्वीकृत", "Confirmed")
    val statusCompleted = LocalizedText("पूर्ण", "Completed")
    val statusCancelled = LocalizedText("रद्द", "Cancelled")
    val reschedule = LocalizedText("री-शेड्यूल", "Reschedule")
    val cancel = LocalizedText("रद्द करें", "Cancel")

    // Bridal Section
    val bridalHeader = LocalizedText("रॉयल ब्राइडल स्टूडियो", "Royal Bridal Studio")
    val bridalSubtitle = LocalizedText("आपकी शादी के दिन के लिए सर्वोत्तम ब्राइडल लुक", "Bespoke bridal looks for your dream wedding day")
    val customizePackage = LocalizedText("पैकेज कस्टमाइज़ करें", "Customize Package")
    val bookTrial = LocalizedText("ब्राइडल ट्रायल बुक करें", "Book Bridal Trial")
    val packageIncludes = LocalizedText("पैकेज में शामिल है:", "Package Includes:")

    // Billing
    val billingTitle = LocalizedText("बिलिंग व रसीद", "Billing & Invoices")
    val newBill = LocalizedText("नया बिल बनाएं", "Create New Bill")
    val billNumber = LocalizedText("बिल संख्या", "Bill No.")
    val selectServicesForBill = LocalizedText("बिल में सेवाएं जोड़ें", "Add Services to Bill")
    val subtotal = LocalizedText("उप-योग", "Subtotal")
    val discount = LocalizedText("छूट (डिस्काउंट)", "Discount")
    val totalAmount = LocalizedText("कुल देय राशि", "Total Payable")
    val paymentMode = LocalizedText("भुगतान का माध्यम", "Payment Method")
    val cash = LocalizedText("नकद (Cash)", "Cash")
    val upi = LocalizedText("यूपीआई (UPI)", "UPI / QR")
    val card = LocalizedText("कार्ड (Card)", "Card")
    val paymentStatus = LocalizedText("भुगतान स्थिति", "Payment Status")
    val paid = LocalizedText("भुगतान प्राप्त (Paid)", "Paid")
    val pending = LocalizedText("बकाया (Pending)", "Pending")
    val generateBill = LocalizedText("बिल सेव व शेयर करें", "Generate & Share Bill")
    val shareReceipt = LocalizedText("व्हाट्सएप पर रसीद भेजें", "Share Receipt on WhatsApp")
    val todaySales = LocalizedText("आज की कुल बिक्री", "Today's Total Sales")
    val monthlySales = LocalizedText("इस महीने की बिक्री", "This Month's Sales")

    // Customer Management
    val customerMgmt = LocalizedText("ग्राहक प्रबंधन", "Customer Management")
    val totalCustomers = LocalizedText("कुल ग्राहक", "Total Customers")
    val addCustomer = LocalizedText("नया ग्राहक जोड़ें", "Add Customer")
    val searchCustomer = LocalizedText("नाम या फोन से खोजें...", "Search by name or phone...")
    val customerPreferences = LocalizedText("पसंद और स्किन टाइप", "Preferences & Skin Type")
    val birthday = LocalizedText("जन्मदिन", "Birthday")
    val anniversary = LocalizedText("सालगिरह (Anniversary)", "Anniversary")
    val sendGreeting = LocalizedText("बधाई व ऑफर भेजें", "Send Greeting & Offer")
    val visits = LocalizedText("विज़िट्स", "Visits")
    val totalSpent = LocalizedText("कुल खर्च", "Total Spent")
    val membershipTier = LocalizedText("सदस्यता टियर", "Membership Tier")

    // Staff Management
    val staffMgmt = LocalizedText("स्टाफ व ब्यूटीशियन", "Staff & Beauticians")
    val addStaff = LocalizedText("नया स्टाफ जोड़ें", "Add Staff")
    val attendance = LocalizedText("आज की उपस्थिति", "Today's Attendance")
    val present = LocalizedText("उपस्थित (Present)", "Present")
    val absent = LocalizedText("अनुपस्थित (Absent)", "Absent")
    val onLeave = LocalizedText("छुट्टी (On Leave)", "On Leave")
    val completedAppointments = LocalizedText("पूर्ण अपॉइंटमेंट", "Completed Bookings")
    val rating = LocalizedText("रेटिंग", "Rating")

    // Inventory
    val inventoryTitle = LocalizedText("इन्वेंटरी व प्रोडक्ट्स", "Inventory & Cosmetics")
    val addProduct = LocalizedText("नया प्रोडक्ट जोड़ें", "Add Product")
    val currentStock = LocalizedText("वर्तमान स्टॉक", "Current Stock")
    val lowStockAlert = LocalizedText("कम स्टॉक अलर्ट", "Low Stock Alert")
    val expiryDate = LocalizedText("एक्सपायरी डेट", "Expiry Date")
    val restock = LocalizedText("स्टॉक बढ़ाएं", "Restock")

    // Offers & Membership
    val offersTitle = LocalizedText("ऑफर व मेंबरशिप", "Offers & Memberships")
    val addCoupon = LocalizedText("नया कूपन बनाएं", "Create Coupon")
    val couponCode = LocalizedText("कूपन कोड", "Coupon Code")
    val validTill = LocalizedText("मान्य तिथि", "Valid Until")
    val membershipPlans = LocalizedText("मेंबरशिप प्लान", "Membership Plans")

    // Gallery
    val galleryTitle = LocalizedText("ब्राइडल व हेयर गैलरी", "Bridal & Hair Gallery")
    val addPhoto = LocalizedText("नई फ़ोटो जोड़ें", "Add Photo")

    // Admin Dashboard
    val adminTitle = LocalizedText("एडमिन डैशबोर्ड", "Admin Dashboard")
    val enterPin = LocalizedText("एडमिन पिन दर्ज करें", "Enter Admin PIN")
    val defaultPinHint = LocalizedText("डिफ़ॉल्ट पिन 1234 है", "Default PIN is 1234")
    val unlock = LocalizedText("अनलॉक करें", "Unlock")
    val lockAdmin = LocalizedText("एडमिन लॉक करें", "Lock Admin")
    val wrongPin = LocalizedText("गलत पिन! कृपया पुनः प्रयास करें।", "Incorrect PIN! Try again.")
    val backupData = LocalizedText("डाटा बैकअप / एक्सपोर्ट", "Backup & Export Data")
    val backupSuccess = LocalizedText("बैकअप क्लिपबोर्ड पर कॉपी हुआ!", "Backup copied to clipboard!")

    // Contact & Location
    val contactTitle = LocalizedText("संपर्क और पता", "Contact & Location")
    val address = LocalizedText("पता", "Address")
    val phone = LocalizedText("फोन नंबर", "Phone Number")
    val hours = LocalizedText("खुलने का समय", "Opening Hours")
    val defaultAddress = LocalizedText("दुकान नं. 14, दुल्हन पैलेस, मेन रोड, गांधी चौक", "Shop 14, Dulhan Palace, Main Road, Gandhi Chowk")
    val defaultHours = LocalizedText("सुबह 10:00 से रात 8:30 (सातों दिन)", "10:00 AM - 8:30 PM (All 7 Days)")
    val defaultPhone = "+91 98765 43210"
    val defaultWhatsApp = "+919876543210"

    // Common Buttons
    val save = LocalizedText("सहेजें", "Save")
    val close = LocalizedText("बंद करें", "Close")
    val edit = LocalizedText("संपादित करें", "Edit")
    val delete = LocalizedText("हटाएं", "Delete")
    val confirm = LocalizedText("पुष्टि करें", "Confirm")
    val success = LocalizedText("सफलतापूर्वक पूर्ण!", "Success!")
}
