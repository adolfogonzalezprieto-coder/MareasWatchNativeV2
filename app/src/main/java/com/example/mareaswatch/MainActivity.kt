package com.example.mareaswatch
import android.Manifest
import android.os.Bundle
import androidx.activity.*
import androidx.activity.compose.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.*
import com.example.mareaswatch.data.*
import com.example.mareaswatch.ui.*
class MainActivity:ComponentActivity(){override fun onCreate(b:Bundle?){super.onCreate(b);setContent{MareasApp()}}}
@Composable fun MareasApp(vm:MainViewModel= viewModel()){val state by vm.state.collectAsState();val permission=rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()){vm.refresh()};LaunchedEffect(Unit){permission.launch(Manifest.permission.ACCESS_FINE_LOCATION)};MaterialTheme{when(val s=state){UiState.WaitingGps->Wait("Buscando GPS...","Obteniendo ubicación actual");UiState.LoadingData->Wait("Cargando datos marinos...","");is UiState.Error->ScalingLazyColumn(horizontalAlignment=Alignment.CenterHorizontally){item{Text("Error",color=Color.Red)};item{Text(s.message,textAlign=TextAlign.Center,fontSize=10.sp)};item{Button(onClick=vm::refresh){Text("Reintentar")}}};is UiState.Ready->Dashboard(s.data,vm::refresh)}}}
@Composable fun Wait(title:String,sub:String)=Box(Modifier.fillMaxSize(),contentAlignment=Alignment.Center){Column(horizontalAlignment=Alignment.CenterHorizontally){CircularProgressIndicator();Spacer(Modifier.height(10.dp));Text(title,fontSize=13.sp,fontWeight=FontWeight.Bold);if(sub.isNotEmpty())Text(sub,fontSize=9.sp,color=Color.Gray,textAlign=TextAlign.Center)}}
@Composable fun Dashboard(d:MarineWeatherData,refresh:()->Unit){Scaffold(timeText={TimeText()},vignette={Vignette(VignettePosition.TopAndBottom)}){ScalingLazyColumn(Modifier.fillMaxSize(),horizontalAlignment=Alignment.CenterHorizontally){item{Text("Mareas Watch V2",fontSize=10.sp,fontWeight=FontWeight.Bold)};item{Text(d.locationName,fontSize=11.sp,color=Color.Cyan,textAlign=TextAlign.Center,maxLines=2)};item{Text("${"%.5f".format(d.latitude)}, ${"%.5f".format(d.longitude)}",fontSize=8.sp,color=Color.Gray)};item{Text("${"%.2f".format(d.tide.currentTideLevel)} m",fontSize=28.sp,fontWeight=FontWeight.Black)};item{Text("MAREA ESTIMADA · ${trend(d.tide.tideTrend)} · ${d.tide.tideProgress}%",fontSize=8.sp,color=Color.Yellow)};item{Metric("Tiempo","${"%.1f".format(d.temperature)} °C · UV ${"%.1f".format(d.uvIndex)}")};item{Metric("Viento","${"%.0f".format(d.windSpeed)} km/h · racha ${"%.0f".format(d.windGusts)}")};item{Metric("Oleaje","${"%.1f".format(d.waveHeight)} m · ${"%.0f".format(d.wavePeriod)} s")};d.tide.nextTides.take(4).forEach{e->item{Metric(if(e.type==TideType.HIGH)"Pleamar est." else "Bajamar est.","${e.time} · ${"%.2f".format(e.height)} m")}};item{CompactChip(onClick=refresh,label={Text("Actualizar GPS")})}}}}
@Composable fun Metric(a:String,b:String)=Chip(onClick={},label={Text(a,fontSize=11.sp)},secondaryLabel={Text(b,fontSize=9.sp)})
fun trend(t:TideTrend)=when(t){TideTrend.RISING->"SUBIENDO";TideTrend.FALLING->"BAJANDO";TideTrend.STATIONARY->"ESTABLE"}
