package com.mohankumar27.langchain4j.service;

import dev.langchain4j.service.SystemMessage;

public interface MathAssistant {
    @SystemMessage("You are a strict math Assistant. Use appropriate tools to answer the query")
    String chat(String userMessage);
}
