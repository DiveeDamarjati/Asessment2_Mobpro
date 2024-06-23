package org.d3if0106.asessment2.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if0106.asessment2.R
import org.d3if0106.asessment2.database.TinggiDb
import org.d3if0106.asessment2.model.Tinggi
import org.d3if0106.asessment2.navigation.Screen
import org.d3if0106.asessment2.ui.theme.Asessment2Theme
import org.d3if0106.asessment2.util.SettingsDataStore
import org.d3if0106.asessment2.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController){
//    var showList by remember {
//        mutableStateOf(true)
//    }
    val dataStore = SettingsDataStore(LocalContext.current)
    val showList by dataStore.layoutFlow.collectAsState(true)
    Scaffold (
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStore.saveLayout(!showList)
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                if (showList) R.drawable.baseline_grid_view_24
                                else R.drawable.baseline_view_list_24
                            ),
                            contentDescription = stringResource(
                                if (showList) R.string.grid
                                else R.string.list
                            ),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.FormBaru.route)
                }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(id = R.string.tambah_data),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    ){
            padding -> ScreenContent(showList, Modifier.padding(padding), navController)
    }
}

@Composable
fun ScreenContent(showList: Boolean, modifier : Modifier, navController: NavHostController){
//    val viewModel: MainViewModel = viewModel()
//    val data = emptyList<Catatan>() <- apabila ingin data kosong
//    val data = viewModel.data

    var context = LocalContext.current
    val db = TinggiDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()


    if (data.isEmpty()){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Icon(
                painter = painterResource(
                    R.drawable.baseline_do_not_disturb_24
                ),
                contentDescription = ""
            )
            Text(text = stringResource(id = R.string.list_kosong))
        }
    }else{
        if (showList){
            LazyColumn (
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 84.dp)
            ){
                items(data){
                    ListItem(Tinggi = it){
                        navController.navigate(Screen.FormUbah.withId(it.id))
                    }
                    Divider()
                }
            }
        }
        else{
            LazyVerticalStaggeredGrid(
                modifier = modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp,8.dp,8.dp,84.dp)
            ){
                items(data){
                    GridItem(Tinggi = it){
                        navController.navigate(Screen.FormUbah.withId(it.id))
                    }
                    Divider()
                }
            }
        }
    }
}

@Composable
fun ListItem(Tinggi: Tinggi, onClick: () -> Unit){
    var context = LocalContext.current

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Text(text = stringResource(id = R.string.umur_MS, Tinggi.umur), maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Bold)
        Text(text = stringResource(id = R.string.tinggi_MS, Tinggi.tinggi), maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(text = stringResource(id = R.string.kategori, stringResource(id = hitung(Tinggi.tinggi, Tinggi.umur, Tinggi.jenis_kelamin))))
        Text(text = Tinggi.jenis_kelamin, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(text = Tinggi.tanggal, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Button(onClick = {
            shareData(
                context = context,
                message = context.getString(R.string.kirim_data, Tinggi.umur, Tinggi.tinggi, context.getString(
                    hitung(Tinggi.tinggi, Tinggi.umur, Tinggi.jenis_kelamin)
                ))
            )
        }
        ) {
            Text(text = stringResource(id = R.string.kirim))
        }
    }
}

@Composable
fun GridItem(Tinggi: Tinggi, onClick: () -> Unit){
    var context = LocalContext.current

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, Color.Gray)
    ){
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.umur_MS, Tinggi.umur),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = R.string.tinggi_MS, Tinggi.tinggi),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stringResource(
                    id = R.string.kategori,
                    stringResource(id = hitung(Tinggi.tinggi, Tinggi.umur, Tinggi.jenis_kelamin))
                )
            )
            Text(
                text = Tinggi.jenis_kelamin,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = Tinggi.tanggal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Button(onClick = {
                    shareData(
                        context = context,
                        message = context.getString(R.string.kirim_data, Tinggi.umur, Tinggi.tinggi, context.getString(
                            hitung(Tinggi.tinggi, Tinggi.umur, Tinggi.jenis_kelamin)
                        ))
                    )
            }
            ) {
                Text(text = stringResource(id = R.string.kirim))
            }
        }
    }
}
private fun shareData(context: Context, message: String){
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null){
        context.startActivity(shareIntent)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun GreetingPreview() {
    Asessment2Theme {
        MainScreen(rememberNavController())
    }
}