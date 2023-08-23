package com.example.ec_final_napapachasjohann.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ec_final_napapachasjohann.data.Converters
import com.example.ec_final_napapachasjohann.model.Products

@Database(entities = [Products::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ProductsDataBase: RoomDatabase(){
    abstract fun productsDao(): ProductsDao

    companion object{
        @Volatile
        private var instance: ProductsDataBase? = null
        fun getDataBase(context: Context): ProductsDataBase {
            if (instance == null){
                synchronized(this){
                    instance = buildDatabase(context)
                }
            }
            return instance!!
        }

        private fun buildDatabase(context: Context): ProductsDataBase? {
            return Room.databaseBuilder(
                context.applicationContext,
                ProductsDataBase::class.java,
                "products_database"
            ).build()
        }
    }
}