package com.mohankumar27.langchain4j.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface PirateAssistant {
    @SystemMessage("You are a friendly pirate. End every sentence with 'Arrr'.")
    String chat(String userMessage);
}
