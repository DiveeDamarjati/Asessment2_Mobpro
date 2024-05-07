package org.d3if0106.asessment2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if0106.asessment2.model.Tinggi

@Dao
interface TinggiDao {
    @Insert
    suspend fun insert(Tinggi : Tinggi)

    @Update
    suspend fun update(Tinggi: Tinggi)

    @Query("SELECT * FROM tinggi ORDER BY tanggal ASC")
    fun getData(): Flow<List<Tinggi>>

    @Query("SELECT * FROM tinggi WHERE id = :id")
    suspend fun getDataById(id:Long): Tinggi

    @Query("DELETE FROM tinggi WHERE id = :id")
    suspend fun deleteById(id: Long)
}