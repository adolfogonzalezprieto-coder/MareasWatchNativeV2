package com.example.mareaswatch.data
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
interface WeatherApi { @GET("v1/forecast") suspend fun forecast(@Query("latitude")lat:Double,@Query("longitude")lon:Double,@Query("current")current:String="temperature_2m,relative_humidity_2m,apparent_temperature,surface_pressure,wind_speed_10m,wind_direction_10m,wind_gusts_10m,uv_index",@Query("hourly")hourly:String="precipitation_probability",@Query("daily")daily:String="sunrise,sunset",@Query("timezone")timezone:String="auto"):WeatherResponse }
interface MarineApi { @GET("v1/marine") suspend fun marine(@Query("latitude")lat:Double,@Query("longitude")lon:Double,@Query("current")current:String="wave_height,wave_direction,wave_period,swell_wave_height",@Query("timezone")timezone:String="auto"):MarineResponse }
interface GeoApi { @GET("reverse") suspend fun reverse(@Query("format")format:String="json",@Query("lat")lat:Double,@Query("lon")lon:Double,@Query("zoom")zoom:Int=12):NominatimResponse }
object ApiProvider { private val c=OkHttpClient.Builder().addInterceptor{ch->ch.proceed(ch.request().newBuilder().header("User-Agent","MareasWatchNativeV2/2.0").build())}.build(); private fun r(u:String)=Retrofit.Builder().baseUrl(u).client(c).addConverterFactory(GsonConverterFactory.create()).build(); val weather=r("https://api.open-meteo.com/").create(WeatherApi::class.java); val marine=r("https://marine-api.open-meteo.com/").create(MarineApi::class.java); val geo=r("https://nominatim.openstreetmap.org/").create(GeoApi::class.java) }
