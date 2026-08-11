package com.example.mareaswatch.data

enum class TideType { HIGH, LOW }
enum class TideTrend { RISING, FALLING, STATIONARY }
data class TideEvent(val time:String,val type:TideType,val height:Double,val coefficient:Int)
data class MoonPhaseInfo(val name:String,val illumination:Int,val coefficient:Int,val isSpringTide:Boolean)
data class TideInfo(val currentTideLevel:Double,val tideTrend:TideTrend,val tideProgress:Int,val nextTides:List<TideEvent>,val moonPhase:MoonPhaseInfo)
data class MarineWeatherData(val latitude:Double,val longitude:Double,val locationName:String,val temperature:Double,val apparentTemp:Double,val windSpeed:Double,val windDirection:Double,val windGusts:Double,val pressure:Double,val humidity:Int,val uvIndex:Double,val precipitationProb:Int,val waveHeight:Double,val wavePeriod:Double,val waveDirection:Double,val swellHeight:Double,val sunrise:String,val sunset:String,val tide:TideInfo)
data class WeatherResponse(val current:WeatherCurrent?,val hourly:WeatherHourly?,val daily:WeatherDaily?)
data class WeatherCurrent(val temperature_2m:Double?,val relative_humidity_2m:Int?,val apparent_temperature:Double?,val surface_pressure:Double?,val wind_speed_10m:Double?,val wind_direction_10m:Double?,val wind_gusts_10m:Double?,val uv_index:Double?)
data class WeatherHourly(val precipitation_probability:List<Int>?)
data class WeatherDaily(val sunrise:List<String>?,val sunset:List<String>?)
data class MarineResponse(val current:MarineCurrent?)
data class MarineCurrent(val wave_height:Double?,val wave_direction:Double?,val wave_period:Double?,val swell_wave_height:Double?)
data class NominatimResponse(val address:NominatimAddress?)
data class NominatimAddress(val amenity:String?,val suburb:String?,val town:String?,val city:String?,val municipality:String?,val county:String?,val state:String?,val country:String?)
