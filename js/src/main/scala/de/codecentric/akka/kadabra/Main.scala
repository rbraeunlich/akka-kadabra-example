package de.codecentric.akka.kadabra
import org.scalajs.jquery.jQuery

import scala.scalajs.js.annotation.JSExport
/**
  * Created by ronny on 15.09.17.
  */
object Main {

  @JSExport
  def main(args: Array[String]): Unit = {
    println("foo")
    jQuery("body").append("<p>Works!</p>")
  }

}
