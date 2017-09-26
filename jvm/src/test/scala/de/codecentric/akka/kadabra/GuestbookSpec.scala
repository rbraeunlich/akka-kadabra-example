package de.codecentric.akka.kadabra

import akka.actor.{ActorRef, ActorSystem, PoisonPill, Props, Terminated}
import akka.testkit.TestProbe
import de.codecentric.akka.kadabra.Guestbook.{AddEntry, GetAll}
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}

/**
  * Created by ronny on 15.09.17.
  */
class GuestbookSpec extends WordSpec with Matchers with PersistenceCleanup with BeforeAndAfter {

  implicit val actorSystem = ActorSystem("test")

  before {
    deleteStorageLocations(actorSystem)
  }

  "The Guestbook" should {

    "save entries" in {
      val sender = TestProbe()
      implicit val senderRef = sender.ref
      val guestbook = actorSystem.actorOf(Props(new Guestbook("1")))
      val newEntry = Entry("author", "This is the best guestbook eva! <3")

      guestbook ! AddEntry(newEntry)

      guestbook ! GetAll
      sender.expectMsg(List(newEntry))
    }

    "return all entries" in {
      val sender = TestProbe()
      implicit val senderRef = sender.ref
      val guestbook = actorSystem.actorOf(Props(new Guestbook("2")))
      val newEntry = Entry("author1", "text1")
      guestbook ! AddEntry(newEntry)
      guestbook ! AddEntry(newEntry)

      guestbook ! GetAll

      sender.expectMsg(List(newEntry, newEntry))
    }

    "should save the entries" in {
      val sender = TestProbe()
      implicit val senderRef = sender.ref
      var guestbook = actorSystem.actorOf(Props(new Guestbook("3")))
      val newEntry = Entry("author", "This entry will stay forever")
      guestbook ! AddEntry(newEntry)
      // wait until the message arrived
      guestbook ! GetAll
      sender.expectMsg(List(newEntry))

      //kill the guestbook
      sender.watch(guestbook)
      guestbook ! PoisonPill
      sender.expectMsgClass(classOf[Terminated])

      //create the Guestbook again
      guestbook = actorSystem.actorOf(Props(new Guestbook("3")))
      guestbook ! GetAll

      sender.expectMsg(List(newEntry))
    }
  }
}
