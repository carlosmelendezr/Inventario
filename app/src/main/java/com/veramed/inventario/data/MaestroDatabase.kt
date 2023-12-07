package com.veramed.inventario.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import java.io.File

/**
 * Database class with a singleton INSTANCE object.
 */
@Database(entities = [Item::class],
    version = 7, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MaestroDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao


    companion object {
        @Volatile
        private var Instance: MaestroDatabase? = null

        fun getDatabase(context: Context): MaestroDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MaestroDatabase::class.java, "maestro_database")
                    /**
                     * Setting this option in your app's database builder means that Room
                     * permanently deletes all data from the tables in your database when it
                     * attempts to perform a migration with no defined migration path.
                     */
                    .createFromAsset("database/inven_maestro1.db")
                    .createFromAsset("database/inven_maestro2.db")
                    .createFromAsset("database/inven_maestro3.db")
                    .createFromAsset("database/inven_maestro4.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}