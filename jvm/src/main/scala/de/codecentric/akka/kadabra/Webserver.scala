package de.codecentric.akka.kadabra

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.util.Timeout
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import de.codecentric.akka.kadabra.Guestbook.{AddEntry, GetAll}
import de.heikoseeberger.akkahttpcirce.{BaseCirceSupport, FailFastCirceSupport}
import akka.pattern.{ask, pipe}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.io.StdIn

/**
  * Created by ronny on 15.09.17.
  */
object Webserver extends FailFastCirceSupport {

  implicit val timeout: Timeout = 5.seconds

  def main(args: Array[String]): Unit = {
    import io.circe.generic.auto._

    implicit val system = ActorSystem("akka-kadabra")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    implicit val guestbook = system.actorOf(Guestbook.props, Guestbook.persistentId)

    val route = path("all") {
      get {
        val allEntries: Future[List[Entry]] = (guestbook ? GetAll).mapTo[List[Entry]]
        complete(allEntries)
      }
    } ~
      path("addEntry") {
        put {
          entity(as[Entry]) { entry =>
            guestbook ! AddEntry(entry)
            complete("created")
          }
        }

      } ~
    path("index") {
      getFromResource("index.html")
    } ~
      getFromResourceDirectory("")

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ ⇒ system.terminate()) // and shutdown when done
  }
}
