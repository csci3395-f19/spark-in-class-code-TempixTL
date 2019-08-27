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
    println(s"${highestHigh.month}/${highestHigh.day}/${highestHigh.year}")

    val mostPrecip = data.maxBy(_.precipitation)
    println(s"${mostPrecip.month}/${mostPrecip.day}/${mostPrecip.year}")

    val daysWithRain = data.count(_.precipitation > 1)
    val percentDaysWithRain = (daysWithRain / data.length.toDouble) * 100
    println(f"$percentDaysWithRain%2.2f%%")
  }
}
