package com.example.mareaswatch.data
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.math.*
object TideCalculator {
 fun generate(lat:Double,lon:Double,date:Date=Date()):TideInfo {
  val ref=Instant.parse("2024-01-11T11:57:00Z").toEpochMilli(); val syn=29.53058867*86400000.0
  var phase=((date.time-ref)%syn)/syn; if(phase<0)phase+=1.0
  val illum=(((1-cos(phase*2*Math.PI))/2)*100).roundToInt(); val coeff=(35+abs(cos(phase*2*Math.PI))*75).roundToInt()
  val name=when { phase<.03||phase>.97->"Luna Nueva"; phase<.28->"Creciente"; phase<.53->"Luna Llena"; phase<.78->"Menguante"; else->"Menguante" }
  val moon=MoonPhaseInfo(name,illum,coeff,coeff>=75); val mean=1.8; val amp=1.4*(coeff/70.0); val period=(12*3600+25*60+12)*1000L
  val zone=ZoneId.systemDefault(); val start=date.toInstant().atZone(zone).toLocalDate().atStartOfDay(zone).toInstant().toEpochMilli(); val shift=3*3600000L+((lon/360.0)*86400000).toLong()
  fun h(t:Long):Double { val r=((t-start+shift).toDouble()/period)*2*Math.PI; return max(.1,(100*(mean+amp*(sin(r)+.25*sin(2*r+.5)))).roundToInt()/100.0) }
  val now=date.time; val current=h(now); val future=h(now+600000); val trend=if(future>current+.01)TideTrend.RISING else if(future<current-.01)TideTrend.FALLING else TideTrend.STATIONARY
  val events=mutableListOf<TideEvent>(); var prev=h(now-900000); var t=now; val fmt=DateTimeFormatter.ofPattern("HH:mm")
  while(t<=now+36*3600000L&&events.size<4){ val v=h(t); val n=h(t+600000); val ty=if(v>prev&&v>=n&&v>mean+.2)TideType.HIGH else if(v<prev&&v<=n&&v<mean-.2)TideType.LOW else null; if(ty!=null)events+=TideEvent(Instant.ofEpochMilli(t).atZone(zone).format(fmt),ty,v,coeff); prev=v;t+=600000 }
  val progress=(((current-(mean-amp))/(2*amp))*100).roundToInt().coerceIn(0,100)
  return TideInfo(current,trend,progress,events,moon)
 }
}
