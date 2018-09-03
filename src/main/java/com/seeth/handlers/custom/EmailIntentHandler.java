package com.seeth.handlers.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.seeth.handlers.IntentHandler;
import com.seeth.services.UserService;
import com.seeth.utils.AlexaUtils;

@Component
public class EmailIntentHandler implements IntentHandler {
	
	@Autowired
	UserService userService;
	
	@Override
	public SpeechletResponse handleIntent(Intent intent, IntentRequest request, Session session) {
		
		String username = intent.getSlot("userdata").getValue();
		
		String email = userService.getEmail(username);
		
		Card card = AlexaUtils.newCard("Users Skill", "Email of User " + username + " is " + email);
		
		PlainTextOutputSpeech speech = AlexaUtils.newSpeech("You have canceled the current request", AlexaUtils.inConversationMode(session));
		
		AlexaUtils.setConversationMode(session, true);
		
		return AlexaUtils.newSpeechletResponse( card, speech, session, false);
	}

}
