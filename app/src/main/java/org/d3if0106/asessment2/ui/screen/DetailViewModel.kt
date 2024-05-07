package org.d3if0106.asessment2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if0106.asessment2.database.TinggiDao
import org.d3if0106.asessment2.model.Tinggi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DetailViewModel(private val dao: TinggiDao) : ViewModel(){
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        fun insert(umur: String, tinggi: String, jenis_kelamin: String){
            val Tinggi = Tinggi(
                umur = umur,
                tinggi = tinggi,
                jenis_kelamin = jenis_kelamin,
                tanggal = formatter.format(Date())
            )

            viewModelScope.launch(Dispatchers.IO) { dao.insert(Tinggi) }
        }

        suspend fun getData(id: Long): Tinggi? {
            return dao.getDataById(id)
        }
        fun update(id: Long, umur: String, tinggi: String, jenis_kelamin: String){
            val Tinggi = Tinggi(
                id = id,
                umur = umur,
                tinggi = tinggi,
                jenis_kelamin = jenis_kelamin,
                tanggal = formatter.format(Date())
            )

            viewModelScope.launch(Dispatchers.IO) { dao.update(Tinggi) }
        }

        fun delete(id: Long){
            viewModelScope.launch(Dispatchers.IO) { dao.deleteById(id) }
        }
    }
