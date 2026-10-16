package com.example.data.model

object PrepopulatedData {
    val initialServices = listOf(
        BeautyServiceEntity(
            nameEn = "Bridal Makeup",
            nameHi = "ब्राइडल मेकअप (दुल्हन श्रृंगार)",
            category = "BRIDAL",
            descriptionEn = "Complete signature bridal makeover with premium international cosmetics, lashes, lens, and jewelry setting.",
            descriptionHi = "प्रीमियम इंटरनेशनल कॉस्मेटिक्स, लैशेज, लेंस और ज्वेलरी सेटिंग के साथ पूर्ण दुल्हन श्रृंगार।",
            price = 12000.0,
            durationMinutes = 180,
            isPopular = true,
            isBridalPackage = true,
            includesSummaryEn = "HD Foundation, Lashes, Hair Styling, Saree/Dupatta Draping",
            includesSummaryHi = "एचडी बेस, लैशेज, हेयर स्टाइलिंग, साड़ी/दुपट्टा ड्रेपिंग"
        ),
        BeautyServiceEntity(
            nameEn = "Engagement Makeup",
            nameHi = "सगाई मेकअप (Engagement Look)",
            category = "MAKEUP",
            descriptionEn = "Subtle, radiant, and dewy engagement look with elegant hair styling and accessory setting.",
            descriptionHi = "सुंदर, चमकदार सगाई मेकअप और शानदार हेयर स्टाइलिंग।",
            price = 5500.0,
            durationMinutes = 120,
            isPopular = true
        ),
        BeautyServiceEntity(
            nameEn = "Party Makeup",
            nameHi = "पार्टी मेकअप (Party Glam)",
            category = "MAKEUP",
            descriptionEn = "Glamorous party look tailored for cocktails, sangeet, or festive functions.",
            descriptionHi = "संगीत, रिसेप्शन और पार्टी के लिए आकर्षक ग्लैम मेकअप।",
            price = 3000.0,
            durationMinutes = 75,
            isPopular = true
        ),
        BeautyServiceEntity(
            nameEn = "Reception Makeup",
            nameHi = "रिसेप्शन मेकअप",
            category = "BRIDAL",
            descriptionEn = "High-shine evening glamorous bridal reception makeover with modern western or indo-western hairstyle.",
            descriptionHi = "रिसेप्शन पार्टी के लिए ग्लैमरस इवनिंग ब्राइडल मेकअप और वेस्टर्न हेयरडो।",
            price = 7000.0,
            durationMinutes = 120,
            isPopular = true
        ),
        BeautyServiceEntity(
            nameEn = "HD Makeup",
            nameHi = "एचडी मेकअप (High Definition)",
            category = "MAKEUP",
            descriptionEn = "Camera-ready high definition flawless base that looks natural even under bright 4K photography.",
            descriptionHi = "कैमरा व फोटोग्राफी के लिए नेचुरल और लंबे समय तक टिकने वाला 4K एचडी मेकअप।",
            price = 6000.0,
            durationMinutes = 90,
            isPopular = true
        ),
        BeautyServiceEntity(
            nameEn = "Airbrush Makeup",
            nameHi = "एयरब्रश मेकअप",
            category = "BRIDAL",
            descriptionEn = "Silicone-based micro-mist airbrush application for 24-hour waterproof, transfer-proof finish.",
            descriptionHi = "24 घंटे वाटरप्रूफ और ट्रांसफ़र-प्रूफ फिनिश के लिए आधुनिक एयरब्रश मेकअप।",
            price = 15000.0,
            durationMinutes = 150,
            isPopular = true,
            isBridalPackage = true
        ),
        BeautyServiceEntity(
            nameEn = "Haircut & Hairstyling",
            nameHi = "हेयरकट और हेयर स्टाइलिंग",
            category = "HAIR",
            descriptionEn = "Professional trendy layer, feather, or bob cuts with blow-dry and styling.",
            descriptionHi = "ट्रेंडी लेयर, फेदर या बॉब कट के साथ ब्लो-ड्राई और स्टाइलिंग।",
            price = 650.0,
            durationMinutes = 45,
            isPopular = true
        ),
        BeautyServiceEntity(
            nameEn = "Hair Coloring",
            nameHi = "हेयर कलरिंग (Global / Highlights)",
            category = "HAIR",
            descriptionEn = "Ammonia-free global hair color, balayage, or trendy ombre streaks.",
            descriptionHi = "अमोनिया-मुक्त ग्लोबल हेयर कलर, स्ट्रीक्स व हाइलाइट्स।",
            price = 2800.0,
            durationMinutes = 90
        ),
        BeautyServiceEntity(
            nameEn = "Hair Spa",
            nameHi = "हेयर स्पा (Deep Nourishment)",
            category = "SPA",
            descriptionEn = "Intensive scalp massage, steam therapy, and protein conditioning mask.",
            descriptionHi = "बालों की गहराई से पोषण, हेड मसाज और स्टीम थेरेपी।",
            price = 1200.0,
            durationMinutes = 60,
            isPopular = true
        ),
        BeautyServiceEntity(
            nameEn = "Facial & Cleanup",
            nameHi = "फेशियल और क्लीनअप (Gold / Diamond)",
            category = "SPA",
            descriptionEn = "Deep pore cleansing, fruit / diamond scrub, steam, blackhead extraction and glow mask.",
            descriptionHi = "डीप पोर क्लींजिंग, गोल्ड/डायमंड स्क्रब और ग्लोइंग फेस पैक।",
            price = 1500.0,
            durationMinutes = 60,
            isPopular = true
        ),
        BeautyServiceEntity(
            nameEn = "Skin Care Treatment",
            nameHi = "स्किन केयर ट्रीटमेंट",
            category = "SKIN",
            descriptionEn = "Targeted treatment for acne, pigmentation, hydration, and anti-aging radiance.",
            descriptionHi = "पिंपल्स, टैनिंग और ग्लो के लिए विशेष डर्मा स्किन ट्रीटमेंट।",
            price = 2200.0,
            durationMinutes = 60
        ),
        BeautyServiceEntity(
            nameEn = "Waxing (Full Body / Arms / Legs)",
            nameHi = "वैक्सिंग (रिका / हनी वैक्स)",
            category = "NAILS",
            descriptionEn = "Smooth painless Rica white chocolate or honey waxing for soft glowing skin.",
            descriptionHi = "रिका वाइट चॉकलेट या हनी वैक्स से रेशमी मुलायम त्वचा।",
            price = 900.0,
            durationMinutes = 45
        ),
        BeautyServiceEntity(
            nameEn = "Threading",
            nameHi = "थ्रेडिंग (Full Face)",
            category = "SKIN",
            descriptionEn = "Gentle precision thread removal for upper lip, chin, forehead, and sides.",
            descriptionHi = "चेहरे के अनचाहे बालों को हटाने के लिए जेंटल थ्रेडिंग।",
            price = 150.0,
            durationMinutes = 20
        ),
        BeautyServiceEntity(
            nameEn = "Manicure",
            nameHi = "मेनिक्योर (Hand Care & Polish)",
            category = "NAILS",
            descriptionEn = "Soothing hand scrub, cuticle care, hand massage, and gel polish.",
            descriptionHi = "हाथों का स्क्रब, मसाज और खूबसूरत नेल पॉलिश।",
            price = 500.0,
            durationMinutes = 40
        ),
        BeautyServiceEntity(
            nameEn = "Pedicure",
            nameHi = "पेडिक्योर (Foot Spa & Healing)",
            category = "NAILS",
            descriptionEn = "Warm aromatic foot soak, dead skin exfoliation, foot massage and nail care.",
            descriptionHi = "पैरों का स्क्रब, मृत त्वचा हटाना, रिलैक्सिंग मसाज।",
            price = 700.0,
            durationMinutes = 45
        ),
        BeautyServiceEntity(
            nameEn = "Mehndi Design",
            nameHi = "मेहंदी डिज़ाइन (Bridal / Arabic)",
            category = "MEHNDI",
            descriptionEn = "Intricate traditional Rajasthani, Arabic, or modern bridal portrait mehndi.",
            descriptionHi = "बारीक राजस्थानी, अरेबिक और शाही ब्राइडल दुल्हन मेहंदी।",
            price = 3500.0,
            durationMinutes = 180,
            isPopular = true
        ),
        BeautyServiceEntity(
            nameEn = "Saree Draping",
            nameHi = "साड़ी ड्रेपिंग (Traditional & Western)",
            category = "BRIDAL",
            descriptionEn = "Flawless saree and lehenga draping in Bengali, Gujarati, Maharashtrian, or modern styles.",
            descriptionHi = "साड़ी और लहंगा को परफ़ेक्ट और आरामदायक स्टाइल में बांधना।",
            price = 450.0,
            durationMinutes = 25
        ),
        BeautyServiceEntity(
            nameEn = "Eyebrow Shaping",
            nameHi = "आईब्रो शेपिंग (Perfect Arch)",
            category = "SKIN",
            descriptionEn = "Artistic eyebrow shaping to complement your facial structure.",
            descriptionHi = "चेहरे के अनुसार सुंदर और परफेक्ट आईब्रो शेप।",
            price = 70.0,
            durationMinutes = 15
        ),
        BeautyServiceEntity(
            nameEn = "Hair Treatment (Keratin / Botox)",
            nameHi = "हेयर ट्रीटमेंट (केराटिन / बोटोक्स)",
            category = "HAIR",
            descriptionEn = "Deep bond repair, frizz control, smoothing keratin or hair botox.",
            descriptionHi = "रूखे बेजान बालों के लिए केराटिन स्मूथनिंग और हेयर बोटोक्स।",
            price = 4500.0,
            durationMinutes = 150
        ),
        BeautyServiceEntity(
            nameEn = "Pre-Bridal Package",
            nameHi = "प्री-ब्राइडल पैकेज (Full Glow)",
            category = "BRIDAL",
            descriptionEn = "3-day or 7-day bridal glow package: full body polish, diamond facial, waxing, mani-pedi, hair spa.",
            descriptionHi = "शादी से पहले पूरा निखार: बॉडी पॉलिशिंग, डायमंड फेशियल, वैक्सिंग, मेनी-पेडी और हेयर स्पा।",
            price = 8500.0,
            durationMinutes = 300,
            isPopular = true,
            isBridalPackage = true
        ),
        BeautyServiceEntity(
            nameEn = "Groom Makeup",
            nameHi = "ग्रूम / दूल्हा मेकअप (HD Subtle)",
            category = "GROOM",
            descriptionEn = "Subtle beard grooming, skin toning, matte HD touch-up, and safa/turban assistance.",
            descriptionHi = "दूल्हे के लिए बियर्ड ग्रूमिंग, नेचुरल एचडी मैट टच-अप और साफ़ा सेटिंग।",
            price = 3500.0,
            durationMinutes = 60
        ),
        BeautyServiceEntity(
            nameEn = "Nail Art & Extensions",
            nameHi = "नेल आर्ट और एक्सटेंशन्स",
            category = "NAILS",
            descriptionEn = "Acrylic/gel nail extensions with glitter, stone embellishments, and bridal french tips.",
            descriptionHi = "ग्लिटर, स्टोन और ब्राइडल फ्रेंच डिज़ाइन के साथ आकर्षक नेल आर्ट।",
            price = 1500.0,
            durationMinutes = 60,
            isPopular = true
        )
    )

