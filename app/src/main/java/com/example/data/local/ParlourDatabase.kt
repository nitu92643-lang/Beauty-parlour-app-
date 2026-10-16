package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.AppointmentEntity
import com.example.data.model.BeautyServiceEntity
import com.example.data.model.BillEntity
import com.example.data.model.CustomerEntity
import com.example.data.model.GalleryItemEntity
import com.example.data.model.InventoryEntity
import com.example.data.model.OfferEntity
import com.example.data.model.ParlourConfigEntity
import com.example.data.model.PrepopulatedData
import com.example.data.model.StaffEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        BeautyServiceEntity::class,
        AppointmentEntity::class,
        CustomerEntity::class,
        BillEntity::class,
        InventoryEntity::class,
        StaffEntity::class,
        OfferEntity::class,
        GalleryItemEntity::class,
        ParlourConfigEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ParlourDatabase : RoomDatabase() {
    abstract fun parlourDao(): ParlourDao

    companion object {
        @Volatile
        private var INSTANCE: ParlourDatabase? = null

        fun getDatabase(context: Context): ParlourDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ParlourDatabase::class.java,
                    "dulhan_parlour_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate initial seed data on first create
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    seedInitialData(database.parlourDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun seedInitialData(dao: ParlourDao) {
            dao.insertServices(PrepopulatedData.initialServices)
            dao.insertServices(PrepopulatedData.initialBridalPackages)
            dao.insertStaffList(PrepopulatedData.initialStaff)
            dao.insertInventory(PrepopulatedData.initialInventory)
            dao.insertOffers(PrepopulatedData.initialOffers)
            dao.insertCustomers(PrepopulatedData.initialCustomers)
            dao.insertAppointments(PrepopulatedData.initialAppointments)
            dao.insertBills(PrepopulatedData.initialBills)
            dao.insertGalleryItems(PrepopulatedData.initialGallery)
            dao.saveParlourConfig(ParlourConfigEntity())
        }
    }
}
