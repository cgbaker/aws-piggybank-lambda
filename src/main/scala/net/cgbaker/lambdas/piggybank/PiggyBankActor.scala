package net.cgbaker.lambdas.piggybank

import akka.persistence.{PersistentActor, SnapshotOffer}

class PiggyBankActor extends PersistentActor {

  import PiggyBankActor._

  var state = PiggyBankState()

  def updateState(evt: Event): Unit = {
    state = state.updated(evt)
  }

  override def persistenceId: String = "piggy-bank-actor"

  override def receiveRecover: Receive = {
    case evt: Event                                 => updateState(evt)
    case SnapshotOffer(_, snapshot: PiggyBankState) => state = snapshot
  }

  override def receiveCommand: Receive = {
    case BalanceChangeRequest(name, amount) =>
      val s = sender()
      persist(AccountBalanceChange(name, amount)) { event =>
        updateState(event)
        s ! BalanceChangeResponse(name, state.accounts.get(name))
      }
    case QueryAccountRequest(name) =>
      sender() ! QueryAccountResponse(name, state.accounts.get(name))
  }

}

case object PiggyBankActor {

  case class PiggyBankState(accounts: Map[String,Double] = Map.empty) {
    def updated(evt: Event): PiggyBankState = evt match {
      case AccountBalanceChange(username, amt) =>
        val newBalance = accounts.getOrElse(username, 0.0) + amt
        this.copy(
          accounts = accounts + (username -> newBalance)
        )
    }
  }

  sealed trait Event
  case class AccountBalanceChange(account: String, amount: Double) extends Event

  sealed trait Command
  case class QueryAccountRequest(account: String) extends Command
  case class QueryAccountResponse(account: String, amount: Option[Double]) extends Command
  case class BalanceChangeRequest(account: String, amount: Double) extends Command
  case class BalanceChangeResponse(account: String, amount: Option[Double]) extends Command

}
