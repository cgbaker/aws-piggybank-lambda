package net.cgbaker.lambdas.piggybank

import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}

import scala.concurrent.duration._
import scala.language.postfixOps

class PiggyBankAppSpec(_system: ActorSystem)
  extends TestKit(_system)
  with Matchers
  with WordSpecLike
  with ImplicitSender
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("PiggyBankAppSpec"))

  override def afterAll: Unit = {
    shutdown(system)
  }

  "A PiggyBank Actor" should {

    "respond to non-existent account commands with None" in {
      val username = "unknown user"
      val pb = system.actorOf(Props(new PiggyBankActor))
      pb ! PiggyBankActor.QueryAccountRequest(username)
      expectMsg(500 millis, PiggyBankActor.QueryAccountResponse(username, None))
    }

    "create accounts on change events" in {
      val username = "some-user"
      val pb = system.actorOf(Props(new PiggyBankActor))
      pb ! PiggyBankActor.BalanceChangeRequest(username, 1.0)
      expectMsg(500 millis, PiggyBankActor.BalanceChangeResponse(username, Some(1.0)))
      pb ! PiggyBankActor.QueryAccountRequest(username)
      expectMsg(500 millis, PiggyBankActor.QueryAccountResponse(username, Some(1.0)))
    }

  }

}
