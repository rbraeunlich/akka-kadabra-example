import org.scalajs.core.tools.linker.ModuleInitializer
import sbt.Keys.libraryDependencies


enablePlugins(ScalaJSPlugin)

scalaVersion := "2.12.1"

lazy val root = project.in(file("."))
  .aggregate(kadabraJS, kadabraJVM)

lazy val kadabra = crossProject.in(file(".")).
  settings(
    name := "akka-kadabra-example",
    scalaVersion := "2.12.1",
    version := "1.0"
  ).
  jvmSettings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.5.4",
      "com.typesafe.akka" %% "akka-testkit" % "2.5.4" % Test,
      "com.typesafe.akka" %% "akka-stream" % "2.5.4",
      "com.typesafe.akka" %% "akka-http" % "10.0.10",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % Test,
      "de.heikoseeberger" %% "akka-http-circe" % "1.19.0-M2",
      "io.circe" %% "circe-generic" % "0.9.0-M1",
      "io.circe" %% "circe-parser" % "0.9.0-M1"
    )
  ).jsSettings(
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.1",
    "be.doeraene" %%% "scalajs-jquery" % "0.9.1",
    "org.akka-js" %%% "akkajsactor" % "1.2.5.4",
    "org.akka-js" %%% "akkajsactorstream" % "1.2.5.4",
    "io.circe" %%% "circe-core" % "0.9.0-M1",
    "io.circe" %%% "circe-generic" % "0.9.0-M1",
    "io.circe" %%% "circe-parser" % "0.9.0-M1"
  ),
  scalaJSUseMainModuleInitializer := true,
  skip in packageJSDependencies := false,
  jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),
  jsDependencies +=
    "org.webjars" % "jquery" % "2.1.4" / "2.1.4/jquery.js"
)

lazy val kadabraJS = kadabra.js
lazy val kadabraJVM = kadabra.jvm.settings(
  (resources in Compile) += (fastOptJS in(kadabraJS, Compile)).value.data,
  (resources in Compile) += (packageJSDependencies in(kadabraJS, Compile)).value
)

