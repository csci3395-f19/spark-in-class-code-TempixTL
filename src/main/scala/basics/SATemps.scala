package basics

case class TempRow(day: Int, doy: Int, month: Int, year: Int, precipitation: Double, avgTemp: Double, hiTemp: Double, loTemp: Double)

object SATemps {
  def parseLine(line: String): TempRow = {
    val p = line.split(",")
    TempRow(p(0).toInt, p(1).toInt, p(2).toInt, p(4).toInt, p(5).toDouble, p(6).toDouble, p(7).toDouble, p(8).toDouble)
  }

  def main(args: Array[String]): Unit = {
    val source = scala.io.Source.fromFile("/users/mlewis/CSCI3395-F19/InClassBD/data/SanAntonioTemps.csv")
    val lines = source.getLines()
    val data = lines.drop(2).map(parseLine).toArray
    
    val highestHigh = data.maxBy(_.hiTemp)
    println(s"Date of highest temperature: ${highestHigh.month}/${highestHigh.day}/${highestHigh.year}")

    val mostPrecip = data.maxBy(_.precipitation)
    println(s"Date of highest precipitation: ${mostPrecip.month}/${mostPrecip.day}/${mostPrecip.year}")

    val daysWithRain = data.count(_.precipitation > 1)
    val percentDaysWithRain = (daysWithRain / data.length.toDouble) * 100
    println(f"Percent of days with rain: $percentDaysWithRain%2.2f%%")

    val rainyDays = data.filter(_.precipitation >= 1.0)
    val avgHiForRainy = rainyDays.foldLeft(0.0)(_ + _.precipitation) / rainyDays.length
    // Calculate without filter
    //
    // val (rainySum, rainyCount) = data.foldLeft((0.0, 0)) { case ((sum, cnt), day) =>
    //   if (day.precipitation >= 1) (sum + day.precipitation, cnt + 1) else (sum, cnt)
    // }
    // val avgHiForRainy = rainySum / rainyCount
    println(s"Average hi temp for rainy days: $avgHiForRainy")

    val months = data.groupBy(_.month)

    val avgHiTempsPerMonth = months.mapValues(temps => temps.foldLeft(0.0)(_ + _.precipitation) / temps.length)
    println("Average hi temps per month:")
    (1 to 12).foreach((month) => println(s"Month $month: ${avgHiTempsPerMonth(month)}"))

    val avgPrecipPerMonth = months.mapValues((temps) => temps.map(_.precipitation).sum / temps.length)
    println("Average precipitation per month:")
    (1 to 12).foreach((month) => println(s"Month $month: ${avgPrecipPerMonth(month)}"))

    val precipPerMonth = months.mapValues((temps) => temps.map(_.precipitation).sorted)
    val medianPrecipPerMonth = precipPerMonth.mapValues((precip) => precip(precip.length / 2))
    println("Median precipitation per month:")
    (1 to 12).foreach((month) => println(s"Month $month: ${medianPrecipPerMonth(month)}"))
  }
}
