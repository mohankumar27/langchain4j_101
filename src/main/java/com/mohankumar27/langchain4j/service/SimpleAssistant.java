package com.mohankumar27.langchain4j.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface SimpleAssistant {

    @SystemMessage("You are a helpful assistant. Address user queries.")
    String chat(String userMessage);
}
