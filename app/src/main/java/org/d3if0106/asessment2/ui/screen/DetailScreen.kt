package org.d3if0106.asessment2.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if0106.asessment2.R
import org.d3if0106.asessment2.database.TinggiDb
import org.d3if0106.asessment2.ui.theme.Asessment2Theme
import org.d3if0106.asessment2.util.ViewModelFactory

const val KEY_ID = "idMahasiswa"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null){
    val context = LocalContext.current
    val db = TinggiDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var tinggi by remember {
        mutableStateOf("")
    }
    var umur by remember {
        mutableStateOf("")
    }
    var jenis_kelamin by remember {
        mutableStateOf("")
    }
    var kategori by remember {
        mutableIntStateOf(0)
    }
    var kondisi by remember {
        mutableStateOf(false)
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(true){
        if (id == null) return@LaunchedEffect

        val data = viewModel.getData(id) ?: return@LaunchedEffect
        tinggi = data.tinggi
        umur = data.umur
        jenis_kelamin = data.jenis_kelamin
        kategori = hitung(tinggi, umur, jenis_kelamin)
        kondisi = true

    }
    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null) Text(text = stringResource(id = R.string.tambah_data))
                    else Text(text = stringResource(id = R.string.edit_BMI))
                },

                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(
                        onClick = {
                            if (umur.equals("") || tinggi.equals("") || jenis_kelamin.equals("")) {
                                Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                                return@IconButton
                            }
                            if (id == null) viewModel.insert(umur, tinggi, jenis_kelamin)
                            else viewModel.update(id, umur, tinggi, jenis_kelamin)
                            navController.popBackStack()
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(id = R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null){
                        DeleteById {
                            showDialog = true
                        }
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false }
                        ) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        },
    ){
            padding ->
        FormTinggi(
            tinggi = tinggi,
            onTinggiChange = { tinggi = it },
            umur =  umur,
            onUmurTinggi = { umur = it},
            jenis_Kelamin = jenis_kelamin,
            onJenisKelaminOnChange = { jenis_kelamin = it},
            kategori = kategori,
            kondisi = kondisi,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun FormTinggi(
    tinggi: String, onTinggiChange: (String) -> Unit,
    umur: String, onUmurTinggi: (String) -> Unit,
    jenis_Kelamin: String, onJenisKelaminOnChange: (String) -> Unit,
    kategori: Int,
    kondisi: Boolean,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = umur,
            onValueChange = { onUmurTinggi(it) },
            label = { Text(text = stringResource(id = R.string.umur)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = tinggi,
            onValueChange = { onTinggiChange(it) },
            label = { Text(text = stringResource(id = R.string.tinggi)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Column (
            modifier = Modifier.fillMaxWidth()
        ){
            Text(text = stringResource(id = R.string.list_jeniskelamin))
            listOf("Laki-Laki", "Perempuan").forEach { jenisKelaminOpsi ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        onJenisKelaminOnChange(jenisKelaminOpsi)
                    }
                ) {
                    RadioButton(
                        selected = jenis_Kelamin == jenisKelaminOpsi,
                        onClick = {
                            onJenisKelaminOnChange(jenisKelaminOpsi)
                        }
                    )
                    Text(text = jenisKelaminOpsi)
                }
            }
        }
        if (kondisi) {
            Divider()
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.kategori, stringResource(id = kategori)),
                    style = MaterialTheme.typography.headlineLarge)
            }
        }
    }
}
@Composable
fun DeleteById(delete: () -> Unit){
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { expanded = true }) {
        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = stringResource(id = R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text(text = stringResource(id = R.string.hapus)) }, onClick = {
                expanded = false
                delete() })
        }
    }
}

fun hitung(tinggi: String, umur: String, jenisKelamin: String): Int {
    val tinggiFloat = tinggi.toFloat()
    val umurFloat = umur.toFloat()

    return if (jenisKelamin.equals("Perempuan", ignoreCase = true)) {
        if (umurFloat in 10.0..12.0) {
            if (tinggiFloat < 147) R.string.pendek
            else if (tinggiFloat <= 156) R.string.ideal
            else R.string.tinggi
        } else if (umurFloat in 13.0 .. 15.0){
            if (tinggiFloat < 156) R.string.pendek
            else if (tinggiFloat <= 159) R.string.ideal
            else R.string.tinggi
        } else if(umurFloat in 16.0 .. 29.0){
            if (tinggiFloat < 159) R.string.pendek
            else if (tinggiFloat <= 162) R.string.ideal
            else R.string.tinggi
        } else if(umurFloat in 30.0 .. 64.0){
            if (tinggiFloat < 158) R.string.pendek
            else if (tinggiFloat <= 161) R.string.ideal
            else R.string.tinggi
        } else{
            if (tinggiFloat < 157) R.string.pendek
            else if (tinggiFloat <= 160 ) R.string.ideal
            else R.string.tinggi
        }
    } else {
        if (umurFloat in 10.0 .. 12.0){
            if (tinggiFloat < 145) R.string.pendek
            else if(tinggiFloat <= 148) R.string.ideal
            else R.string.tinggi
        } else if(umurFloat in 13.0 .. 15.0){
            if (tinggiFloat < 163) R.string.pendek
            else if (tinggiFloat <= 166) R.string.ideal
            else R.string.tinggi
        } else if(umurFloat in 16.0 .. 29.0){
            if (tinggiFloat < 168) R.string.pendek
            else if (tinggiFloat <= 171) R.string.ideal
            else R.string.tinggi
        }else if (umurFloat in 30.0 .. 64.0){
            if (tinggiFloat < 166) R.string.pendek
            else if (tinggiFloat <= 169) R.string.ideal
            else R.string.tinggi
        } else {
            if (tinggiFloat < 164) R.string.pendek
            else if (tinggiFloat <= 167) R.string.ideal
            else R.string.tinggi
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    Asessment2Theme {
        DetailScreen(rememberNavController())
    }
}
