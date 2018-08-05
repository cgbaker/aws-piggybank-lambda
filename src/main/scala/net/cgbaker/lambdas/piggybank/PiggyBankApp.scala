package net.cgbaker.lambdas.piggybank

import akka.actor.{ActorRef, ActorSystem, Props}

object PiggyBankApp extends App {

  val system = ActorSystem("piggyBank")

  val piggyBank: ActorRef = system.actorOf(Props(new PiggyBankActor), "piggyBankActor")

}