    val initialBridalPackages = listOf(
        BeautyServiceEntity(
            nameEn = "Royal Dulhan HD Bridal Package",
            nameHi = "शाही दुल्हन एचडी ब्राइडल पैकेज",
            category = "BRIDAL",
            descriptionEn = "Signature HD bridal makeup, lashes, eye lens, luxury floral hair bun, saree/lehenga draping, and touch-up kit.",
            descriptionHi = "सिग्नेचर एचडी मेकअप, लैशेज, लेंस, फ्लोरल हेयर बन, लहंगा ड्रेपिंग और टचअप किट।",
            price = 14999.0,
            durationMinutes = 240,
            isPopular = true,
            isBridalPackage = true,
            includesSummaryEn = "HD Makeup + International Hairdo + Jewelry Setting + Saree Draping + Lashes + Contact Lenses",
            includesSummaryHi = "एचडी मेकअप + इंटरनेशनल हेयरडू + ज्वेलरी सेटिंग + साड़ी ड्रेपिंग + लैशेज + आई लेंस"
        ),
        BeautyServiceEntity(
            nameEn = "Rajkumari Airbrush Bridal Suite",
            nameHi = "राजकुमारी एयरब्रश ब्राइडल स्वीट",
            category = "BRIDAL",
            descriptionEn = "Ultra-luxury silicone airbrush makeup, 3D lashes, custom designer hairdo, pre-makeup skin booster ampoule.",
            descriptionHi = "अल्ट्रा लग्जरी एयरब्रश मेकअप, 3डी लैशेज, डिजाइनर हेयरडू और स्किन बूस्टर सीरम।",
            price = 18999.0,
            durationMinutes = 240,
            isPopular = true,
            isBridalPackage = true,
            includesSummaryEn = "Airbrush Makeup + 3D Mink Lashes + Designer Hair Styling + Skin Ampoule + Free Bridal Trial",
            includesSummaryHi = "एयरब्रश मेकअप + 3डी मिंक लैशेज + डिजाइनर हेयर स्टाइलिंग + स्किन सीरम + फ्री ब्राइडल ट्रायल"
        ),
        BeautyServiceEntity(
            nameEn = "Complete Pre-Bridal + Wedding Combo",
            nameHi = "सम्पूर्ण प्री-ब्राइडल + वेडिंग कॉम्बो",
            category = "BRIDAL",
            descriptionEn = "Complete 7-day bridal journey: Full body polish, 2x luxury facials, Rica waxing, hair botox, bridal mehndi, and wedding makeup.",
            descriptionHi = "7 दिवसीय दुल्हन यात्रा: बॉडी पॉलिशिंग, 2 लग्जरी फेशियल, रिका वैक्स, मेहंदी और मुख्य शादी मेकअप।",
            price = 24999.0,
            durationMinutes = 480,
            isPopular = true,
            isBridalPackage = true,
            includesSummaryEn = "Pre-Bridal (Body Polish + 2 Facials + Waxing + Spa) + Bridal HD Makeup + Bridal Mehndi",
            includesSummaryHi = "प्री-ब्राइडल (बॉडी पॉलिश + 2 फेशियल + वैक्सिंग + स्पा) + ब्राइडल मेकअप + दुल्हन मेहंदी"
        )
    )

