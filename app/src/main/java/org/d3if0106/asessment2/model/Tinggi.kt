package org.d3if0106.asessment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "tinggi")
class Tinggi (
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0L,
    val umur: String,
    val tinggi: String,
    val jenis_kelamin: String,
    val tanggal: String
)