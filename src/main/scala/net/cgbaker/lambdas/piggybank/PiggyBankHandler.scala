package net.cgbaker.lambdas.piggybank

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler

class PiggyBankHandler extends SpeechletRequestStreamHandler(new PiggyBankSpeechlet(), new java.util.HashSet[String]()) {

}