    val initialStaff = listOf(
        StaffEntity(
            name = "Pooja Sharma",
            phone = "+91 98234 11223",
            role = "SENIOR_ARTIST",
            specialty = "HD & Airbrush Bridal Makeup",
            rating = 4.9f,
            isAvailable = true,
            todayAttendance = "PRESENT",
            completedAppointmentsCount = 142
        ),
        StaffEntity(
            name = "Sunita Verma",
            phone = "+91 98765 22334",
            role = "HAIRSTYLIST",
            specialty = "Bridal Hairdos & Keratin",
            rating = 4.8f,
            isAvailable = true,
            todayAttendance = "PRESENT",
            completedAppointmentsCount = 98
        ),
        StaffEntity(
            name = "Neha Gupta",
            phone = "+91 99887 33445",
            role = "MEHNDI_EXPERT",
            specialty = "Traditional Bridal Mehndi & Nail Art",
            rating = 4.9f,
            isAvailable = true,
            todayAttendance = "PRESENT",
            completedAppointmentsCount = 85
        ),
        StaffEntity(
            name = "Ritu Rani",
            phone = "+91 97654 44556",
            role = "BEAUTICIAN",
            specialty = "Facials, Skin Glow & Waxing",
            rating = 4.7f,
            isAvailable = true,
            todayAttendance = "PRESENT",
            completedAppointmentsCount = 115
        )
    )

