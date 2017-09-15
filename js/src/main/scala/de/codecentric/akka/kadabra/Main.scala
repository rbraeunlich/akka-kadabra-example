package de.codecentric.akka.kadabra

import akka.actor.ActorSystem
import de.codecentric.akka.kadabra.EntryRenderer.RetrieveEntries
import de.codecentric.akka.kadabra.EntrySender.NewEntry
import org.scalajs.dom.raw.Event
import org.scalajs.jquery.{JQueryEventObject, jQuery}

import scala.scalajs.js.annotation.JSExport

/**
  * Created by ronny on 15.09.17.
  */
object Main {
  val actorSystem = ActorSystem("akka-kadabra-client")

  private val entryRenderer = actorSystem.actorOf(EntryRenderer.props)
  private val entrySender = actorSystem.actorOf(EntrySender.props)

  @JSExport
  def main(args: Array[String]): Unit = {
    jQuery("body").append("<h1>✨Akka-kadabra✨</h1>")
    renderInputElements()
    val retrieveDiv = jQuery("<div>").appendTo(jQuery("body"))
    jQuery("<button type=\"button\" id=\"retrieve-button\">Get guestbook entries</button>")
      .click(() => {
        entryRenderer ! RetrieveEntries
      }).appendTo(retrieveDiv)
    jQuery("body").append("<br>")
  }

  private def renderInputElements() = {
    val inputDiv = jQuery("<div>").appendTo(jQuery("body"))
    inputDiv.append("""Author:<input name="author" id="author" type="text"/>""")
    inputDiv.append("""Text:<input name="text" id="text" type="text" />""")
    jQuery("<button type=\"button\" id=\"submit-button\">Submit!</button>")
      .click(() => {
        val author = jQuery("#author").value().toString
        val text = jQuery("#text").value().toString
        entrySender ! NewEntry(author, text)
        entryRenderer ! RetrieveEntries
      }).appendTo(inputDiv)
    jQuery("body").append("<br>")
  }
}
