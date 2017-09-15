package de.codecentric.akka.kadabra

import akka.actor.{Actor, ActorLogging, Props}
import de.codecentric.akka.kadabra.EntryRenderer.RetrieveEntries
import org.scalajs.dom._
import org.scalajs.jquery.jQuery

/**
  * Created by ronny on 15.09.17.
  */
class EntryRenderer extends Actor with ActorLogging {
  override def receive: Receive = {
    case RetrieveEntries =>
      log.info("Retrieving guestbook entries")
      val request = new XMLHttpRequest()
      request.onreadystatechange = _ => {
        if (request.readyState == 4 && request.status == 200) {
          log.info(s"Received answer from server")
          jQuery("#entries").remove()
          jQuery("body").append(s"""<div id="entries">${request.responseText}</div>""")
        }
      }
      request.open("GET", "http://localhost:8080/all", async = true)
      request.send()
  }
}

object EntryRenderer {

  val props = Props[EntryRenderer]

  case object RetrieveEntries

}