    val initialInventory = listOf(
        InventoryEntity(
            productNameEn = "MAC Studio Fix Foundation (NC 25-40)",
            productNameHi = "मैक स्टूडियो फिक्स फाउंडेशन",
            brand = "MAC Cosmetics",
            category = "MAKEUP",
            currentStock = 8,
            minThreshold = 3,
            unit = "bottles",
            purchasePrice = 3200.0,
            expiryDate = "2027-08",
            lastRestocked = "2026-09-10"
        ),
        InventoryEntity(
            productNameEn = "O3+ Bridal Facial Radiance Kit",
            productNameHi = "O3+ ब्राइडल फेशियल किट",
            brand = "O3+ Professional",
            category = "SKINCARE",
            currentStock = 4,
            minThreshold = 3,
            unit = "kits",
            purchasePrice = 2800.0,
            expiryDate = "2027-04",
            lastRestocked = "2026-09-25"
        ),
        InventoryEntity(
            productNameEn = "Rica White Chocolate Wax",
            productNameHi = "रिका वाइट चॉकलेट वैक्स",
            brand = "Rica Italy",
            category = "WAX",
            currentStock = 12,
            minThreshold = 4,
            unit = "tins",
            purchasePrice = 1150.0,
            expiryDate = "2028-01",
            lastRestocked = "2026-10-01"
        ),
        InventoryEntity(
            productNameEn = "L'Oréal Professionnel Serie Expert Hair Mask",
            productNameHi = "लोरियल हेयर स्पा क्रीम",
            brand = "L'Oréal",
            category = "HAIR",
            currentStock = 6,
            minThreshold = 2,
            unit = "tubs",
            purchasePrice = 980.0,
            expiryDate = "2027-11",
            lastRestocked = "2026-09-18"
        ),
        InventoryEntity(
            productNameEn = "Natural Sojat Herbal Mehndi Cones",
            productNameHi = "सोजत हर्बल मेहंदी कोन",
            brand = "Sojat Gold",
            category = "MEHNDI",
            currentStock = 2, // Low stock alert triggered!
            minThreshold = 5,
            unit = "boxes",
            purchasePrice = 450.0,
            expiryDate = "2026-12",
            lastRestocked = "2026-08-15"
        ),
        InventoryEntity(
            productNameEn = "Gel Nail Polish Assorted Bridal Red/Gold",
            productNameHi = "जेल नेल पॉलिश सेट",
            brand = "Bluesky",
            category = "NAILS",
            currentStock = 15,
            minThreshold = 4,
            unit = "bottles",
            purchasePrice = 350.0,
            expiryDate = "2028-05",
            lastRestocked = "2026-10-05"
        )
    )

