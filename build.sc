import mill._
import mill.scalalib._
import mill.scalalib.scalafmt._

trait CommonModule extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.12.8"
  def ivyDeps = Agg(
    ivy"ch.qos.logback:logback-classic:1.2.3",
    ivy"net.logstash.logback:logstash-logback-encoder:5.3",
    ivy"com.typesafe.scala-logging::scala-logging:3.9.2"
  )

  object test extends TestModule {
    def ivyDeps = Agg(ivy"org.scalatest::scalatest:3.0.5")
    def testFrameworks = Seq("org.scalatest.tools.Framework")
  }
}

object playws extends CommonModule {
  override def ivyDeps = super.ivyDeps() ++ Agg(
    ivy"com.typesafe.play::play-ahc-ws-standalone:2.0.1",
    ivy"com.fasterxml.jackson.module::jackson-module-scala:2.9.8"
  )
}
