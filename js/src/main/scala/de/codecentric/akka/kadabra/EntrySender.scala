package de.codecentric.akka.kadabra

import akka.actor.{Actor, ActorLogging, Props}
import de.codecentric.akka.kadabra.EntrySender.NewEntry
import org.scalajs.dom.XMLHttpRequest
import org.scalajs.jquery.jQuery
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

/**
  * Created by ronny on 15.09.17.
  */
class EntrySender extends Actor with ActorLogging {
  override def receive: Receive = {
    case entry:NewEntry =>
      log.info("Sending guestbook entry")
      val request = new XMLHttpRequest()
      request.open("PUT", "http://localhost:8080/addEntry", async = false)
      request.setRequestHeader("Content-Type", "application/json")
      request.send(entry.asJson.toString())
  }
}

object EntrySender {
  val props = Props[EntrySender]

  case class NewEntry(author: String, text: String)

}
