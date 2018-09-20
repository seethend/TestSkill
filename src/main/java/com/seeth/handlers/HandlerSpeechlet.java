package com.seeth.handlers;

import javax.sql.DataSource;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;

@Service
public class HandlerSpeechlet implements SpeechletV2 {
	
	@Autowired
	BeanFactory beanFactory;
	
//	private AnnotationConfigApplicationContext context;


	@Override
	public void onSessionStarted(
			SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
		
	}
	
	@Override
	public SpeechletResponse onLaunch(
			SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
		System.out.println("##################################################");
		System.out.println("Inside onLaunch");
		System.out.println("##################################################");
		PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
		outputSpeech.setText("Welcome to Users");
		
		PlainTextOutputSpeech repromtSpeech = new PlainTextOutputSpeech();
		repromtSpeech.setText("Now I'll ask a question");
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(repromtSpeech);
		return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
	}

	@Override
	public SpeechletResponse onIntent(
			SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		IntentRequest request = requestEnvelope.getRequest();
		
		Intent intent = request.getIntent();
		
		Session session = requestEnvelope.getSession();
		
		String intentName = intent.getName();
		
		String handlerBeanName = null;
		
		if(intentName.equals("AMAZON.FallbackIntent")) {
			handlerBeanName = "fallbackIntentHandler";
		}
		else if(intentName.equals("AMAZON.StopIntent")) {
			handlerBeanName = "stopIntentHandler";
		}
		else if(intentName.equals("AMAZON.HelpIntent")) {
			handlerBeanName = "helpIntentHandler";
		}
		else if(intentName.equals("AMAZON.CancelIntent")) {
			handlerBeanName = "cancelIntentHandler";
		}
		else if(intentName.equals("AMAZON.NavigateHomeIntent")) {
			handlerBeanName = "navigateHomeIntent";
		}
		else {
			handlerBeanName = intentName + "Handler";
		}
		
		Object handlerBean = beanFactory.getBean(handlerBeanName);
		
//		context = new AnnotationConfigApplicationContext("com.seeth");
		
//		Object handlerBean = context.getBean(handlerBeanName);
		
		IntentHandler intentHandler = (IntentHandler) handlerBean;
		
		return intentHandler.handleIntent(intent, request, session);
		
	}
	

	@Override
	public void onSessionEnded(
			SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
		
	}



}
