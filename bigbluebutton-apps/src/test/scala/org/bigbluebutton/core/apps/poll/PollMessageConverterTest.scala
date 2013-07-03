package org.bigbluebutton.core.apps.poll

import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.bigbluebutton.core.apps.poll.messages.R
import org.bigbluebutton.core.util.RandomStringGenerator._
import scala.collection.mutable.ArrayBuffer
import org.bigbluebutton.core.apps.poll.messages.Q
import org.bigbluebutton.core.apps.poll.messages.P

class PollMessageConverterTest {
  
  var samplePoll: P = null
  
	 @BeforeClass
	 def setUp() {
	 	val resps = new ArrayBuffer[R]()
	 	resps += new R("0", "Answer 1")
	 	resps += new R("1", "Answer 2")
	 	resps += new R("2", "Answer 3")
	 	
	 	val questions = new ArrayBuffer[Q]()
	 	questions += new Q("qID", "MULTI_CHOICE", "What is my name?", resps.toArray)
	 	
	 	samplePoll = new P("pollID", "My Sample Poll", questions.toArray)	   
	 }
	 
	 @Test(groups = Array[String]( "unit" ))
	 def convertCreatePollMessageTest(){
		val msg = "{\"title\":\"My sample poll\",\"questions\":[{\"questionType\":\"MULTI_CHOICE\",\"responses\":[\"Answer 1\",\"Answer 2\",\"Answer 3\"],\"question\":\"What is my name?\"}]}";

		val cut = new PollMessageConverter
		val pvp = cut.convertCreatePollMessage(msg)
		val gson = new Gson()
		
		assert(pvp.title.equals("My sample poll"), "Title not the same.")
		assert(pvp.questions.length == 1, "Number of questions is wrong. Must be [" + pvp.questions.length + "]")
		assert(pvp.questions(0).question.equals("What is my name?"), "First questions is [" + pvp.questions(0).question + "]")
	}
	
	 @Test(groups = Array[String]( "unit" ))
	 def convertUpdatePollMessageTest(){ 
	   val gson = new Gson()
		val cut = new PollMessageConverter
		val pvp = cut.convertUpdatePollMessage(gson.toJson(samplePoll))
		
		assert(pvp.title.equals(samplePoll.title), "Title not the same.")
		assert(pvp.questions.length == samplePoll.questions.length, "Number of questions is wrong. Must be [" + pvp.questions.length + "]")
		assert(pvp.questions(0).question.equals("What is my name?"), "First questions is [" + pvp.questions(0).question + "]")
	}

	 @Test(groups = Array[String]( "unit" ))
	 def convertTakePollMessageTest() { 
	//   msg = "{"id":"pollID-102","questions":[{"questionID":"q1","responses":["0","1"]}]}"
	   val r1 = new QuestionResponsesVO("q1", Array("1", "2", "3"))
	   
	   val pollResponse = new java.util.HashMap[String, Object]()
	   pollResponse.put("pollID", "pollID")
	   pollResponse.put("questions", Array(r1))
	   
	   val gson = new Gson()
	   val cut = new PollMessageConverter
	   val pvp = cut.convertTakePollMessage(gson.toJson(pollResponse))
		
	   assert(pvp.responses.length == 1, "Number of responses is 1")
	} 
}