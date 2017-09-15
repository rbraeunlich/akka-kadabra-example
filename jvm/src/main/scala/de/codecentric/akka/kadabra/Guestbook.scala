package de.codecentric.akka.kadabra

import akka.actor.{Actor, Props}
import de.codecentric.akka.kadabra.Guestbook.{AddEntry, GetAll}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by ronny on 15.09.17.
  */
class Guestbook extends Actor {

  val entries: ArrayBuffer[Entry] = ArrayBuffer()

  override def receive: Receive = {

    case GetAll => sender ! entries

    case AddEntry(entry) => entries += entry

  }
}

object Guestbook {

  val props = Props[Guestbook]

  case object GetAll

  case class AddEntry(entry: Entry)

}
