import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.ExecutionContext.Implicits.global

object PlayWSLogging extends App with StrictLogging {

  implicit val system: ActorSystem = ActorSystem()
  system.registerOnTermination {
    System.exit(0)
  }
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val ws = StandaloneAhcWSClient()

  val resultF = ws.url("https://www.google.com").get()

  resultF
    .andThen { case _ => ws.close() }
    .andThen { case _ => system.terminate() }

  logger.info("fooo")
}
