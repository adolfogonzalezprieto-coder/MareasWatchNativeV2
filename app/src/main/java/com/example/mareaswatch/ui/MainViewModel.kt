package com.example.mareaswatch.ui
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.example.mareaswatch.data.*
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
sealed interface UiState { data object WaitingGps:UiState; data object LoadingData:UiState; data class Ready(val data:MarineWeatherData):UiState; data class Error(val message:String):UiState }
class MainViewModel(app:Application):AndroidViewModel(app){ private val repo=MarineRepository();private val gps=LocationServices.getFusedLocationProviderClient(app);private val _state=MutableStateFlow<UiState>(UiState.WaitingGps);val state:StateFlow<UiState> = _state
 fun refresh()=viewModelScope.launch { val ok=ContextCompat.checkSelfPermission(getApplication(),android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED;if(!ok){_state.value=UiState.Error("Permiso de ubicación no concedido");return@launch};_state.value=UiState.WaitingGps;try{val req=CurrentLocationRequest.Builder().setPriority(Priority.PRIORITY_HIGH_ACCURACY).setDurationMillis(20000).setMaxUpdateAgeMillis(0).build();val loc=gps.getCurrentLocation(req,CancellationTokenSource().token).await();if(loc==null){_state.value=UiState.Error("No se pudo obtener la posición GPS. Activa la ubicación y prueba al aire libre.");return@launch};_state.value=UiState.LoadingData;_state.value=UiState.Ready(repo.load(loc.latitude,loc.longitude))}catch(e:Exception){_state.value=UiState.Error(e.message?:"No se pudo obtener la ubicación actual")}}
}
