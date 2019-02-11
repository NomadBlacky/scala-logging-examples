import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import net.logstash.logback.marker.Markers
import org.slf4j.Marker
import play.api.libs.ws.StandaloneWSResponse
import play.api.libs.ws.ahc.{StandaloneAhcWSClient, StandaloneAhcWSRequest}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object PlayWSLogging extends App with StrictLogging with WSResponseMarkerGen {

  implicit val system: ActorSystem = ActorSystem()
  system.registerOnTermination {
    System.exit(0)
  }
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val ws = StandaloneAhcWSClient()

  val request = StandaloneAhcWSRequest(ws, "https://www.google.com")

  logger.info(Markers.append("http_request", request), "Execute HTTP request")

  val resultF = request.execute()

  resultF
    .andThen {
      case Success(response) =>
        logger.info(genMarker(response), "Received HTTP response")
      case Failure(exception) =>
        logger.error("Failed HTTP request", exception)
    }
    .andThen { case _ => ws.close() }
    .andThen { case _ => system.terminate() }
}

trait WSResponseMarkerGen {
  def genMarker(response: StandaloneWSResponse): Marker = {
    Markers.append("http_response", Map(
      "status" -> response.status,
      "headers" -> response.headers,
      "cookies" -> response.cookies,
      "body" -> response.body
    ))
  }
}
