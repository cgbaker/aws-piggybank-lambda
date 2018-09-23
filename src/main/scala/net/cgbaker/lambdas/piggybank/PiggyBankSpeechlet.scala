package net.cgbaker.lambdas.piggybank

import akka.actor.{ActorSystem, Props}
import com.amazon.speech.speechlet._
import com.amazon.speech.ui.{OutputSpeech, PlainTextOutputSpeech, Reprompt}

class PiggyBankSpeechlet extends Speechlet {

  import PiggyBankSpeechlet._

  val system = ActorSystem("piggyBank")

  val piggyBank = system.actorOf(Props(new PiggyBankActor), "piggyBankActor")

  override def onSessionStarted(request: SessionStartedRequest, session: Session): Unit = {
    logInvocation("onSessionStarted", request, session)
  }

  override def onLaunch(request: LaunchRequest, session: Session): SpeechletResponse = {
    logInvocation("onLaunch", request, session)
    SpeechletResponse.newAskResponse(
      "Welcome to Piggy Bank. How can I help you?",
      "You can tell me to check balances or ask about the balance of a specific account."
    )
  }

  override def onIntent(request: IntentRequest, session: Session): SpeechletResponse = {
    logInvocation("onIntent", request, session)
    request.getIntent.getName match {
      case "checkBalances" =>
        askResponse("There are not accounts right now because my creator has not written that code.")
      case "endSession" =>
        tellResponse("Goodbye.")
      case _ =>
        askResponse("I do not understand.")
    }
  }

  override def onSessionEnded(request: SessionEndedRequest, session: Session): Unit = {
    logInvocation("onSessionEnded", request, session)
  }

}

object PiggyBankSpeechlet {
  private def logInvocation(name: String, request: SpeechletRequest, session: Session): Unit = {
    val requestId = request.getRequestId
    val sessionId = session.getSessionId
    println(s"$name requestId=$requestId sessionId=$sessionId")
  }

  implicit def stringToOS(txt: String): OutputSpeech = {
    val os = new PlainTextOutputSpeech()
    os.setText(txt)
    os
  }
  implicit def stringToRP(txt: String): Reprompt = {
    val rp = new Reprompt()
    rp.setOutputSpeech(txt)
    rp
  }

  def askResponse(txt: String): SpeechletResponse = {
    SpeechletResponse.newAskResponse(txt, "Anything else?")
  }

  def tellResponse(txt: String): SpeechletResponse = {
    SpeechletResponse.newTellResponse(txt)
  }
}