    val initialOffers = listOf(
        OfferEntity(
            code = "DULHAN20",
            titleEn = "Bridal Season 20% OFF",
            titleHi = "ब्राइडल सीजन 20% की छूट",
            descriptionEn = "Flat 20% discount on all premium bridal packages booked this month.",
            descriptionHi = "इस महीने बुक होने वाले सभी ब्राइडल पैकेज पर 20% फ्लैट छूट।",
            discountPercent = 20,
            minSpend = 8000.0,
            validUntil = "2026-12-31"
        ),
        OfferEntity(
            code = "KARWA_GLOW",
            titleEn = "Karwa Chauth & Festive Special",
            titleHi = "करवा चौथ और त्योहार स्पेशल",
            descriptionEn = "Free O3+ cleanup with full arms and legs chocolate waxing.",
            descriptionHi = "फुल वैक्सिंग के साथ फ्री O3+ क्लीनअप का लाभ उठाएं।",
            discountPercent = 15,
            minSpend = 2000.0,
            validUntil = "2026-11-15"
        ),
        OfferEntity(
            code = "FIRSTVISIT",
            titleEn = "Welcome New Client ₹300 OFF",
            titleHi = "नए ग्राहकों के लिए ₹300 की छूट",
            descriptionEn = "Instant ₹300 off on any service above ₹1,000 for first time visitors.",
            descriptionHi = "₹1000 से ऊपर की किसी भी सेवा पर तुरंत ₹300 की छूट।",
            flatDiscount = 300.0,
            minSpend = 1000.0,
            validUntil = "2026-12-31"
        )
    )

    val initialCustomers = listOf(
        CustomerEntity(
            name = "Priya Sharma",
            phone = "9812345678",
            email = "priya.sharma@example.com",
            preferences = "Prefers organic wax, allergic to strong bleaches, loves soft pink lip shades.",
            birthday = "22-10",
            anniversaryDate = "15-12",
            membershipTier = "DULHAN_VIP",
            totalVisits = 6,
            totalSpend = 28500.0,
            lastVisitDate = "2026-10-10"
        ),
        CustomerEntity(
            name = "Anjali Mehra",
            phone = "9876543211",
            email = "anjali.m@example.com",
            preferences = "Sensitive scalp, asks for Sunita for all haircuts.",
            birthday = "05-11",
            anniversaryDate = "20-02",
            membershipTier = "GOLD",
            totalVisits = 4,
            totalSpend = 14200.0,
            lastVisitDate = "2026-10-05"
        ),
        CustomerEntity(
            name = "Kavita Yadav",
            phone = "9988776655",
            email = "kavita.y@example.com",
            preferences = "Looking for Airbrush bridal makeup on Dec 4th.",
            birthday = "14-10", // Today!
            anniversaryDate = "08-01",
            membershipTier = "SILVER",
            totalVisits = 2,
            totalSpend = 8500.0,
            lastVisitDate = "2026-09-28"
        )
    )

