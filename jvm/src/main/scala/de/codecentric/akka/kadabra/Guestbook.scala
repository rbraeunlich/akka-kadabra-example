package de.codecentric.akka.kadabra

import akka.actor.{Actor, ActorLogging, Props}
import akka.persistence.{PersistentActor, SnapshotOffer}
import de.codecentric.akka.kadabra.Guestbook.{AddEntry, GetAll}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by ronny on 15.09.17.
  */
class Guestbook(id: String) extends PersistentActor with ActorLogging {

  override def persistenceId: String = id

  val entries: ArrayBuffer[Entry] = ArrayBuffer()

  def updateState(add: AddEntry): Unit = entries += add.entry

  override def receiveRecover: Receive = {
    case add: AddEntry =>
      log.info(s"Recovered event $add")
      updateState(add)
    case SnapshotOffer(_, snapshot: ArrayBuffer[Entry]) =>
      log.info(s"Restored snapshot $snapshot")
      entries ++= snapshot
  }

  override def receiveCommand: Receive = {
    case GetAll => sender ! entries

    case add: AddEntry =>
      persist(add) { event =>
        log.info(s"Applying event $event. State: $entries")
        updateState(event)
        if (entries.nonEmpty && entries.length % 100 == 0) {
          saveSnapshot(entries)
        }
      }
  }
}

object Guestbook {

  val persistentId: String = "guestbook"

  val props = Props(new Guestbook(persistentId))

  case object GetAll

  case class AddEntry(entry: Entry)

}
