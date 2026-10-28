package com.example.lab10.view
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lab10.data.SerieApiService
import com.example.lab10.data.SerieModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ContenidoSeriesListado(navController: NavHostController, servicio: SerieApiService) {
    var listaSeries: List<SerieModel> by remember { mutableStateOf(emptyList()) }

    LaunchedEffect(Unit) {
        val listado = servicio.selectSeries()
        listaSeries = listado
    }

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("ID", Modifier.weight(0.1f), fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Text("SERIE", Modifier.weight(0.7f), fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Text("Acción", Modifier.weight(0.2f), fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
        }

        items(listaSeries) { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = item.id.toString(), modifier = Modifier.weight(0.1f), textAlign = TextAlign.End)
                Text(text = item.name, modifier = Modifier.weight(0.6f).padding(start = 8.dp))
                IconButton(
                    onClick = { navController.navigate("serieVer/${item.id}") },
                    modifier = Modifier.weight(0.1f)
                ) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Editar")
                }
                IconButton(
                    onClick = { navController.navigate("serieDel/${item.id}") },
                    modifier = Modifier.weight(0.1f)
                ) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}

@Composable
fun ContenidoSerieEditar(navController: NavHostController, servicio: SerieApiService, pid: Int = 0) {
    var id by remember { mutableStateOf<Int>(pid) }
    var name by remember { mutableStateOf<String?>("") }
    var release_date by remember { mutableStateOf<String?>("") }
    var rating by remember { mutableStateOf<String?>("") }
    var category by remember { mutableStateOf<String?>("") }
    var grabar by remember { mutableStateOf(false) }

    if (id != 0) {
        LaunchedEffect(Unit) {
            val objSerie = servicio.selectSerie(id.toString())
            delay(100)
            name = objSerie.body()?.name
            release_date = objSerie.body()?.release_date
            rating = objSerie.body()?.rating.toString()
            category = objSerie.body()?.category
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = id.toString(),
            onValueChange = { },
            label = { Text("ID (solo lectura)") },
            readOnly = true,
            singleLine = true
        )
        TextField(
            value = name!!,
            onValueChange = { name = it },
            label = { Text("Name: ") },
            singleLine = true
        )
        TextField(
            value = release_date!!,
            onValueChange = { release_date = it },
            label = { Text("Release Date:") },
            singleLine = true
        )
        TextField(
            value = rating!!,
            onValueChange = { rating = it },
            label = { Text("Rating:") },
            singleLine = true
        )
        TextField(
            value = category!!,
            onValueChange = { category = it },
            label = { Text("Category:") },
            singleLine = true
        )
        Button(
            onClick = {
                grabar = true
            }
        ) {
            Text("Grabar", fontSize = 16.sp)
        }
    }

    if (grabar) {
        val objSerie = SerieModel(id, name!!, release_date!!, rating!!.toInt(), category!!)
        LaunchedEffect(Unit) {
            if (id == 0)
                servicio.insertSerie(objSerie)
            else
                servicio.updateSerie(id.toString(), objSerie)
        }
        grabar = false
        navController.navigate("series")
    }
}