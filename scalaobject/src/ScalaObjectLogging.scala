import com.typesafe.scalalogging.StrictLogging
import net.logstash.logback.marker.Markers._

object ScalaObjectLogging extends App with StrictLogging {

  // List
  logger.info(append("list", List(1, 2, 3)), "Scala's List")

  // Map
  logger.info(append("map", Map(1 -> "one", 2 -> "two")), "Scala's Map")

  // case class
  logger.info(append("user", User(100, "t.kadowaki")), "Scala's case class")

  // Tuple
  logger.info(append("tuple", (1, "str", 'c', 2.34, List(5,6), Map("key" -> "value"))), "Scala's tuple")
}

case class User(id: Long, name: String)
