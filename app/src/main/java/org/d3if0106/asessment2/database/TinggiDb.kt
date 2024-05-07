package org.d3if0106.asessment2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if0106.asessment2.model.Tinggi

@Database(entities = [Tinggi::class], version = 1, exportSchema = false)
abstract class TinggiDb : RoomDatabase() {
    abstract val dao: TinggiDao
    companion object {
        @Volatile
        private var INSTACE: TinggiDb? = null
        fun getInstance(context: Context): TinggiDb{
            synchronized(this){
                var instance = INSTACE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TinggiDb::class.java,
                        "bmi.db"
                    ).build()
                    INSTACE = instance
                }
                return instance
            }
        }
    }
}