    val initialAppointments = listOf(
        AppointmentEntity(
            customerName = "Priya Sharma",
            customerPhone = "9812345678",
            serviceId = 1,
            serviceNameEn = "Bridal Makeup",
            serviceNameHi = "ब्राइडल मेकअप",
            date = "2026-10-15",
            timeSlot = "11:00 AM",
            beauticianName = "Pooja Sharma",
            status = "CONFIRMED",
            price = 12000.0,
            notes = "Bridal dress is deep maroon lehenga. Wants HD waterproof base.",
            isBridalTrial = true,
            weddingDate = "2026-11-20"
        ),
        AppointmentEntity(
            customerName = "Anjali Mehra",
            customerPhone = "9876543211",
            serviceId = 10,
            serviceNameEn = "Facial & Cleanup",
            serviceNameHi = "फेशियल और क्लीनअप",
            date = "2026-10-15",
            timeSlot = "02:30 PM",
            beauticianName = "Ritu Rani",
            status = "CONFIRMED",
            price = 1500.0,
            notes = "Gold glow facial before Diwali."
        ),
        AppointmentEntity(
            customerName = "Sneha Patel",
            customerPhone = "9712398745",
            serviceId = 7,
            serviceNameEn = "Haircut & Hairstyling",
            serviceNameHi = "हेयरकट और हेयर स्टाइलिंग",
            date = "2026-10-16",
            timeSlot = "04:00 PM",
            beauticianName = "Sunita Verma",
            status = "BOOKED",
            price = 650.0,
            notes = "Feather cut with volume blow-dry."
        )
    )

    val initialBills = listOf(
        BillEntity(
            billNumber = "DBP-2026-1089",
            customerName = "Priya Sharma",
            customerPhone = "9812345678",
            date = "2026-10-10",
            itemsSummary = "Pre-Bridal Package (₹8,500), Hair Spa (₹1,200)",
            subtotal = 9700.0,
            discount = 1000.0,
            taxAmount = 0.0,
            totalAmount = 8700.0,
            paymentMode = "UPI",
            paymentStatus = "PAID",
            notes = "Paid via GPay UPI transaction ref #983421"
        ),
        BillEntity(
            billNumber = "DBP-2026-1090",
            customerName = "Anjali Mehra",
            customerPhone = "9876543211",
            date = "2026-10-05",
            itemsSummary = "Hair Coloring (₹2,800), Manicure & Pedicure (₹1,200)",
            subtotal = 4000.0,
            discount = 400.0,
            taxAmount = 0.0,
            totalAmount = 3600.0,
            paymentMode = "CASH",
            paymentStatus = "PAID",
            notes = "Cash received"
        )
    )

    val initialGallery = listOf(
        GalleryItemEntity(
            titleEn = "Royal Rajwada Bridal Look",
            titleHi = "शाही राजवाड़ा ब्राइडल लुक",
            category = "BRIDAL_MAKEUP",
            descriptionEn = "Heavy jewelry setting, bold eyes, and classic maroon bridal lips.",
            descriptionHi = "क्लासिक लाल/मैरून शादी का जोड़ा और पारंपरिक शाही ज्वेलरी।"
        ),
        GalleryItemEntity(
            titleEn = "Pastel Dewy Engagement Glow",
            titleHi = "पेस्टल ड्यूई एंगेजमेंट ग्लो",
            category = "BRIDAL_MAKEUP",
            descriptionEn = "Soft glam nude makeup for modern pastel lehenga look.",
            descriptionHi = "मॉडर्न और हल्का प्राकृतिक पेस्टल ब्राइडल मेकअप।"
        ),
        GalleryItemEntity(
            titleEn = "Floral Braided Hairdo & Bun",
            titleHi = "फ्लोरल ब्राइडल जुड़ा व चोटी",
            category = "HAIRSTYLES",
            descriptionEn = "Fresh baby's breath and jasmine gajra adorned bridal bun.",
            descriptionHi = "मोगरे के गजरे और गुलाब की पंखुड़ियों से सजा सुंदर जुड़ा।"
        ),
        GalleryItemEntity(
            titleEn = "Intricate Bridal Dulhan Mehndi",
            titleHi = "बारीक दुल्हन हाथ-पैर मेहंदी",
            category = "MEHNDI",
            descriptionEn = "Dulha-Dulhan portrait figures with intricate checks and mandalas.",
            descriptionHi = "दूल्हा-दुल्हन पोट्रेट और पारंपरिक राजस्थानी जाल।"
        ),
        GalleryItemEntity(
            titleEn = "Red & Gold Glitter Nail Art",
            titleHi = "रेड व गोल्डन ब्राइडल नेल्स",
            category = "NAILS",
            descriptionEn = "Sparkling stones with 3D acrylic roses and chrome gold tip.",
            descriptionHi = "चमकदार स्टोन, 3D रोज़ और क्रोम गोल्ड नेल एक्सटेंशन।"
        )
    )
}
